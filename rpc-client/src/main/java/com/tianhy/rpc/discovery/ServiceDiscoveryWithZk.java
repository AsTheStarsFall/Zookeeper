package com.tianhy.rpc.discovery;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.server.ZKDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link}
 *
 * @Desc: 服务发现
 * @Author: thy
 * @CreateTime: 2020/1/11 7:20
 **/
public class ServiceDiscoveryWithZk implements IServiceDiscovery {

    CuratorFramework client = null;

    //服务地址本地缓存
    List<String> localCache = new ArrayList<>();

    {
        client = CuratorFrameworkFactory.builder()
                .connectString(ZkConfig.CONNECTION_STR)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace("registry")
                .build();
        client.start();
    }

    @Override
    public String discovery(String serviceName) {
        String path = "/" + serviceName;
        if (localCache.isEmpty()) {
            try {
                localCache = client.getChildren().forPath(path);
                //加入watch
                registryWatch(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //对已有的地址做负载均衡
        LoadBlanceStrategy loadBlanceStrategy = new RandomLoadBlance();
        return loadBlanceStrategy.selectHost(localCache);
    }

    private void registryWatch(String path) throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, path, true);
        PathChildrenCacheListener listener = (curatorFramework, event) -> {
            System.out.println("客户端收到节点变化通知");
            localCache = client.getChildren().forPath(path);
        };
        pathChildrenCache.getListenable().addListener(listener);
        pathChildrenCache.start();

    }
}
