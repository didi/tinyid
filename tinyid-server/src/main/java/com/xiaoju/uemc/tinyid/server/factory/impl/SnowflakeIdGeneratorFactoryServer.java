package com.xiaoju.uemc.tinyid.server.factory.impl;

import com.xiaoju.uemc.tinyid.base.factory.AbstractIdGeneratorFactory;
import com.xiaoju.uemc.tinyid.base.generator.IdGenerator;
import com.xiaoju.uemc.tinyid.base.generator.impl.SnowflakeIdGenerator;
import com.xiaoju.uemc.tinyid.server.common.annotation.Module;
import com.xiaoju.uemc.tinyid.server.service.SnowflakeWorkIdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangbingbing
 * @date 2020/10/16
 */
@Component
@Module("snowflake.enable")
public class SnowflakeIdGeneratorFactoryServer extends AbstractIdGeneratorFactory {

    private static final Logger logger = LoggerFactory.getLogger(SnowflakeIdGeneratorFactoryServer.class);

    @Autowired
    private SnowflakeWorkIdService workIdService;

    @Override
    public IdGenerator createIdGenerator(String bizType) {
        logger.info("SnowflakeIdGenerator :{}", bizType);
        return new SnowflakeIdGenerator(workIdService.workId());
    }
}
