package com.xiaoju.uemc.tinyid.server.service.impl;

import com.xiaoju.uemc.tinyid.server.service.SnowflakeWorkIdService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhangbingbing
 * @date 2020/10/16
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SnowflakeWorkerIdServiceImplTest {

    @Autowired
    private SnowflakeWorkIdService snowflakeWorkIdService;

    @Test
    public void testWorkId() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(5);
        final Set<Integer> workIds = new HashSet<>(5);
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        workIds.add(snowflakeWorkIdService.workId());
                    } finally {
                        latch.countDown();
                    }
                }
            }).start();
        }
        latch.await();
        Assert.assertEquals(1, workIds.size());
    }
}