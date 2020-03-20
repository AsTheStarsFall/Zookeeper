package com.zookeeper.demo.v2.curator;

import com.zookeeper.demo.v2.ZKConf;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2020/1/12 6:06
 **/
public class RecipesMasterSelectA {

    static String masterPath = "/curator_recipes_master_path";

    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString(ZKConf.CONNECT_STR)
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();

    public static void main(String[] args) throws InterruptedException {
        client.start();
        //选举
        LeaderSelector selector = new LeaderSelector(client, masterPath, new LeaderSelectorListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                System.out.println("clientA state:" + newState.isConnected());
            }

            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                System.out.println("clientA 成为master");
                Thread.sleep(3000);
                System.out.println("clientA 完成master操作，释放master");
            }
        });

        selector.autoRequeue();
        selector.start();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
