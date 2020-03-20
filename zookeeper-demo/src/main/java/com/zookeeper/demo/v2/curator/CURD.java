package com.zookeeper.demo.v2.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * {@link}
 *
 * @Desc: Curator 的CURD操作
 * @Author: thy
 * @CreateTime: 2020/1/10 23:35
 **/
public class CURD {
    //    private static String CONNECT_STR = "127.0.0.1:2181";
    public static String CONNECT_STR = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    private static String PATH = "/zk/test1";

    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString(CONNECT_STR)
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .namespace("registry")
            .build();


    public static void main(String[] args) throws Exception {
        client.start();
        create(client);
//        delete(client);
//        update(client);

    }

    //更细数据
    private static void update(CuratorFramework client) throws Exception {
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath(PATH);
        client.setData().withVersion(stat.getVersion()).forPath(PATH);
        try {
            client.setData().withVersion(stat.getVersion()).forPath(PATH);

        } catch (Exception e) {
            System.out.println("Faile set node data due to :" + e.getMessage());
        }


    }

    //删除节点
    private static void delete(CuratorFramework client) throws Exception {
        Stat stat = new Stat();
        //读取数据
        String value = new String(client.getData().storingStatIn(stat).forPath(PATH));
        System.out.println("读取数据: " + value);
        //
        client.delete()
                //递归删除所有子节点
                .deletingChildrenIfNeeded()
                //指定版本
                .withVersion(stat.getVersion())
                .forPath(PATH);
    }

    //创建节点
    private static void create(CuratorFramework client) throws Exception {
        client.create()
                //创建父节点
                .creatingParentContainersIfNeeded()
                //持久节点
                .withMode(CreateMode.PERSISTENT)
                .forPath(PATH, "init".getBytes());
    }


}
