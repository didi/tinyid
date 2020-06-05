package com.xiaoju.uemc.tinyid.client.service.impl;

import com.xiaoju.uemc.tinyid.base.entity.SegmentId;
import com.xiaoju.uemc.tinyid.base.service.SegmentIdService;
import com.xiaoju.uemc.tinyid.client.config.TinyIdClientConfig;
import com.xiaoju.uemc.tinyid.client.config.TinyIdClientConfigProperties;
import com.xiaoju.uemc.tinyid.client.utils.TinyIdHttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * @author du_imba
 */
@Component
@EnableConfigurationProperties(TinyIdClientConfigProperties.class)
public class HttpSegmentIdServiceImpl implements SegmentIdService {
    @Autowired
    private TinyIdClientConfig clientConfig;


    private static final Logger logger = Logger.getLogger(HttpSegmentIdServiceImpl.class.getName());

    @Override
    public SegmentId getNextSegmentId(String bizType) {

        String response = TinyIdHttpUtils.post(clientConfig.getTinyIdServer(), clientConfig.getReadTimeout(), clientConfig.getConnectTimeout());
        logger.info("tinyId client getNextSegmentId end, response:" + response);
        if (response == null || "".equals(response.trim())) {
            return null;
        }
        SegmentId segmentId = new SegmentId();
        String[] arr = response.split(",");
        segmentId.setCurrentId(new AtomicLong(Long.parseLong(arr[0])));
        segmentId.setLoadingId(Long.parseLong(arr[1]));
        segmentId.setMaxId(Long.parseLong(arr[2]));
        segmentId.setDelta(Integer.parseInt(arr[3]));
        segmentId.setRemainder(Integer.parseInt(arr[4]));
        return segmentId;
    }

}
