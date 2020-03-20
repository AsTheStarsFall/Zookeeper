package com.zookeeper.demo.rmi.rpc;

import com.zookeeper.demo.rmi.rpc.zk.*;

/**
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/3/3
 **/
public class ClientDemo {

    public static void main(String[] args) throws InterruptedException {
        ServiceDiscovery serviceDiscovery = new
                ServiceDiscoveryImpl(ZkConfig.CONNNECTION_STR);

        RpcClientProxy rpcClientProxy = new RpcClientProxy(serviceDiscovery);

        for (int i = 0; i < 10; i++) {
            SayHello hello = rpcClientProxy.clientProxy(SayHello.class, null);
            System.out.println(hello.sayHello("thy"));
            Thread.sleep(1000);
        }

    }
}
