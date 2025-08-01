#include "arduino_secrets.h"
/* 
  Sketch generated by the Arduino IoT Cloud Thing "Untitled"
  https://create.arduino.cc/cloud/things/77f1c152-3028-4c10-bbd1-d82039978228 

  Arduino IoT Cloud Variables description

  The following variables are automatically generated and updated when changes are made to the Thing

  float fHumidity;
  float fLightBrightness;
  float fTemperature;
  CloudColoredLight setRGBColor;
  int intSetMode;

  Variables which are marked as READ/WRITE in the Cloud Thing will also have functions
  which are called when their values are changed from the Dashboard.
  These functions are generated with the Thing and added at the end of this sketch.
*/

#include "thingProperties.h"
#include <ArduinoBLE.h>
#include <WiFiNINA.h>
#include <NeoPixelConnect.h>
#include <DHT.h>
#include <arduino-timer.h>
#include <BH1750.h>
#include <Wire.h>
#include <PDM.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include "myPinData.h"



// NeoPixelConnect myLEDstrip(NEOPIXELS_CONTROL_PIN, NUM_NEOPIXELS, pio1, 0);
// NeoPixelConnect myLEDstripMIC(MIC_REC_PIXEL_CONTROL_PIN, MIC_REC_NUM_OF_PIXEL, pio1, 1);
NeoPixelConnect myRingLEDstrip (RINGTYPE_LED_PIXEL_CONTROL_PIN, NUM_OF_RINGTYPE_LED_PIXEL, pio1, 0);
NeoPixelConnect myRingLEDstripMIC(MIC_REC_PIXEL_CONTROL_PIN, MIC_REC_NUM_OF_PIXEL, pio1, 1);

Adafruit_SSD1306 myOledDisplay(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, OLED_RESET);

// NeoPixelConnect p (RINGTYPE_LED_PIXEL_CONTROL_PIN, NUM_OF_RINGTYPE_LED_PIXEL, pio1, 0);
// NeoPixelConnect pp(MIC_REC_PIXEL_CONTROL_PIN, MIC_REC_NUM_OF_PIXEL, pio1, 1);

unsigned long dynamicLedCtrlInterval = 50;
unsigned long dynamicLedCtrlPreviousMillis = 0;

int rainbowCycleCycles = 0;
int numPixel = 0, seqColorWipeHandlerFlowCtrl = 0;
// 변수 정의
unsigned char redColor = 0, greenColor = 0, blueColor = 0;
DHT myDHT(DHT_PIN, DHTTYPE);
BH1750 myLIGHTSENSOR(BH1750_ADDRESS);

Timer<1, micros> timer; // create a timer with 1 task and microsecond resolution

unsigned long myTimeChk;
unsigned long myRandNumber;
// 버튼 상태 플래그
bool flgButtonPressedChk = false;
bool flgButtonPressedStatus = false;
bool flgButtonReleasedStatus = false;
bool flgExternalInterruptDetected = false;
bool flgOnOffCtrlStatus = false;
bool flgModeChangeCtrl = 0;
bool flgEmotionalModeEnable = false;
bool flgAutoBrightnessMode = false;
bool flgModeDynamicEffectA = false;
bool flgModeDynamicEffectB = false;
bool flgMachineLearningKeyWordSpeechControlMode = false;

int setIoT_Mode = 0;
int IoTMODE_WIFI = 1;
int IoTMODE_BLE = 2;
int IoTMODE_CHANGE_DEACTIVATE_BLE = 3;
int IoTMODE_CHANGE_ACTIVATE_BLE = 4;
int AI_MODE_PREPARE =5;
int AI_MODE_RUN_SPEECH_CONTROL = 6;
int AI_MODE_END_PROCESS = 7;

unsigned char cntKeyInTimingCtrl = 0;
unsigned char seqSystemModeControl = 0;
unsigned long btnChkTimeInterval = 100;
unsigned long btnChkTimePreviousMillis = 0;

unsigned char cntTimeCheckForDHT_Sensor = 0;
unsigned char cntTimeCheckForBH1750_Sensor = 0;
float hue, saturation, value;

