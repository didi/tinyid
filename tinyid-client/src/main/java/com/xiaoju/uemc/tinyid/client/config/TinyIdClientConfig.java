package com.xiaoju.uemc.tinyid.client.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.List;

/**
 * @author du_imba
 */
@Data
public class TinyIdClientConfig {
    private String cacheServerUrl;
    private String noCacheServerUrl;
    private String token;
    private boolean localCache;

    public TinyIdClientConfig(String cacheServerUrl, String noCacheServerUrl, String token, boolean localCache) {
        this.cacheServerUrl = cacheServerUrl;
        this.noCacheServerUrl = noCacheServerUrl;
        this.token = token;
        this.localCache = localCache;
    }
}
