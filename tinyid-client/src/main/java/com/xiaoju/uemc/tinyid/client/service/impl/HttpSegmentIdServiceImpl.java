package com.xiaoju.uemc.tinyid.client.service.impl;

import com.xiaoju.uemc.tinyid.base.entity.SegmentId;
import com.xiaoju.uemc.tinyid.base.service.SegmentIdService;
import com.xiaoju.uemc.tinyid.client.config.TinyIdClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * @author du_imba
 */
@Component
public class HttpSegmentIdServiceImpl implements SegmentIdService {
    @Autowired
    private TinyIdClientConfig config;
    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = Logger.getLogger(HttpSegmentIdServiceImpl.class.getName());

    private static HttpHeaders headers = new HttpHeaders();

    @Override
    public SegmentId getNextSegmentId(String bizType) {

        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("token", config.getToken());
        requestBody.add("bizType", bizType);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(config.getCacheServerUrl(), requestEntity, String.class);

        logger.info("tinyId client getNextSegmentId end, response:" + responseEntity);
        if (StringUtils.isEmpty(responseEntity)) {
            return null;
        }

        SegmentId segmentId = new SegmentId();
        String[] arr = responseEntity.getBody().split(",");
        segmentId.setCurrentId(new AtomicLong(Long.parseLong(arr[0])));
        segmentId.setLoadingId(Long.parseLong(arr[1]));
        segmentId.setMaxId(Long.parseLong(arr[2]));
        segmentId.setDelta(Integer.parseInt(arr[3]));
        segmentId.setRemainder(Integer.parseInt(arr[4]));
        return segmentId;
    }

}
