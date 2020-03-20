package com.zookeeper.demo;

import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.zookeeper.demo.distirbuteLock.DistirbutedLock;
import jdk.nashorn.internal.parser.JSONParser;
import org.junit.Test;

import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws JsonProcessingException {
        assertTrue( true );
        SortedSet<Integer> sortedSet=new TreeSet<>();
        for (int i = 0; i < 10; i++) {
            sortedSet.add(i);
        }
        SortedSet<Integer> integers = sortedSet.headSet(5);
        System.out.println(new ObjectMapper().writeValueAsString(integers));
        System.out.println(integers.last());
        System.out.println(integers.first());
        sortedSet.clear();
    }

    public static void main(String[] args) throws IOException {
        CountDownLatch countDownLatch=new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                try {
                    countDownLatch.await();
                    DistirbutedLock distirbutedLock=new DistirbutedLock();
                    distirbutedLock.lock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },"Thread-"+i).start();
            countDownLatch.countDown();
        }
        System.in.read();
    }
}
