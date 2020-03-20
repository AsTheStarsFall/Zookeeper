package com.zookeeper.demo.v2.curator;

import com.zookeeper.demo.v2.ZKConf;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

/**
 * {@link}
 *
 * @Desc: 分布式计数器
 * @Author: thy
 * @CreateTime: 2020/1/12 6:58
 **/
public class RecipesDisAtomicInt {
    static String distAtomicIntPath = "/curator_recipes_distatomicint_path";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString(ZKConf.CONNECT_STR)
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();

    public static void main(String[] args) throws Exception {
        client.start();
        //分布式原子递增
        DistributedAtomicInteger atomicInteger = new DistributedAtomicInteger(
                client, distAtomicIntPath, new RetryNTimes(3, 1000)
        );

        AtomicValue<Integer> add = atomicInteger.add(8);
        System.out.println("Result : " + add.succeeded());
        System.out.println(add.preValue());
        System.out.println(add.postValue());

    }
}
