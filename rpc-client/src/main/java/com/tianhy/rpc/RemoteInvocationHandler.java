package com.tianhy.rpc;

import com.tianhy.request.RpcRequest;
import com.tianhy.rpc.discovery.IServiceDiscovery;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/6/19
 **/
public class RemoteInvocationHandler implements InvocationHandler {
    private IServiceDiscovery serviceDiscovery;
    private String version;

    public RemoteInvocationHandler(IServiceDiscovery discovery, String version) {
        this.serviceDiscovery = discovery;
        this.version = version;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //构建请求体
        RpcRequest rpcRequest = new RpcRequest();
        //类名
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        //方法名
        rpcRequest.setMethodName(method.getName());
        //参数
        rpcRequest.setParameters(args);
        //版本
        rpcRequest.setVersion(version);

        String serviceName = rpcRequest.getClassName();
        if (!StringUtils.isEmpty(version)) {
            serviceName = serviceName + "-" + version;
        }
        //服务发现，获取到服务地址
        String discovery = serviceDiscovery.discovery(serviceName);

        //远程通信
        RpcNetTransport rpcNetTransport = new RpcNetTransport(discovery);
        Object result = rpcNetTransport.send(rpcRequest);
        return result;
    }
}
