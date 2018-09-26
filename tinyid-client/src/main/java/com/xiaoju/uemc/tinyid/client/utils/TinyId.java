package com.xiaoju.uemc.tinyid.client.utils;

import com.xiaoju.uemc.tinyid.client.factory.impl.IdGeneratorFactoryClient;
import com.xiaoju.uemc.tinyid.base.generator.IdGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author du_imba
 */
public class TinyId {
    private static IdGeneratorFactoryClient client = IdGeneratorFactoryClient.getInstance(null);

    private TinyId() {

    }

    public static Long nextId(String bizType) {
        if(bizType == null) {
            throw new IllegalArgumentException("type is null");
        }
        IdGenerator idGenerator = client.getIdGenerator(bizType);
        return idGenerator.nextId();
    }

    public static List<Long> nextId(String bizType, Integer batchSize) {
        if(batchSize == null) {
            Long id = nextId(bizType);
            List<Long> list = new ArrayList<>();
            list.add(id);
            return list;
         }
        IdGenerator idGenerator = client.getIdGenerator(bizType);
        return idGenerator.nextId(batchSize);
    }

}
