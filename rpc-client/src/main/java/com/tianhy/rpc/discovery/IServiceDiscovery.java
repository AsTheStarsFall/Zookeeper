package com.tianhy.rpc.discovery;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2020/1/11 7:20
 **/
public interface IServiceDiscovery {
    //根据服务名称返回服务地址
    String discovery(String serviceName);


}