static const unsigned char PROGMEM image_data_LOGOarray[] = {0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 
  0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 
  0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 
  0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 
  0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 
  0xff, 0xff, 0xff, 0xff, 0xc3, 0xe1, 0xff, 0xfe, 0x3f, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 
  0xff, 0xff, 0xff, 0xff, 0xc3, 0xe1, 0xff, 0xfe, 0x3f, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 
  0xff, 0xff, 0xff, 0xff, 0xc1, 0xc1, 0xff, 0xfe, 0x3f, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 
  0xff, 0xff, 0xff, 0xff, 0xc1, 0xc1, 0xc0, 0x0e, 0x30, 0xf0, 0x03, 0x80, 0x1f, 0xff, 0xff, 0xff, 
  0xff, 0xff, 0xff, 0xff, 0xc1, 0x81, 0xc7, 0x86, 0x03, 0xe1, 0xc3, 0x80, 0x3f, 0xff, 0xff, 0xff, 
  0xff, 0xff, 0xff, 0xff, 0xc8, 0x81, 0xf0, 0x06, 0x0f, 0xe0, 0x01, 0x83, 0xff, 0xff, 0xff, 0xff, 
  0xff, 0xff, 0xff, 0xff, 0xcc, 0x11, 0xc0, 0x06, 0x07, 0xe0, 0x01, 0x87, 0xff, 0xff, 0xff, 0xff, 
  0xff, 0xff, 0xff, 0xff, 0xcc, 0x11, 0x8f, 0x86, 0x23, 0xe3, 0xff, 0x87, 0xff, 0xff, 0xff, 0xff, 
  0xff, 0xff, 0xff, 0xff, 0xcc, 0x31, 0xc0, 0x06, 0x30, 0xe0, 0x03, 0x87, 0xff, 0xff, 0xff, 0xff, 
  0xff, 0xff, 0xff, 0xff, 0xce, 0x39, 0xf0, 0x0e, 0x3c, 0x3c, 0x03, 0x87, 0xff, 0xff, 0xff, 0xff, 
  0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 
  0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 
  0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 
  0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 
  0xff, 0xe0, 0x07, 0xff, 0xfc, 0x7f, 0xe3, 0xff, 0xff, 0xf8, 0xff, 0xf1, 0xff, 0xff, 0xff, 0xff, 
  0xff, 0xc3, 0xc3, 0xff, 0xff, 0xff, 0xe3, 0xff, 0xff, 0xf8, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 
  0xff, 0x87, 0xff, 0x8f, 0x0c, 0x70, 0x03, 0xc0, 0x3f, 0xf8, 0xff, 0xf1, 0xc0, 0x1f, 0x00, 0x7f, 
  0xff, 0x87, 0x03, 0x8f, 0x0c, 0x60, 0x03, 0x00, 0x0f, 0xf8, 0xff, 0xf1, 0xc0, 0x06, 0x00, 0x3f, 
  0xff, 0x87, 0x03, 0x8f, 0x0c, 0x63, 0xe3, 0x1f, 0x0f, 0xf8, 0xff, 0xf1, 0xc7, 0xc4, 0x3e, 0x3f, 
  0xff, 0x87, 0xe3, 0x8f, 0x0c, 0x63, 0xe3, 0x00, 0x0f, 0xf8, 0xff, 0xf1, 0xc7, 0xc4, 0x00, 0x3f, 
  0xff, 0x87, 0xe3, 0x8f, 0x0c, 0x63, 0xe3, 0x1f, 0xff, 0xf8, 0xff, 0xf1, 0xc7, 0xc4, 0x3f, 0xff, 
  0xff, 0xc0, 0x03, 0x80, 0x0c, 0x60, 0x03, 0x00, 0x1f, 0xf8, 0x00, 0x31, 0xc7, 0xc6, 0x00, 0x3f, 
  0xff, 0xf0, 0x03, 0xc0, 0x0c, 0x78, 0x03, 0xc0, 0x1f, 0xf8, 0x00, 0x31, 0xc7, 0xc7, 0x00, 0x3f, 
  0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 
  0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 
  0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 
  0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff
};

