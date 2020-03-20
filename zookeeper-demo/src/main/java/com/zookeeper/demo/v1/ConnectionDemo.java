package com.zookeeper.demo.v1;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Desc:
 * @Author: thy
 * @CreateTime: 2018/11/29
 **/
public class ConnectionDemo {

    public static void main(String[] args) {
        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181," +
                    "127.0.0.1:2182,127.0.0.1:2183", 4000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                        countDownLatch.countDown(); //这个地方通知线程，连接成功了，释放锁,所以这个方法使得连接必定成功
                    }
                }
            });
            countDownLatch.await(); //阻塞，直到连接成功 --> countDownLatch.countDown()
            System.out.println(zooKeeper.getState());

            //创建节点
            //     zooKeeper.create("/zk-persis-thy","0".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            Thread.sleep(1000);
            Stat stat = new Stat();

            //获取当前节点值
            byte[] zooKeeperData = zooKeeper.getData("/zk-persis-thy", null, stat);
            System.out.println(new String(zooKeeperData));

            //修改节点值
            zooKeeper.setData("/zk-persis-thy", "1".getBytes(), stat.getVersion());

            //获取当前节点值
            byte[] zooKeeperData1 = zooKeeper.getData("/zk-persis-thy", null, stat);
            System.out.println(new String(zooKeeperData1));
//
//            //删除当前节点值
//            zooKeeper.delete("/zk-persis-thy",stat.getVersion());
            zooKeeper.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {

        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

}
