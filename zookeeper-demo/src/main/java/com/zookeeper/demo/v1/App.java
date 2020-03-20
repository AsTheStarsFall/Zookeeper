package com.zookeeper.demo.v1;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

        try {

            ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181," +
                    "127.0.0.1:2182,127.0.0.1:2183", 4000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("--");
                    if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {

                    }
                }
            });
            System.out.println(zooKeeper.getState());
            Thread.sleep(1000);
            System.out.println(zooKeeper.getState());

            System.in.read();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
