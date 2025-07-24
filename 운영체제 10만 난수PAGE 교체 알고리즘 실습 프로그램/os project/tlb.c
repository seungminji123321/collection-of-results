#include <stdio.h>
#include <stdlib.h>

#define MAX_FRAMES 100
#define PAGESET_SIZE 50

// ---------- LRU ----------
typedef struct {
    int page;
    int lastUsed;
} LRUFrame;

void simulateLRU_counts(const char* filename, int frameSize, int* hit, int* fault) {
    FILE* fp = fopen(filename, "r");
    if (!fp) return;
    LRUFrame frames[MAX_FRAMES];
    int time = 0, h = 0, f = 0, page;

    for (int i = 0; i < frameSize; i++) {
        frames[i].page = -1;
        frames[i].lastUsed = -1;
    }
    while (fscanf(fp, "%d", &page) == 1) {
        int found = 0;
        for (int i = 0; i < frameSize; i++) {
            if (frames[i].page == page) {
                frames[i].lastUsed = time++;
                h++;
                found = 1;
                break;
            }
        }
        if (!found) {
            int lruIdx = 0;
            for (int i = 1; i < frameSize; i++) {
                if (frames[i].lastUsed < frames[lruIdx].lastUsed) {
                    lruIdx = i;
                }
            }
            frames[lruIdx].page = page;
            frames[lruIdx].lastUsed = time++;
            f++;
        }
    }
    fclose(fp);
    *hit = h;
    *fault = f;
}

// ---------- FIFO ----------
typedef struct { int page; } FIFOFrame;

void simulateFIFO_counts(const char* filename, int frameSize, int* hit, int* fault) {
    FILE* fp = fopen(filename, "r");
    if (!fp) {
        return;
    }
    FIFOFrame frames[MAX_FRAMES];
    int next = 0, h = 0, f = 0, page;

    for (int i = 0; i < frameSize; i++) {
        frames[i].page = -1;
    }
    while (fscanf(fp, "%d", &page) == 1) {
        int found = 0;
        for (int i = 0; i < frameSize; i++) {
            if (frames[i].page == page) { 
                h++; 
                found = 1; 
                break; 
            }
        }
        if (!found) { 
            frames[next].page = page; 
            next = (next + 1) % frameSize; 
            f++; 
        }
    }
    fclose(fp);
    *hit = h;
    *fault = f;
}

// ---------- CLOCK ----------
typedef struct { int page; int refBit; } ClockFrame;

void simulateClock_counts(const char* filename, int frameSize, int* hit, int* fault) {
    FILE* fp = fopen(filename, "r");
    if (!fp) return;
    ClockFrame frames[MAX_FRAMES];
    int hand = 0, h = 0, f = 0, page;

    for (int i = 0; i < frameSize; i++) { 
        frames[i].page = -1; 
        frames[i].refBit = 0; 
    }
    while (fscanf(fp, "%d", &page) == 1) {
        int found = 0;
        for (int i = 0; i < frameSize; i++) {
            if (frames[i].page == page) { 
                frames[i].refBit = 1;
                h++; 
                found = 1; 
                break; 
            }
        }
        if (!found) {
            while (frames[hand].refBit == 1) { 
                frames[hand].refBit = 0; 
                hand = (hand + 1) % frameSize; 
            }
            frames[hand].page = page;
            frames[hand].refBit = 1;
            hand = (hand + 1) % frameSize;
            f++;
        }
    }
    fclose(fp);
    *hit = h;
    *fault = f;
}

// ---------- MRU ----------
void simulateMRU_counts(const char* filename, int frameSize, int* hit, int* fault) {
    FILE* fp = fopen(filename, "r");
    if (!fp) return;
    LRUFrame frames[MAX_FRAMES];
    int time = 0, h = 0, f = 0, page;

    for (int i = 0; i < frameSize; i++) { 
        frames[i].page = -1; 
        frames[i].lastUsed = -1; 
    }
    while (fscanf(fp, "%d", &page) == 1) {
        int found = 0;
        for (int i = 0; i < frameSize; i++) {
            if (frames[i].page == page) { 
                frames[i].lastUsed = time++; 
                h++; 
                found = 1; 
                break; 
            }
        }
        if (!found) {
            int mruIdx = 0;
            for (int i = 1; i < frameSize; i++) {
                if (frames[i].lastUsed > frames[mruIdx].lastUsed) {
                    mruIdx = i;
                }
            }
            for (int i = 0; i < frameSize; i++) {
                if (frames[i].page == -1) { 
                    mruIdx = i; 
                    break; 
                }
            }
            frames[mruIdx].page = page;
            frames[mruIdx].lastUsed = time++;
            f++;
        }
    }
    fclose(fp);
    *hit = h;
    *fault = f;
}

int main() {
    int algoChoice, fileChoice;

    const char* filenames[] = { "random.txt", "8020.txt", "sequential.txt", "gaussian.txt" };
    const char* algoNames[] = { "LRU", "FIFO", "CLOCK", "MRU" };
    const char* fileStems[] = { "random", "8020", "sequential", "gaussian" };

    printf("==== 페이지 접근 시퀀스 선택 ====\n");
    printf("1. 지역성 없는 랜덤 워크로드\n2. 80-20 워크로드\n3. 순차적 워크로드\n4. 가우시안 분포 워크로드\n선택: ");
    scanf("%d", &fileChoice);
    if (fileChoice < 1 || fileChoice > 4) { 
        printf("잘못된 시퀀스 선택.\n"); 
        return 1; 
    }

    printf("\n==== 알고리즘 선택 ====\n");
    printf("1. LRU\n2. FIFO\n3. CLOCK\n4. MRU\n선택: ");
    scanf("%d", &algoChoice);
    if (algoChoice < 1 || algoChoice > 4) { 
        printf("잘못된 알고리즘 선택.\n"); 
        return 1; 
    }

    char outFilename[100];
    snprintf(outFilename, sizeof(outFilename), "%s_%s_results.txt",
             algoNames[algoChoice-1], fileStems[fileChoice-1]);

    FILE* outFp = fopen(outFilename, "w");
    if (!outFp) { 
        printf("결과 파일을 열 수 없습니다.\n");
        return 1; 
        }
    fprintf(outFp, "# frameSize hitRate(%%)\n");

    for (int frameSize = 1; frameSize <= PAGESET_SIZE; frameSize++) {
        int hit = 0, fault = 0;
        switch (algoChoice) {
            case 1: 
                simulateLRU_counts(filenames[fileChoice-1], frameSize, &hit, &fault); 
                break;
            case 2: 
                simulateFIFO_counts(filenames[fileChoice-1], frameSize, &hit, &fault); 
                break;
            case 3: 
                simulateClock_counts(filenames[fileChoice-1], frameSize, &hit, &fault); 
                break;
            case 4: 
                simulateMRU_counts(filenames[fileChoice-1], frameSize, &hit, &fault); 
                break;
        }
        double rate = (hit + fault) ? (double)hit / (hit + fault) * 100.0 : 0.0;
        fprintf(outFp, "%d %.2f\n", frameSize, rate);
    }

    fclose(outFp);
    printf("결과가 '%s'에 저장되었습니다.\n", outFilename);
    return 0;
}