void setup() {
  // Initialize serial and wait for port to open:
  Serial.begin(9600); //while(!Serial);
Serial.println("A");
  myDHT.begin(); delay(100);
  Wire.begin(); delay(100);
  myLIGHTSENSOR.begin(); delay(100);
  //while (!Serial); // Wait for Serial to be ready
  pinMode(EXINTERRUPT_PIN, INPUT_PULLUP);
  pinMode(LED_BUILTIN, OUTPUT);
  if(!myOledDisplay.begin(SSD1306_SWITCHCAPVCC, SCREEN_ADDRESS)){
    Serial.println(F("SSD1306 allocation failed"));
    for(;;);
  }
  

  myOledDisplay.clearDisplay();
  myOledDisplay.drawBitmap(0,0,image_data_LOGOarray, 128, 32, 1);
  myOledDisplay.display();
  delay(2000);

  timer.every(1000000, myTaskScheduler);
\

  // NeoPixel 초기화
  myRingLEDstrip.neoPixelInit(NEOPIXELS_CONTROL_PIN, NUM_NEOPIXELS);
  myRingLEDstripMIC.neoPixelInit(MIC_REC_PIXEL_CONTROL_PIN, MIC_REC_NUM_OF_PIXEL);

  // myRingLEDstrip.neoPixelInit(RINGTYPE_LED_PIXEL_CONTROL_PIN, NUM_OF_RINGTYPE_LED_PIXEL);
  // myRingLEDstripMIC.neoPixelInit(MIC_REC_PIXEL_CONTROL_PIN, MIC_REC_NUM_OF_PIXEL);

  Serial.println("External Interrupt test using Touch sensor");
  attachInterrupt(EXINTERRUPT_PIN, onInterruptTriggerChangeHandler, CHANGE);

  // Defined in thingProperties.h
  initProperties();

  // Connect to Arduino IoT Cloud
  ArduinoCloud.begin(ArduinoIoTPreferredConnection);

  //setDebugMessageLevel(2);
  //ArduinoCloud.printDebugInfo();

  // NeoPixel 초기화 및 설정
  myRingLEDstrip.neoPixelFill(redColor, greenColor, blueColor, true);
  myRingLEDstripMIC.neoPixelFill(100, 0, 0, true);

  myRingLEDstrip.neoPixelInit(RINGTYPE_LED_PIXEL_CONTROL_PIN, NUM_OF_RINGTYPE_LED_PIXEL); delay(1000);
  myRingLEDstripMIC.neoPixelInit(MIC_REC_PIXEL_CONTROL_PIN, MIC_REC_NUM_OF_PIXEL); delay(1000);
  myRingLEDstripMIC.neoPixelFill(255,0,0,true); delay(1000);
  myRingLEDstripMIC.neoPixelFill(0,255,0,true); delay(1000);
  myRingLEDstripMIC.neoPixelFill(0,0,255,true); delay(1000);
  myRingLEDstripMIC.neoPixelFill(100,100,100,true); delay(1000);

}

void loop() {
  ArduinoCloud.update();
  timer.tick();
  myBtnStatusCheckHandler();  
}

