package com.zookeeper.demo.v2;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2020/1/9 13:17
 **/
public class ZookeeperConnectionSample {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);
//    private static String CONNECT_STR = "127.0.0.1:2181";
    public static String CONNECT_STR="127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";


    public static void main(String[] args) throws IOException {
        /**
         * connectString：服务器列表
         * sessionTimeout：会话超时时间
         * watcher
         * canBeReadOnly：表示当前会话是否支持read-only
         * sessionId/sessionPasswd,可以实现客户端会话复用
         */
        ZooKeeper zooKeeper = new ZooKeeper(CONNECT_STR, 5000, new Watcher() {
            //当zk服务器向客户端发送一个Watch事件通知时，客户端调用process回调
            @Override
            public void process(WatchedEvent event) {
                if (Event.KeeperState.SyncConnected == event.getState()) {
                    countDownLatch.countDown();
                }
            }
        });
        System.out.println("zk state： " + zooKeeper.getState());
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long sessionId = zooKeeper.getSessionId();
        byte[] sessionPasswd = zooKeeper.getSessionPasswd();
    }
}
