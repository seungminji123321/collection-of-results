#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define PAGE_COUNT   50       // 작업 집합 페이지 수 (0~49)
#define SEQ_LENGTH   100000   // 한 워크로드당 참조 수열 길이
#define M_PI 3.14159265358979323846


// 균등 난수 [0,1)
static double uniform_rand() {
    return rand() / (RAND_MAX + 1.0);
}

// 1. 지역성 없는 순수 랜덤 워크로드
void gen_random(const char* filename, unsigned int seed) {
    srand(seed);
    FILE* fp = fopen(filename, "w");
    if (!fp) {
        perror(filename);
        return;
    }
    for (int i = 0; i < SEQ_LENGTH; i++) {
        int p = rand() % PAGE_COUNT;
        fprintf(fp, "%d\n", p);
    }
    fclose(fp);
}

// 2. 80-20 워크로드 (hot/cold mix)
void gen_8020(const char* filename, unsigned int seed) {
    srand(seed);
    FILE* fp = fopen(filename, "w");
    if (!fp) {
        perror(filename);
        return;
    }
    int hot_n = PAGE_COUNT / 5;
    if (hot_n < 1) hot_n = 1;
    for (int i = 0; i < SEQ_LENGTH; i++) {
        double r = uniform_rand();
        int p;
        if (r < 0.8) {
            p = rand() % hot_n;
        } else {
            p = hot_n + (rand() % (PAGE_COUNT - hot_n));
        }
        fprintf(fp, "%d\n", p);
    }
    fclose(fp);
}

// 3. 순차적 워크로드
void gen_sequential(const char* filename) {
    FILE* fp = fopen(filename, "w");
    if (!fp) {
        perror(filename);
        return;
    }
    for (int i = 0; i < SEQ_LENGTH; i++) {
        int p = i % PAGE_COUNT;
        fprintf(fp, "%d\n", p);
    }
    fclose(fp);
}

// 4. 가우시안 분포 워크로드
void gen_gaussian(const char* filename, unsigned int seed) {
    srand(seed);
    FILE* fp = fopen(filename, "w");
    if (!fp) {
        perror(filename);
        return;
    }
    double mu = (PAGE_COUNT - 1) / 6.0;
    double sigma = PAGE_COUNT / 10.0;
    for (int i = 0; i < SEQ_LENGTH; i++) {
        // Box-Muller 변환
        double u1 = uniform_rand();
        double u2 = uniform_rand();
        double z0 = sqrt(-2.0 * log(u1)) * cos(2.0 * M_PI * u2);
        int p = (int)round(mu + sigma * z0);
        if (p < 0) p = 0;
        if (p >= PAGE_COUNT) p = PAGE_COUNT - 1;
        fprintf(fp, "%d\n", p);
    }
    fclose(fp);
}

int main() {
    // 파일 단위 생성
    gen_random("random.txt", 2025);
    gen_8020("8020.txt", 2025);
    gen_sequential("sequential.txt");
    gen_gaussian("gaussian.txt", 42);
    printf("워크로드 파일 생성 완료: random.txt, 8020.txt, sequential.txt, gaussian.txt\n");
    return 0;
}
