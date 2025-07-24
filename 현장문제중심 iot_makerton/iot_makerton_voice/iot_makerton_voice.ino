#define EIDSP_QUANTIZE_FILTERBANK   0
#include <clorm-project-1_inferencing.h>
#include <PDM.h>
#include <Adafruit_NeoPixel.h>


typedef struct {
    int16_t *buffer;
    uint8_t buf_ready;
    uint32_t buf_count;
    uint32_t n_samples;
} inference_t;

static inference_t inference;
static signed short sampleBuffer[2048];
static bool debug_nn = false; // Set this to true to see e.g. features generated from the raw signal
static volatile bool record_ready = false;

/* NeoPixel 설정 */
#define PIN            6   // 네오픽셀 데이터 핀
#define NUMPIXELS      2   // 네오픽셀 개수
Adafruit_NeoPixel pixels(NUMPIXELS, PIN, NEO_GRB + NEO_KHZ800);

/**
 * @brief      Arduino setup function
 */
void setup()
{
    // put your setup code here, to run once:
    Serial.begin(115200);
    // comment out the below line to cancel the wait for USB connection (needed for native USB)
    while (!Serial);
    Serial.println("Edge Impulse Inferencing Demo");

    // 내장 LED 핀 설정
    //pinMode(LED_BUILTIN, OUTPUT);
    //digitalWrite(LED_BUILTIN, LOW);

    // NeoPixel 초기화
    pixels.begin();
    pixels.show(); // Initialize all pixels to 'off'

    // summary of inferencing settings (from model_metadata.h)
    ei_printf("Inferencing settings:\n");
    ei_printf("\tInterval: ");
    ei_printf_float((float)EI_CLASSIFIER_INTERVAL_MS);
    ei_printf(" ms.\n");
    ei_printf("\tFrame size: %d\n", EI_CLASSIFIER_DSP_INPUT_FRAME_SIZE);
    ei_printf("\tSample length: %d ms.\n", EI_CLASSIFIER_RAW_SAMPLE_COUNT / 16);
    ei_printf("\tNo. of classes: %d\n", sizeof(ei_classifier_inferencing_categories) / sizeof(ei_classifier_inferencing_categories[0]));

    if (microphone_inference_start(EI_CLASSIFIER_RAW_SAMPLE_COUNT) == false) {
        ei_printf("ERR: Could not allocate audio buffer (size %d), this could be due to the window length of your model\r\n", EI_CLASSIFIER_RAW_SAMPLE_COUNT);
        return;
    }
}

/**
 * @brief      Arduino main function. Runs the inferencing loop.
 */
void loop()
{
    ei_printf("Starting inferencing in 2 seconds...\n");

    // 녹화 시작 알림 (빨간색 네오픽셀)
    //digitalWrite(LED_BUILTIN, HIGH);
    set_neopixel_color(255, 0, 0); // Red
    delay(2000);

    ei_printf("Recording...\n");

    bool m = microphone_inference_record();
    if (!m) {
        ei_printf("ERR: Failed to record audio...\n");
        return;
    }

    ei_printf("Recording done\n");

    // 녹화 종료 알림 (녹색 네오픽셀)
    //digitalWrite(LED_BUILTIN, LOW);
    set_neopixel_color(0, 255, 0); // Green

    signal_t signal;
    signal.total_length = EI_CLASSIFIER_RAW_SAMPLE_COUNT;
    signal.get_data = &microphone_audio_signal_get_data;
    ei_impulse_result_t result = { 0 };

    EI_IMPULSE_ERROR res = run_classifier_continuous(&signal, &result, debug_nn);
    if (res != EI_IMPULSE_OK) {
        ei_printf("ERR: Failed to run classifier (%d)\n", res);
        return;
    }

    // print inference return code
    ei_printf("run_classifier returned: %d\r\n", res);
    print_inference_result(result);

    // 인식 결과에 따른 NeoPixel 제어
    control_neopixel(result);
}

/**
 * @brief      PDM buffer full callback
 *             Copy audio data to app buffers
 */
