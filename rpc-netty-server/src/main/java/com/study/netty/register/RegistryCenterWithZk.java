package com.study.netty.register;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2020/1/11 4:26
 **/
public class RegistryCenterWithZk implements IRegistryCenter {
    CuratorFramework client = null;

    {
        client = CuratorFrameworkFactory.builder().
                connectString(ZKConfig.CONNECTION_STR).sessionTimeoutMs(5000).
                retryPolicy(new ExponentialBackoffRetry(1000, 3)).
                namespace("registry")
                .build();
        client.start();
    }

    @Override
    public void regist(String serviceName, String serviceAddress) throws Exception {
        String servicePath = "/" + serviceName;

        //检查服务节点是否存在
        if (client.checkExists().forPath(servicePath) == null) {
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(servicePath);
        }

        client.create().withMode(CreateMode.PERSISTENT).forPath(servicePath + "/" + serviceAddress);
        System.out.println("服务注册成功！");
    }
}
