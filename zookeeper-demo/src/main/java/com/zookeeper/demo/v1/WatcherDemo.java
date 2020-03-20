package com.zookeeper.demo.v1;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Desc:事件机制
 * @Author: thy
 * @CreateTime: 2018/11/29
 **/
public class WatcherDemo {

    static int i = 0;

    public static void main(String[] args) {

        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);

            ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181," +
                    "127.0.0.1:2182,127.0.0.1:2183", 4000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("默认事件：" + watchedEvent.getType());
                    if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                        countDownLatch.countDown(); //这个地方通知线程，连接成功了，释放锁
                    }
                }
            });
            countDownLatch.await(); //阻塞，直到连接成功 --> countDownLatch.countDown()
            System.out.println(zooKeeper.getState());

            //创建节点
            zooKeeper.create("/zk-persis-thy", "2".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);


            //注册监听事件 getData,Exists,getChildren
            Stat stat = zooKeeper.exists("/zk-persis-thy", new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    System.out.println(event.getType() + "-->" + event.getPath());// 当前节点的当前 Watcher

                    //当数据发生变化会通知客户端，但客户端只收到一次，所以要再次绑定
                    try {
                        zooKeeper.exists(event.getPath(), true);// true 默认的 Watcher
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });

            //通过修改操作来触发监听事件
            Stat stat1 = zooKeeper.setData("/zk-persis-thy", "3".getBytes(), stat.getVersion());

            Thread.sleep(1000);

            //通过删除操作来触发监听事件
            zooKeeper.delete("/zk-persis-thy", stat1.getVersion());

            System.in.read();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }


}