void myBtnStatusCheckHandler() {
  if(!flgExternalInterruptDetected) return;

  if((unsigned long)(millis() - btnChkTimePreviousMillis) >= btnChkTimeInterval) {  
    if(!digitalRead(EXINTERRUPT_PIN)) {
      flgButtonPressedStatus  = true;
      flgButtonReleasedStatus = false;
    }

    if(flgButtonPressedChk)  {      
      if(!flgButtonPressedStatus)  {
        Serial.println("Touch Button Pressed");
        cntKeyInTimingCtrl++;

        if(!flgOnOffCtrlStatus && (cntKeyInTimingCtrl>=KEY_IN_TIME_SHORT_CHECK_VALUE)) {
          Serial.println("Mode 1: White Color");
          cntKeyInTimingCtrl = 0;
          flgButtonPressedStatus  = true;
          flgButtonReleasedStatus = false;
          flgOnOffCtrlStatus = true;
          flgModeChangeCtrl = true;
          seqSystemModeControl = 5;

          digitalWrite(LED_BUILTIN, ON);
          myRingLEDstrip.neoPixelFill(255, 255, 255, true);
          myRingLEDstripMIC.neoPixelFill(255, 255, 255, true);

          flgExternalInterruptDetected = false;
        }

        else if(flgOnOffCtrlStatus && (cntKeyInTimingCtrl>=KEY_IN_TIME_LONG_CHECK_VALUE)) {
          Serial.println("Power Off");
          cntKeyInTimingCtrl = 0;
          flgButtonPressedStatus  = true;
          flgButtonReleasedStatus = false;
          flgOnOffCtrlStatus = false;
          seqSystemModeControl = 0;

          digitalWrite(LED_BUILTIN, OFF);

          myRingLEDstrip.neoPixelFill(0, 0, 0, true);
          myRingLEDstripMIC.neoPixelFill(0, 0, 0, true);

          flgExternalInterruptDetected = false;
        }

        if(flgOnOffCtrlStatus) {
          switch(seqSystemModeControl)  {
            case 5:
              if(!flgModeChangeCtrl) seqSystemModeControl = 10;
            break;
            case 10:
              Serial.println("Mode 2: Red Color");

              myRingLEDstrip.neoPixelFill(255, 0, 0, true);
              myRingLEDstripMIC.neoPixelFill(255, 50, 50, true);

              flgModeChangeCtrl = true;
              seqSystemModeControl = 15;
            break;

            case 15:
              if(!flgModeChangeCtrl) seqSystemModeControl = 20;
            break;

            case 20:
              Serial.println("Mode 3: Green Color");

              myRingLEDstrip.neoPixelFill(0, 255, 0, true);
              myRingLEDstripMIC.neoPixelFill(50, 255, 50, true);
              flgModeChangeCtrl = true;
              seqSystemModeControl = 25;
            break;

            case 25:
              if(!flgModeChangeCtrl) seqSystemModeControl = 30;
            break;

            case 30:
              Serial.println("Mode 4: Blue Color");

              myRingLEDstrip.neoPixelFill(0, 0, 255, true);
              myRingLEDstripMIC.neoPixelFill(50, 50, 255, true);

              flgModeChangeCtrl = true;
              seqSystemModeControl = 35;
            break;

            case 35:
              if(!flgModeChangeCtrl) seqSystemModeControl = 40;
            break;

            case 40:
              Serial.println("Mode 5: All OFF");

              myRingLEDstrip.neoPixelFill(0, 0, 0, true);
              myRingLEDstripMIC.neoPixelFill(0, 0, 0, true);

              flgModeChangeCtrl = true;
              seqSystemModeControl = 45;
            break;

            case 45:
              if(!flgModeChangeCtrl) seqSystemModeControl = 50;
            break;

            case 50: 
              Serial.println("Mode 1: White Color");
              myRingLEDstrip.neoPixelFill(255, 255, 255, true);
              myRingLEDstripMIC.neoPixelFill(255, 255, 255, true);
              flgModeChangeCtrl = true;
              seqSystemModeControl = 55;
            break;

            case 55:
              if(!flgModeChangeCtrl) seqSystemModeControl = 10;
            break;

            default: break;
          }
        }
      }
    }

    else  {
      if(!flgButtonReleasedStatus) {
        Serial.println("Touch Button Released");
        cntKeyInTimingCtrl = 0;
        flgButtonPressedStatus  = false;
        flgButtonReleasedStatus = true;
        flgModeChangeCtrl = false;
        flgExternalInterruptDetected = false;
      }
    }
    btnChkTimePreviousMillis = millis();
  }
}
void sendHSV_ColorDataToCloud(void){
  rgb_to_hsv(redColor, greenColor, blueColor);

  setRGBColor.setHue(hue);
  setRGBColor.setSaturation(saturation);
  setRGBColor.setBrightness(value);

}

void rgb_to_hsv(double r, double g, double b){
  r = r/255.0;
  g = g/255.0;
  b = b/255.0;

  double cmax = max(r,max(g,b));
  double cmin = min(r,min(g,b));
  double diff = cmax - cmin;
  hue = -1; saturation = -1;

  if (cmax == cmin) hue = 0;
  else if(cmax == r) hue = fmod(60*((g -b)/diff)+360, 360);
  else if (cmax ==g) hue = fmod(60*((b-r)/diff)+120, 360);
  else if (cmax ==b) hue = fmod(60*((r-g)/diff)+240, 360);

  if (cmax == 0) saturation = 0;
  else saturation = (diff/cmax)*100;

  value = cmax * 1000;
}
void onInterruptTriggerChangeHandler() {
  if (digitalRead(EXINTERRUPT_PIN)) {
    flgButtonPressedChk = true;
    flgExternalInterruptDetected = true;
  } else {
    flgButtonPressedChk = false;
    flgExternalInterruptDetected = true;
  }
}

