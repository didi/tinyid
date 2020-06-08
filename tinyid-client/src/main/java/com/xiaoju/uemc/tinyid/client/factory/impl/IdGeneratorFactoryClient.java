package com.xiaoju.uemc.tinyid.client.factory.impl;

import com.xiaoju.uemc.tinyid.base.factory.AbstractIdGeneratorFactory;
import com.xiaoju.uemc.tinyid.base.generator.IdGenerator;
import com.xiaoju.uemc.tinyid.base.generator.impl.CachedIdGenerator;
import com.xiaoju.uemc.tinyid.base.service.SegmentIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * @author du_imba
 */
@Component
public class IdGeneratorFactoryClient extends AbstractIdGeneratorFactory {

    @Autowired
    private SegmentIdService segmentIdService;

    @Override
    protected IdGenerator createIdGenerator(String bizType) {
        return new CachedIdGenerator(bizType, segmentIdService);
    }

}
