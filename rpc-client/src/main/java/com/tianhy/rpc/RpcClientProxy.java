package com.tianhy.rpc;

import com.tianhy.rpc.discovery.IServiceDiscovery;
import com.tianhy.rpc.discovery.ServiceDiscoveryWithZk;

import java.lang.reflect.Proxy;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/6/19
 **/
public class RpcClientProxy {
    private IServiceDiscovery discovery = new ServiceDiscoveryWithZk();

    public <T> T clientProxy(final Class<?> interfaceClazz, String version) {

        return (T) Proxy.newProxyInstance(interfaceClazz.getClassLoader(),
                new Class[]{interfaceClazz}, new RemoteInvocationHandler(discovery, version));

//        Class<?> clazz = interfaceClazz.getClass();
//        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new RemoteInvocationHandler(host,port));

    }


}
