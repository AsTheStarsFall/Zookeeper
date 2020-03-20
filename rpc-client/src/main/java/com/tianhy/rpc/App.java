package com.tianhy.rpc;

import com.tianhy.service.IHelloService;
import com.tianhy.service.IPaymentService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
//        RpcClientProxy proxy = new RpcClientProxy();
//        IHelloService helloService = proxy.clientProxy(IHelloService.class,"127.0.0.1",8888);
//        String result = helloService.sayHello("hello");
//        System.out.println(result);

        //spring的方式
/*        ApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfig.class);
        RpcClientProxy rpcClientProxy = ac.getBean(RpcClientProxy.class);
        IHelloService helloService = rpcClientProxy.clientProxy(IHelloService.class, "127.0.0.1", 8080);
        String result = helloService.sayHello("i am super man");
        System.out.println(result);*/
//        IPaymentService paymentService = rpcClientProxy.clientProxy(IPaymentService.class, "localhost", 8888);
//        paymentService.doPay();

        //加入ZK以后
        ApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfig.class);
        RpcClientProxy rpcClientProxy = ac.getBean(RpcClientProxy.class);
        IHelloService helloService = rpcClientProxy.clientProxy(IHelloService.class, "v1.0");
        String result = helloService.sayHello("i am super man");
        System.out.println(result);

    }
}
