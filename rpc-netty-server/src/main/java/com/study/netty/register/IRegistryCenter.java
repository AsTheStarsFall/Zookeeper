package com.study.netty.register;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2020/1/11 4:26
 **/
public interface IRegistryCenter {
    void regist(String serviceName, String serviceAddress) throws Exception;

}