static void pdm_data_ready_inference_callback(void)
{
    int bytesAvailable = PDM.available();

    // read into the sample buffer
    int bytesRead = PDM.read((char *)&sampleBuffer[0], bytesAvailable);

    if ((inference.buf_ready == 0) && (record_ready == true)) {

        for(int i = 0; i < bytesRead>>1; i++) {
            inference.buffer[inference.buf_count++] = sampleBuffer[i];

            if(inference.buf_count >= inference.n_samples) {
                inference.buf_count = 0;
                inference.buf_ready = 1;
                break;
            }
        }
    }
}

/**
 * @brief      Init inferencing struct and setup/start PDM
 *
 * @param[in]  n_samples  The n samples
 *
 * @return     { description_of_the_return_value }
 */
static bool microphone_inference_start(uint32_t n_samples)
{
    inference.buffer = (int16_t *)malloc(n_samples * sizeof(int16_t));

    if(inference.buffer == NULL) {
        return false;
    }

    inference.buf_count  = 0;
    inference.n_samples  = n_samples;
    inference.buf_ready  = 0;

    // configure the data receive callback
    PDM.onReceive(pdm_data_ready_inference_callback);

    PDM.setBufferSize(2048);
    delay(250);

    // initialize PDM with:
    // - one channel (mono mode)
    if (!PDM.begin(1, EI_CLASSIFIER_FREQUENCY)) {
        ei_printf("ERR: Failed to start PDM!");
        microphone_inference_end();
        return false;
    }

    // optionally set the gain, defaults to 24
    // Note: values >=52 not supported
    //PDM.setGain(40);

    return true;
}

/**
 * @brief      Wait on new data
 *
 * @return     True when finished
 */
static bool microphone_inference_record(void)
{
    bool ret = true;

    record_ready = true;
    while (inference.buf_ready == 0) {
        delay(10);
    }

    inference.buf_ready = 0;
    record_ready = false;

    return ret;
}

/**
 * Get raw audio signal data
 */
static int microphone_audio_signal_get_data(size_t offset, size_t length, float *out_ptr)
{
    numpy::int16_to_float(&inference.buffer[offset], out_ptr, length);

    return 0;
}

/**
 * @brief      Stop PDM and release buffers
 */
static void microphone_inference_end(void)
{
    PDM.end();
    ei_free(inference.buffer);
}

void print_inference_result(ei_impulse_result_t result) {

    // Print how long it took to perform inference
    ei_printf("Timing: DSP %d ms, inference %d ms, anomaly %d ms\r\n",
            result.timing.dsp,
            result.timing.classification,
            result.timing.anomaly);

    ei_printf("Predictions:\r\n");
    for (uint16_t i = 0; i < EI_CLASSIFIER_LABEL_COUNT; i++) {
        ei_printf("  %s: ", ei_classifier_inferencing_categories[i]);
        ei_printf("%.5f\r\n", result.classification[i].value);
    }

    // Print anomaly result (if it exists)
#if EI_CLASSIFIER_HAS_ANOMALY == 1
    ei_printf("Anomaly prediction: %.3f\r\n", result.anomaly);
#endif
}

/**
 * @brief      Control NeoPixel based on inference result
 */
void control_neopixel(ei_impulse_result_t result) {
    for (uint16_t i = 0; i < EI_CLASSIFIER_LABEL_COUNT; i++) {
        if (result.classification[i].value > 0.5) { // Threshold for classification
            if (strcmp(ei_classifier_inferencing_categories[i], "키워드1") == 0) {
                set_neopixel_color(255, 0, 0); // Red for 키워드1
            } else if (strcmp(ei_classifier_inferencing_categories[i], "키워드2") == 0) {
                set_neopixel_color(0, 255, 0); // Green for 키워드2
            } else if (strcmp(ei_classifier_inferencing_categories[i], "키워드3") == 0) {
                set_neopixel_color(0, 0, 255); // Blue for 키워드3
            }
        }
    }
}

/**
 * @brief      Set NeoPixel color
 */
void set_neopixel_color(uint8_t red, uint8_t green, uint8_t blue) {
    for (int i = 0; i < NUMPIXELS; i++) {
        pixels.setPixelColor(i, pixels.Color(red, green, blue));
    }
    pixels.show();
}

#if !defined(EI_CLASSIFIER_SENSOR) || EI_CLASSIFIER_SENSOR != EI_CLASSIFIER_SENSOR_MICROPHONE
#error "Invalid model for current sensor."
#endif
