package com.zookeeper.demo.v2.curator;

import com.zookeeper.demo.v2.ZKConf;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;


/**
 * {@link}
 *
 * @Desc: 使用Curator 实现分布式锁
 * @Author: thy
 * @CreateTime: 2020/1/12 5:45
 **/
public class RecipesLock {
    static String lockPath = "/curator_recipes_lock_path";

    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString(ZKConf.CONNECT_STR)
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();

    public static void main(String[] args) throws InterruptedException {
        client.start();
        //分布式锁
        final InterProcessMutex lock = new InterProcessMutex(client, lockPath);
        final CountDownLatch cdl = new CountDownLatch(1);
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        cdl.await();
                        lock.acquire(); //竞争锁
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
                    String orderNo = sdf.format(new Date());
                    System.err.println("生成的订单号："+ orderNo);
                    try {
                        Thread.sleep(Integer.MAX_VALUE);
                        //释放锁
                        lock.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        cdl.countDown();
    }

}
