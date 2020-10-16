package com.xiaoju.uemc.tinyid.base.generator.impl;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhangbingbing
 * @date 2020/10/16
 * @see SnowflakeIdGenerator 测试用例
 */
public class SnowflakeIdGeneratorTest {

    private final SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1);
    private final SnowflakeIdGenerator idGenerator2 = new SnowflakeIdGenerator(2);

    @Test
    public void testNextId() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(2);
        final int size = 5000;
        final Set<Long> idSet = new HashSet<>(size);
        final Set<Long> idSet2 = new HashSet<>(size);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < size; i++) {
                    idSet.add(idGenerator.nextId());
                }
                latch.countDown();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < size; i++) {
                    idSet2.add(idGenerator2.nextId());
                }
                latch.countDown();
            }
        }).start();
        latch.await();
        Assert.assertEquals(size, idSet.size());
        Assert.assertEquals(size, idSet2.size());
        idSet.removeAll(idSet2);
        // idSet idSet2没有重复的id
        Assert.assertEquals(size, idSet.size());
    }

    @Test
    public void testNextIds() {
        Set<Long> idSet = new HashSet<>(5000);
        for (int i = 0; i < 5; i++) {
            idSet.addAll(idGenerator.nextId(1000));
        }
        Assert.assertEquals(5000, idSet.size());
    }

}