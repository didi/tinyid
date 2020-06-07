package com.xiaoju.uemc.tinyid.client.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

@Configuration
@EnableConfigurationProperties(TinyIdClientConfigProperties.class)
public class TinyIdAutoConfig {
    @Autowired
    private TinyIdClientConfigProperties properties;
    private static final int MIN_TIME_OUT = 1 * 1000;
    private static final String cacheServerUrl = "http://{0}/tinyid/id/nextSegmentIdSimple";
    private static final String noCacheServerUrl = "http://{0}/tinyid/id/nextIdSimple";

    @Bean
    public TinyIdClientConfig config() {

        if (StringUtils.isEmpty(properties.getToken())) {
            throw new IllegalArgumentException("cannot find tinyid.token config in application");
        }
        if (StringUtils.isEmpty(properties.getServer())) {
            throw new IllegalArgumentException("cannot find tinyid.server config in application");
        }

        String cacheServer = MessageFormat.format(cacheServerUrl, properties.getServer());
        String noCacheServer = MessageFormat.format(noCacheServerUrl, properties.getServer());
        return new TinyIdClientConfig(cacheServer, noCacheServer, properties.getToken(), properties.isLocalCache());
    }

    @Bean
    public RestTemplate restTemplate() {

        if (properties.getReadTimeout() < MIN_TIME_OUT) {
            properties.setReadTimeout(MIN_TIME_OUT);
        }

        if (properties.getConnectTimeout() < MIN_TIME_OUT) {
            properties.setConnectTimeout(MIN_TIME_OUT);
        }

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(properties.getConnectTimeout());
        requestFactory.setReadTimeout(properties.getReadTimeout());

        return new RestTemplate(requestFactory);
    }

}
