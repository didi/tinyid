package com.xiaoju.uemc.tinyid.client.factory.impl;

import com.xiaoju.uemc.tinyid.base.factory.AbstractIdGeneratorFactory;
import com.xiaoju.uemc.tinyid.base.generator.IdGenerator;
import com.xiaoju.uemc.tinyid.base.generator.impl.CachedIdGenerator;
import com.xiaoju.uemc.tinyid.base.service.SegmentIdService;
import com.xiaoju.uemc.tinyid.client.config.TinyIdClientConfig;
import com.xiaoju.uemc.tinyid.client.service.impl.HttpSegmentIdServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author du_imba
 */
@Component
public class IdGeneratorFactoryClient extends AbstractIdGeneratorFactory {

    private static final Logger logger = Logger.getLogger(IdGeneratorFactoryClient.class.getName());

    @Autowired
    private SegmentIdService segmentIdService;

    @Override
    protected IdGenerator createIdGenerator(String bizType) {
        return new CachedIdGenerator(bizType, segmentIdService);
    }

}
