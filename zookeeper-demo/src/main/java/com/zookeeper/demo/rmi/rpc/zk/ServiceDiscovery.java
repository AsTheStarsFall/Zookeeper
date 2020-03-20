package com.zookeeper.demo.rmi.rpc.zk;

/**
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/3/3
 **/
public interface ServiceDiscovery {
    /**
    * @Description: 根据请求的服务地址，获取对应的调用地址
    * @Param: [serviceName]
    * @return: java.lang.String
    * @Author: thy
    * @throws:
    * @Date: 2019/3/3
    */
    String discover(String serviceName);

}