bool myTaskScheduler (void*){
  cntTimeCheckForDHT_Sensor++;
  cntTimeCheckForBH1750_Sensor++;

  if(cntTimeCheckForDHT_Sensor >= TIME_TICK_DHT_SENSOR_CHECK) {
    cntTimeCheckForDHT_Sensor = 0;
    myDHT_SensorDAQ();
  }

  if(cntTimeCheckForBH1750_Sensor >= TIME_TICK_BH1750_SENSOR_CHECK) {
    cntTimeCheckForBH1750_Sensor = 0;
    myLightSensorDAQ();
  }
  return 1;
}

void myDHT_SensorDAQ(void)  {
  float h = myDHT.readHumidity();
  float t = myDHT.readTemperature();
  unsigned char getTempData;
  
  #if(DEBUG_MODE_ENABLE)
    Serial.print(F("Humidity: ")); Serial.print(h);
    Serial.print(F("%  Temp.: ")); Serial.print(t); Serial.println(F("°C "));
  #endif

  
  myOledDisplay.setTextColor(SSD1306_WHITE, SSD1306_BLACK);
  myOledDisplay.fillRect(0, 0, 128, 8, BLACK); myOledDisplay.setCursor(0,0); myOledDisplay.print(" T: ");  myOledDisplay.print(t); myOledDisplay.print("["); myOledDisplay.print((char)247); myOledDisplay.print("C"); myOledDisplay.print("]");     myOledDisplay.display();
  myOledDisplay.fillRect(0, 8, 128, 8, BLACK); myOledDisplay.setCursor(0,8); myOledDisplay.print(" H: ");  myOledDisplay.print(h); myOledDisplay.print((String)"[%]");     myOledDisplay.display();

  if(1){
    h = 81;
    t = 23.5;
    fHumidity = h;
    fTemperature = t;

    if (flgEmotionalModeEnable){
      getTempData = t + h /100;
      if (h> 70) getTempData +=5;
      #if(DEBUG_MODE_ENABLE)
      Serial.print(getTempData);
      Serial.println(F(" 'C"));
      #endif
      if (getTempData<5) {
        redColor = 255; greenColor= 89; blueColor = 89;}
        else if(getTempData <10){redColor = 254; greenColor = 155; blueColor = 120;}
        else if(getTempData <25){redColor = 255; greenColor = 255; blueColor = 255;}
        else if(getTempData <30){redColor = 58; greenColor = 199; blueColor = 131;}
        else {redColor = 0; greenColor = 0; blueColor = 250;}

        myRingLEDstrip.neoPixelFill(redColor, greenColor, blueColor, true);
        sendHSV_ColorDataToCloud();

        
      }
    }
  }


void myLightSensorDAQ(void) {
  unsigned char getBrightness;
  float lux = myLIGHTSENSOR.readLightLevel();
  #if(DEBUG_MODE_ENABLE)
    Serial.print("Light: ");  Serial.print(lux);  Serial.println("[lx]");
  #endif

  myOledDisplay.setTextColor(SSD1306_WHITE, SSD1306_BLACK);
  myOledDisplay.fillRect(0, 16, 128, 8, BLACK); myOledDisplay.setCursor(0,16); myOledDisplay.print(" L: ");  myOledDisplay.print(lux); myOledDisplay.print("[lx]");     myOledDisplay.display();
  myOledDisplay.setTextColor(SSD1306_WHITE, SSD1306_BLACK);
  myOledDisplay.fillRect(0, 24, 128, 8, BLACK); myOledDisplay.setCursor(0,24); myOledDisplay.print(" Maker Guide Line"); myOledDisplay.display();

  if(1) fLightBrightness = lux;

  if(flgAutoBrightnessMode){
    if(getBrightness > 10000) getBrightness = 100000;
    getBrightness = map(lux, 0, 10000, 255, 0);
    myRingLEDstrip.neoPixelFill(redColor*((float)getBrightness/255), greenColor*((float)getBrightness/255), blueColor*((float)getBrightness/255), true);

    setRGBColor.setBrightness(map(getBrightness, 0, 255, 0, 100));
  }
}

