package com.xiaoju.uemc.tinyid.client.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.List;

/**
 * @author du_imba
 */
@Data
@Configuration
@EnableConfigurationProperties(TinyIdClientConfigProperties.class)
public class TinyIdClientConfig {

    private String tinyIdServer;
    private int readTimeout;
    private int connectTimeout;

    private static final int DEFAULT_TIME_OUT = 5000;
    private static String serverUrl = "http://{0}/tinyid/id/nextSegmentIdSimple?token={1}&bizType=";

    @Autowired
    private TinyIdClientConfigProperties properties;

    public TinyIdClientConfig() {

    }

    public TinyIdClientConfig(String tinyIdServer, int readTimeout, int connectTimeout) {
        this.tinyIdServer = tinyIdServer;
        this.readTimeout = readTimeout;
        this.connectTimeout = connectTimeout;
    }

    @Bean
    public TinyIdClientConfig initTinyIdClientConfig() {
        if (StringUtils.isEmpty(properties.getToken())) {
            throw new IllegalArgumentException("cannot find tinyid.token config in application");
        }
        if (StringUtils.isEmpty(properties.getServer())) {
            throw new IllegalArgumentException("cannot find tinyid.server config in application");
        }
        if (properties.getReadTimeout() <= 0) {
            properties.setReadTimeout(DEFAULT_TIME_OUT);
        }
        if (properties.getConnectTimeout() <= 0) {
            properties.setConnectTimeout(DEFAULT_TIME_OUT);
        }

        String server = MessageFormat.format(serverUrl, properties.getServer(), properties.getToken());

        TinyIdClientConfig clientConfig = new TinyIdClientConfig(server, properties.getReadTimeout(), properties.getConnectTimeout());
        return clientConfig;
    }

}
