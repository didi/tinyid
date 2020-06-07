package com.xiaoju.uemc.tinyid.client;

import com.xiaoju.uemc.tinyid.client.handler.IDGeneratorService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @Author du_imba
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:application.properties")
@ComponentScan("com.xiaoju.uemc.tinyid.client")
@SpringBootConfiguration
public class ClientTest {

    @Autowired
    private IDGeneratorService client;

    @Test
    public void testNextId() {
        for (int i = 0; i < 10; i++) {
            System.err.println("current id is: " + client.nextIntID("test"));
        }
    }

}
