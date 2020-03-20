package com.zookeeper.demo.v2.curator;

import java.util.concurrent.*;

/**
 * {@link}
 *
 * @Desc: 多线程下的栅栏
 * @Author: thy
 * @CreateTime: 2020/1/12 7:12
 **/
public class RecipesCyclicBarrier {

    public static CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(3);
        es.submit(new Thread(new Runner("1号")));
        es.submit(new Thread(new Runner("2号")));
        es.submit(new Thread(new Runner("3号")));
        es.shutdown();
    }

    static class Runner implements Runnable {
        private String name;

        public Runner(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println(name + "准备好了");
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(name + "跑了");
        }
    }
}
