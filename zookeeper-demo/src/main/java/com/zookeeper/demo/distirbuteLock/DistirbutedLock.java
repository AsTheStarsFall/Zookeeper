package com.zookeeper.demo.distirbuteLock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @Desc:扩展Lock实现分布式锁
 * @Author: thy
 * @CreateTime: 2018/12/5
 **/
public class DistirbutedLock implements Lock, Watcher {

    private ZooKeeper zk = null;
    private String ROOT_LOCK = "/locks"; //根节点
    private String PREV_LOCK; //前一个节点
    private String CURRENT_LOCK;//当前节点

    private CountDownLatch countDownLatch;

    public DistirbutedLock() {

        try {
            zk = new ZooKeeper("127.0.0.1:2181", 4000, this);
            //判断根节点是否存在
            Stat stat = zk.exists(ROOT_LOCK, false);
            if (stat == null) {
                //如果不存在创建一个
                zk.create(ROOT_LOCK, "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void lock() {
        if (this.tryLock()) {
            System.out.println(Thread.currentThread().getName() + "->" + CURRENT_LOCK + "获得锁成功");
            return;
        }
        //阻塞
        waitForLock(PREV_LOCK);
    }

    private boolean waitForLock(String prev) {
        try {
            //监听上一个节点
            Stat stat = zk.exists(prev, true);
            if (stat != null) {
                System.out.println(Thread.currentThread().getName() + "->等待锁" + prev + "释放");
                countDownLatch = new CountDownLatch(1);
                countDownLatch.await();
                System.out.println(Thread.currentThread().getName() + "->获得锁成功");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }


    @Override
    public boolean tryLock() {
        //竞争锁
        try {
            //创建节点
            CURRENT_LOCK = zk.create(ROOT_LOCK + "/", "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(Thread.currentThread().getName() + "->" + CURRENT_LOCK + "尝试竞争锁");

            //获取根节点下所有子节点
            List<String> children = zk.getChildren(ROOT_LOCK, false);
            //排序
            SortedSet<String> sortedSet = new TreeSet<>();
            for (String child : children) {
                sortedSet.add(ROOT_LOCK + "/" + child);
            }
            //获取最小节点
            String first = sortedSet.first();
            SortedSet<String> headSet = sortedSet.headSet(CURRENT_LOCK);//比当前节点小的节点
            //如果是最小的节点，则获取锁
            if (CURRENT_LOCK.equals(first)) {
                return true;
            }
            //获取比当前小的最后一个节点，把锁给它PREV_LOCK
            if (!headSet.isEmpty()) {
                PREV_LOCK = headSet.last();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        //释放锁
        System.out.println(Thread.currentThread().getName() + "->释放锁" + CURRENT_LOCK);
        try {
            zk.delete(CURRENT_LOCK, -1);
            CURRENT_LOCK = null;
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }


    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }
}
