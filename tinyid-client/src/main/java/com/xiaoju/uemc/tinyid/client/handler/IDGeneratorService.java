package com.xiaoju.uemc.tinyid.client.handler;

import com.xiaoju.uemc.tinyid.base.generator.IdGenerator;
import com.xiaoju.uemc.tinyid.client.config.TinyIdClientConfig;
import com.xiaoju.uemc.tinyid.client.factory.impl.IdGeneratorFactoryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class IDGeneratorService {
    @Autowired
    private IdGeneratorFactoryClient client;
    @Autowired
    private TinyIdClientConfig config;
    @Autowired
    private RestTemplate restTemplate;

    private static HttpHeaders headers = new HttpHeaders();

    public List<Long> nextID(String bizType, int batchSize) {
        if (config.isLocalCache()) {
            IdGenerator idGenerator = client.getIdGenerator(bizType);
            List<Long> idList = idGenerator.nextId(batchSize);
            return idList;
        } else {
            MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("token", config.getToken());
            requestBody.add("bizType", bizType);
            requestBody.add("batchSize", batchSize);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(config.getNoCacheServerUrl(), requestEntity, String.class);

            if (StringUtils.isEmpty(responseEntity.getBody())) {
                throw new RuntimeException("获取ID失败");
            }
            List<Long> list = Arrays.asList(responseEntity.getBody().split(",")).stream().mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
            return list;
        }
    }

    public Long nextID(String bizType) {
        List<Long> list = nextID(bizType, 1);
        return list.get(0);
    }

    public Integer nextIntID(String bizType) {
        Long longID = nextID(bizType);
        if (longID > Integer.MAX_VALUE) {
            throw new RuntimeException("获取ID超出int范围");
        }
        return longID.intValue();
    }

    public List<Integer> nextIntID(String bizType, int batchSize) {
        List<Long> longIDList = nextID(bizType, batchSize);

        List<Integer> intIDList = longIDList.stream().peek(a -> {
            if (a > Integer.MAX_VALUE) {
                throw new RuntimeException("获取ID超出int范围");
            }
        }).mapToInt(Long::intValue).boxed().collect(Collectors.toList());

        return intIDList;
    }

}
