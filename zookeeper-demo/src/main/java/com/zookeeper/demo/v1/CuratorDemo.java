package com.zookeeper.demo.v1;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * @Desc:对数据节点操作的封装
 * @Author: thy
 * @CreateTime: 2018/11/29
 **/
public class CuratorDemo {
    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181," +
                        "127.0.0.1:2182,127.0.0.1:2183").sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace("curator").build();

        curatorFramework.start();

        //原生api:逐级创建，父节点存在，子节点才能创建
        curatorFramework.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT).forPath("/thy/node1","1".getBytes());

        Stat stat=new Stat();
        //获取节点内容
        byte[] bytes = curatorFramework.getData().storingStatIn(stat).forPath("/thy/node1");
        System.out.println(new String(bytes));
        //修改节点内容
        curatorFramework.setData().withVersion(stat.getVersion()).forPath("/thy/node1","xx".getBytes());
        curatorFramework.close();

    }

}
