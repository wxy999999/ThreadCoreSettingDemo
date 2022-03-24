package com.czb.test;

import java.util.concurrent.*;

/**
 * 类功能：
 *
 * @auther wangxy
 * @date 2022/3/23 17:43
 */
public class CpuConcurrentService {
    private final int concurrentCount = 10000;
    private final CountDownLatch countDownLatch200 = new CountDownLatch(concurrentCount);
    private final CountDownLatch countDownLatch25 = new CountDownLatch(concurrentCount);

    private final ExecutorService executorService200 = new ThreadPoolExecutor(200,
            200, 60L, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(10000));
    private final ExecutorService executorService25 = new ThreadPoolExecutor(25,
            25, 60L, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(10000));


    public boolean cpuConcurrent() {
        try {
            long duration200 = execCpu(countDownLatch200, executorService200);
            long duration25 = execCpu(countDownLatch25, executorService25);
            System.out.println(String.format("duration200:%S, duration25:%s", duration200, duration25));
            return duration25 <= duration200;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean ioConcurrent() {
        try {
            long duration200 = execIo(countDownLatch200, executorService200);
            long duration25 = execIo(countDownLatch25, executorService25);
            System.out.println(String.format("duration200:%S, duration25:%s", duration200, duration25));
            return duration25 <= duration200;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private long execCpu(CountDownLatch countDownLatch, ExecutorService executorService) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < concurrentCount; i++) {
            executorService.execute(new CpuSync(countDownLatch));
        }
        countDownLatch.await();
        return System.currentTimeMillis() - startTime;
    }

    static class CpuSync implements Runnable {

        private final CountDownLatch countDownLatch;

        public CpuSync(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }
        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        public void run() {
            double value = 1;
            for (int i = 2; i < 100; i++) {
                value = value * Math.random();
            }
            countDownLatch.countDown();
        }
    }

    private long execIo(CountDownLatch countDownLatch, ExecutorService executorService) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < concurrentCount; i++) {
            executorService.execute(new IoSync(countDownLatch));
        }
        countDownLatch.await();
        return System.currentTimeMillis() - startTime;
    }

    static class IoSync implements Runnable {

        private final CountDownLatch countDownLatch;

        public IoSync(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }
        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        public void run() {
            try {
                Thread.sleep(2L);
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