bool mySensorDataAcquisitionFunction(void*){
  myDHT_SensorDAQ();
  myLightSensorDAQ();
  return 1;
}
void myRainbowCycleEffect(){
  uint16_t i;
  for(i = 0; i< NUM_OF_RINGTYPE_LED_PIXEL; i++){
    Wheel(((i * 256/NUM_OF_RINGTYPE_LED_PIXEL)+ rainbowCycleCycles) & 255);
    myRingLEDstrip.neoPixelSetValue(i,redColor, greenColor, blueColor, true);
    Serial.print(i); Serial.print(" | ");Serial.print(redColor); Serial.print(" | ");
    Serial.print(greenColor); Serial.print(" | ");Serial.print(blueColor); 
  }
  rainbowCycleCycles ++;

  if (rainbowCycleCycles >= 256)rainbowCycleCycles = 0;
}

void myRainbowEffect(){
  uint16_t i;
  for(i = 0; i<NUM_OF_RINGTYPE_LED_PIXEL; i++){
    Wheel((i+rainbowCycleCycles) & 255); myRingLEDstrip.neoPixelSetValue(i, redColor, greenColor, blueColor, true);
    Serial.print(i); Serial.print(" | ");Serial.print(redColor); Serial.print(" | ");
    Serial.print(greenColor); Serial.print(" | ");Serial.print(blueColor);
  }
  rainbowCycleCycles++;

  if(rainbowCycleCycles >= 256) rainbowCycleCycles = 0;
}

void myColorWipeEffect(uint8_t r, uint8_t g, uint8_t b){
  switch(seqColorWipeHandlerFlowCtrl){
    case 0: numPixel = 0; seqColorWipeHandlerFlowCtrl++; break;
    case 1: myRingLEDstrip.neoPixelSetValue(numPixel, r, g, b,true); seqColorWipeHandlerFlowCtrl++; break;
    case 2: numPixel++; seqColorWipeHandlerFlowCtrl++;break;
    case 3: if(numPixel > NUM_OF_RINGTYPE_LED_PIXEL)seqColorWipeHandlerFlowCtrl = 0;
    else seqColorWipeHandlerFlowCtrl = 1;
    break;
    case 4:
    seqColorWipeHandlerFlowCtrl = 0;
    break;
  }
}

