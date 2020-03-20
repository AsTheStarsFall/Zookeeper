package com.zookeeper.demo.v1;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;


/**
 * @Desc:Curator的事件机制
 * @Author: thy
 * @CreateTime: 2018/11/29
 **/
public class CuratorWatcherDemo {

    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181," +
                        "127.0.0.1:2182,127.0.0.1:2183").sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace("curator").build();

        curatorFramework.start();

//        addListenerWithPathChildCache(curatorFramework,"/thy");
//        addListenerWithNodeCache(curatorFramework,"/thy");
        addListenerWithTreeCache(curatorFramework,"/thy");
        System.in.read();


    }

    /**
     * PathChildCache 监听一个节点下子节点的创建、删除、更新
     * NodeCache  监听一个节点的更新和创建事件
     * TreeCache  综合PatchChildCache和NodeCache的特性
     */

    public static void addListenerWithPathChildCache(CuratorFramework curatorFramework,String path) throws Exception {
        PathChildrenCache pcc=new PathChildrenCache(curatorFramework,path,true);
        PathChildrenCacheListener pccl=new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                System.out.println("receive event: "+ pathChildrenCacheEvent.getType());
            }
        };

        pcc.getListenable().addListener(pccl);
        pcc.start(PathChildrenCache.StartMode.NORMAL);

    }

    public static void addListenerWithNodeCache(CuratorFramework curatorFramework,String path) throws Exception {
       final NodeCache nc=new NodeCache(curatorFramework,path,false);

        NodeCacheListener ncl=new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("receive event: "+ nc.getCurrentData().getPath());
            }
        };
        nc.getListenable().addListener(ncl);
        nc.start();

    }

    public static void addListenerWithTreeCache(CuratorFramework curatorFramework,String path) throws Exception {
        TreeCache tc=new TreeCache(curatorFramework,path);

        TreeCacheListener tcl=new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                System.out.println("receive event: "+ event.getType());
            }
        };
        tc.getListenable().addListener(tcl);
        tc.start();
    }


}
