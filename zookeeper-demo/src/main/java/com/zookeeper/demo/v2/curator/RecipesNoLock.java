package com.zookeeper.demo.v2.curator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * {@link}
 *
 * @Desc: 没有实现分布式锁
 * @Author: thy
 * @CreateTime: 2020/1/12 5:32
 **/
public class RecipesNoLock {

    public static void main(String[] args) {
        final CountDownLatch cdl = new CountDownLatch(1);
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        cdl.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
                    String orderNo = sdf.format(new Date());
                    System.err.println("生成的订单号："+ orderNo);
                }
            }).start();
        }

        cdl.countDown();
    }

    //输出，同一时刻的订单号都是重复的

    /**
     * 生成的订单号：05:42:46|214
     * 生成的订单号：05:42:46|214
     * 生成的订单号：05:42:46|214
     * 生成的订单号：05:42:46|214
     * 生成的订单号：05:42:46|214
     * 生成的订单号：05:42:46|214
     * 生成的订单号：05:42:46|214
     * 生成的订单号：05:42:46|214
     * 生成的订单号：05:42:46|214
     * 生成的订单号：05:42:46|214
     */

}
