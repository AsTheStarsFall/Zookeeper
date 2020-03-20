package com.zookeeper.demo.rmi;

import java.net.MalformedURLException;
import java.rmi.*;

/**
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/3/3
 **/
public class ClientDemo {

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        HelloService helloService=
                (HelloService) Naming.lookup("rmi://127.0.0.1/Hello");
        // HelloServiceImpl实例(HelloServiceImpl_stub)
        // RegistryImpl_stub
        System.out.println(helloService.sayHello("Thy"));

    }
}
