package com.zookeeper.demo.v2.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * {@link}
 *
 * @Desc: 权限控制
 * @Author: thy
 * @CreateTime: 2020/1/11 1:15
 **/
public class ACL {
    private static String PATH = "/zk-auth-test";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("127.0.0.1:2181")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            //设置权限
            .authorization("digest", "username:admin".getBytes())
            .build();

    public static void main(String[] args) throws Exception {
        client.start();
        create(client);
        //connect with no acl
        createWithNoAcl();
    }

    private static void createWithNoAcl() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();

        client.start();
        try {
            client.create()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(PATH, "aaa".getBytes());
        } catch (Exception e) {
            System.out.println("Faile create node due to :" + e.getMessage());
        }
        Thread.sleep(Integer.MAX_VALUE);
    }

    private static void create(CuratorFramework client) throws InterruptedException {
        try {
            client.create()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(PATH, "auth-test".getBytes());
        } catch (Exception e) {
            System.out.println("Faile create node due to :" + e.getMessage());
        }
//        Thread.sleep(Integer.MAX_VALUE);

    }


}
