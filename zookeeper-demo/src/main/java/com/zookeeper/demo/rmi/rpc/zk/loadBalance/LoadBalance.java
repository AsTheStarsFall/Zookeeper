package com.zookeeper.demo.rmi.rpc.zk.loadBalance;

import java.util.List;

/**
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/3/3
 **/
public interface LoadBalance {
    String selectHost(List<String> repos);
}
