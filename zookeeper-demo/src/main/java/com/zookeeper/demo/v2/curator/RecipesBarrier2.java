package com.zookeeper.demo.v2.curator;

import com.zookeeper.demo.v2.ZKConf;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2020/1/12 7:32
 **/
public class RecipesBarrier2 {
    static String barrier_path = "/curator_recipes_barrier_path";
//    static DistributedBarrier barrier;

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        CuratorFramework client = CuratorFrameworkFactory.builder()
                                .connectString(ZKConf.CONNECT_STR)
                                .sessionTimeoutMs(5000)
                                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                                .build();
                        client.start();
                        DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client, barrier_path, 5);
                        Thread.sleep(Math.round(Math.random() * 3000));
                        System.out.println(Thread.currentThread().getName() + "线程来到了barrier处");
                        barrier.enter();
                        System.out.println("启动");
                        Thread.sleep(Math.round(Math.random() * 3000));
                        barrier.leave();
                        System.out.println("退出");
                    } catch (Exception e) {
                    }
                }
            }).start();
        }
    }
}
