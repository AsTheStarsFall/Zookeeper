package com.zookeeper.demo.rmi.rpc.zk.loadBalance;

import java.util.List;
import java.util.Random;

/**
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/3/3
 **/
public class RandomLoadBalance extends AbstractLoadBalance {
    @Override
    protected String doSelect(List<String> repos) {
        int len = repos.size();
        Random random = new Random();
        return repos.get(random.nextInt(len));
    }
}
