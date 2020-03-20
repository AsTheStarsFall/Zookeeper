package com.zookeeper.demo.v2.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * {@link}
 *
 * @Desc: 监听器
 * @Author: thy
 * @CreateTime: 2020/1/11 2:09
 **/
public class WatcherSample {

    private static String PATH = "/zk/nodecache";

    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("127.0.0.1:2181")
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .sessionTimeoutMs(5000)
            .build();

    public static void main(String[] args) throws Exception {
        client.start();
        //叶子节点的监听
//        laafWatch(client);
        //子节点的监听
        pathChildrenWatch(client);

    }

    //子节点监控
    private static void pathChildrenWatch(CuratorFramework client) throws Exception {
        PathChildrenCache cache = new PathChildrenCache(client, PATH, true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                switch (event.getType()) {
                    case CHILD_ADDED:
                        System.out.println("CHILD ADDED :" + event.getData().getPath());
                        break;
                    case CHILD_REMOVED:
                        System.out.println("CHILD REMOVED :" + event.getData().getPath());
                        break;
                    case CHILD_UPDATED:
                        System.out.println("CHILD UPDATED :" + event.getData().getPath());
                        break;
                }
            }
        });
        client.create().withMode(CreateMode.PERSISTENT).forPath(PATH);
        Thread.sleep(1000);
        client.create().withMode(CreateMode.PERSISTENT).forPath(PATH + "/c1");
        Thread.sleep(1000);

        client.setData().forPath(PATH + "/c1", "update".getBytes());
        Thread.sleep(1000);

        client.delete().forPath(PATH + "/c1");
        Thread.sleep(1000);

    }

    //叶子节点监控
    private static void laafWatch(CuratorFramework client) throws Exception {
        //Cache是对事件监听的包装，可以看作是本地缓存视图与zk视图的对比过程
        final NodeCache cache = new NodeCache(client, PATH, false);
        //如果为true，cache在第一次启动的时候，就会从zk读取对应节点的数据内容
        cache.start(true);
        cache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("Node data updated! new Data:" +
                        new String(cache.getCurrentData().getData()));
            }
        });
        client.setData().forPath(PATH, "u".getBytes());
        Thread.sleep(Integer.MAX_VALUE);
    }

}