uint32_t Wheel(byte WheelPos){
  WheelPos = 255 - WheelPos;

  if (WheelPos< 85){
    redColor = 255-WheelPos*3; greenColor = 0; blueColor = WheelPos * 3;
    return 1;
  }
  if (WheelPos<170){
    WheelPos -= 85; redColor = 0; greenColor = WheelPos *3; blueColor = 255 - WheelPos *3;
    return 1;
  }
  WheelPos -= 170;
  redColor = WheelPos *3; greenColor = 255 - WheelPos * 3; blueColor = 0;
  return 1;
}
/*
  Since IntSetMode is READ_WRITE variable, onIntSetModeChange() is
  executed every time a new value is received from IoT Cloud.
*/
void onIntSetModeChange() {
  switch(intSetMode){
    case 0 : 
    Serial.println("0");
    flgEmotionalModeEnable = 0;
    flgModeDynamicEffectA = 0;
    flgModeDynamicEffectB = 0;
    flgAutoBrightnessMode = 0;
    flgMachineLearningKeyWordSpeechControlMode = 0;
    break;

    case 1:
    //setIoT_Mode = IoTMODE_CHANGE_ACTIVATE_BLE;
    Serial.println("1");
    intSetMode = 0;
    flgEmotionalModeEnable = 0;
    flgModeDynamicEffectA = 0;
    flgModeDynamicEffectB = 0;
    flgAutoBrightnessMode = 0;
    flgMachineLearningKeyWordSpeechControlMode = 0;
    break;

    case 2:
    Serial.println("2");
    flgEmotionalModeEnable = 1;
    flgModeDynamicEffectA = 0;
    flgModeDynamicEffectB = 0;
    flgAutoBrightnessMode = 0;
    flgMachineLearningKeyWordSpeechControlMode = 0;
    break;

    case 3:
    Serial.println("3");
    flgEmotionalModeEnable = 0;
    flgModeDynamicEffectA = 0;
    flgModeDynamicEffectB = 0;
    flgAutoBrightnessMode = 1;
    flgMachineLearningKeyWordSpeechControlMode = 0;
    break;

    case 4:
    Serial.println("4");
    //setIoT_Mode = AI_MODE_PREPARE;
    flgEmotionalModeEnable = 0;
    flgModeDynamicEffectA = 0;
    flgModeDynamicEffectB = 0;
    flgAutoBrightnessMode = 0;
    flgMachineLearningKeyWordSpeechControlMode = 1;
    break;

    case 5:
    Serial.println("5");
    flgEmotionalModeEnable = 0;
    flgModeDynamicEffectA = 1;
    flgModeDynamicEffectB = 0;
    flgAutoBrightnessMode = 0;
    flgMachineLearningKeyWordSpeechControlMode = 0;
    while(intSetMode == 5){
      if((unsigned long)(millis()-dynamicLedCtrlPreviousMillis) >= dynamicLedCtrlInterval){
    myColorWipeEffect(255, 0, 0);
    myColorWipeEffect(0,255,0);
    myColorWipeEffect(0,0,255);
    delay(100);
    dynamicLedCtrlPreviousMillis = millis();}}
    break;

    case 6:
    Serial.println("6");
    flgEmotionalModeEnable = 0;
    flgModeDynamicEffectA = 0;
    flgModeDynamicEffectB = 1;
    flgAutoBrightnessMode = 0;
    flgMachineLearningKeyWordSpeechControlMode = 0;
    break;

    case 7:
    Serial.println("7");
    flgEmotionalModeEnable = 0;
    flgModeDynamicEffectA = 0;
    flgModeDynamicEffectB = 0;
    flgAutoBrightnessMode = 0;
    flgMachineLearningKeyWordSpeechControlMode = 0;
    break;

    case 8:
    flgEmotionalModeEnable = 0;
    flgModeDynamicEffectA = 0;
    flgModeDynamicEffectB = 0;
    flgAutoBrightnessMode = 0;
    flgMachineLearningKeyWordSpeechControlMode = 0;
    break;

    default : break;
  }
}

/*
  Since SetRGBColor is READ_WRITE variable, onSetRGBColorChange() is
  executed every time a new value is received from IoT Cloud.
*/
//====================================================================
//대시보드에서 링LED제어
void onSetRGBColorChange() {
  if(setRGBColor.getSwitch()){
    #if(DEBUG_MODE_ENABLE)
    Serial.println("Lamp On");
    #endif
    if((1) && !flgAutoBrightnessMode)
      setRGBColor.getValue().getRGB(redColor, greenColor, blueColor);
  }
  else {
    #if(DEBUG_MODE_ENABLE)
    Serial.println("Lamp Off");
    #endif
    redColor= 0; greenColor = 0; blueColor = 0;
    sendHSV_ColorDataToCloud();
  }

  myRingLEDstrip.neoPixelFill(redColor, greenColor, blueColor, true);
  flgModeDynamicEffectA = 0;
  intSetMode = 0;
}
//=======================================================================
/*
  Since FHumidity is READ_WRITE variable, onFHumidityChange() is
  executed every time a new value is received from IoT Cloud.
*/
void onFHumidityChange() {
  // Add your code here to act upon FHumidity change
}

/*
  Since FLightBrightness is READ_WRITE variable, onFLightBrightnessChange() is
  executed every time a new value is received from IoT Cloud.
*/
void onFLightBrightnessChange() {
  // Add your code here to act upon FLightBrightness change
}

/*
  Since FTemperature is READ_WRITE variable, onFTemperatureChange() is
  executed every time a new value is received from IoT Cloud.
*/
void onFTemperatureChange() {
  // Add your code here to act upon FTemperature change
}
