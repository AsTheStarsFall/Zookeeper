package com.zookeeper.demo.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/3/3
 **/
public interface HelloService extends Remote {
    String sayHello(String msg) throws RemoteException;
}
