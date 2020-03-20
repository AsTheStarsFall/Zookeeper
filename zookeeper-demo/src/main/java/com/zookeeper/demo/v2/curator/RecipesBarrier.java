package com.zookeeper.demo.v2.curator;

import com.zookeeper.demo.v2.ZKConf;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * {@link}
 *
 * @Desc: 分布式下的栅栏
 * @Author: thy
 * @CreateTime: 2020/1/12 7:19
 **/
public class RecipesBarrier {
    static String barrier_path = "/curator_recipes_barrier_path";
    static DistributedBarrier barrier;

    public static void main(String[] args) throws Exception {
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
                        barrier = new DistributedBarrier(client, barrier_path);
                        System.out.println(Thread.currentThread().getName() + "线程来到了barrier处");
                        barrier.setBarrier();
                        barrier.waitOnBarrier();
                        System.out.println("启动");
                    } catch (Exception e) {

                    }
                }
            }).start();
        }
        Thread.sleep(2000);
        barrier.removeBarrier();
    }

}
