package com.zookeeper.demo.v2.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * {@link}
 *
 * @Desc: Curator 创建session会话
 * @Author: thy
 * @CreateTime: 2020/1/10 23:39
 **/
public class CreateSession {
    public static void main(String[] args) {
        /**
         * baseSleepTimeMs:初始sleep时间
         * maxRetries：最大重试次数
         */
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        CuratorFramework client=
                CuratorFrameworkFactory.newClient("127.0.0.1:2181",
                        5000,3000,
                        retryPolicy);

        client.start();

    }
}


