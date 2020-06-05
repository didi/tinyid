package com.xiaoju.uemc.tinyid.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "tinyid")
public class TinyIdClientConfigProperties {
    /**
     * 本地缓存开启时，会预读取两段号段到本项目中，然后从本地内存获得ID
     * 缓存关闭时，http直接访问服务端获得ID
     */
    private boolean localCache = false;
    /**
     * 服务端地址
     */
    private String server;
    /**
     * 本地token
     */
    private String token;

    private int readTimeout = 10;

    private int connectTimeout = 10;
}
