package com.czb.test;

/**
 * 类功能：
 *
 * @auther wangxy
 * @date 2022/3/23 17:39
 */
public class TestMain {
    public static void main(String[] args) {
        int cycleTimes = 100;  // 循环次数

//        cpuCalc(cycleTimes);

        ioCalc(cycleTimes);
    }

    private static void cpuCalc(int cycleTimes) {
        int countThread25 = 0;
        for (int i = 0; i < cycleTimes; i++) {
            if (new CpuConcurrentService().cpuConcurrent()) countThread25++;
        }
        System.out.println(String.format("CPU密集型场景下，25线程更快的次数是:%s", countThread25));
    }

    private static void ioCalc(int cycleTimes) {
        int countThread25 = 0;
        for (int i = 0; i < cycleTimes; i++) {
            if (new CpuConcurrentService().ioConcurrent()) countThread25++;
        }
        System.out.println(String.format("IO密集型场景下，25线程更快的次数是:%s", countThread25));
    }
}
