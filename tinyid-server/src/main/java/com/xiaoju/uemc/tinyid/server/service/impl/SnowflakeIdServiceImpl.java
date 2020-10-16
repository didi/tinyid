package com.xiaoju.uemc.tinyid.server.service.impl;

import com.xiaoju.uemc.tinyid.server.common.annotation.Module;
import com.xiaoju.uemc.tinyid.server.factory.impl.SnowflakeIdGeneratorFactoryServer;
import com.xiaoju.uemc.tinyid.server.service.SnowflakeIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangbingbing
 * @date 2020/10/16
 */
@Service
@Module(value = "snowflake.enable")
public class SnowflakeIdServiceImpl implements SnowflakeIdService {

    @Autowired
    private SnowflakeIdGeneratorFactoryServer factoryServer;

    @Override
    public Long nextId(String businessType) {
        return factoryServer.getIdGenerator(businessType).nextId();
    }

    @Override
    public List<Long> nextIdBatch(String businessType, int batchSize) {
        return factoryServer.getIdGenerator(businessType).nextId(batchSize);
    }
}
