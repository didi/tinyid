package com.xiaoju.uemc.tinyid.client;

import com.xiaoju.uemc.tinyid.client.config.TinyIdClientConfig;
import com.xiaoju.uemc.tinyid.client.utils.TinyId;
import org.junit.Test;

import java.util.List;

/**
 * @Author du_imba
 */

public class ClientTest {

    @Test
    public void testNextId() {
        TinyIdClientConfig clientConfig = TinyIdClientConfig.getInstance();
        clientConfig.setTinyIdServer("localhost:9999");
        clientConfig.setTinyIdToken("0f673adf80504e2eaa552f5d791b644c");
        TinyId.init(clientConfig);

        Long id = TinyId.nextId("test");
        System.out.println("current id is: " + id);

        List<Long> test = TinyId.nextId("test", 10);
        System.err.println(test);

    }
}
