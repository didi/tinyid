package com.xiaoju.uemc.tinyid.client.config;

import java.util.List;

/**
 * @author du_imba
 */
public class TinyIdClientConfig {

    private String tinyIdToken;
    private String tinyIdServer;
    private List<String> serverList;
    private Integer readTimeout;
    private Integer connectTimeout;

    private volatile static TinyIdClientConfig tinyIdClientConfig;

    private TinyIdClientConfig() {
    }

    public static TinyIdClientConfig getInstance() {
        if (tinyIdClientConfig != null) {
            return tinyIdClientConfig;
        }
        synchronized (TinyIdClientConfig.class) {
            if (tinyIdClientConfig != null) {
                return tinyIdClientConfig;
            }
            tinyIdClientConfig = new TinyIdClientConfig();
        }
        return tinyIdClientConfig;
    }

    public String getTinyIdToken() {
        return tinyIdToken;
    }

    public void setTinyIdToken(String tinyIdToken) {
        this.tinyIdToken = tinyIdToken;
    }

    public String getTinyIdServer() {
        return tinyIdServer;
    }

    public void setTinyIdServer(String tinyIdServer) {
        this.tinyIdServer = tinyIdServer;
    }

    public List<String> getServerList() {
        return serverList;
    }

    public void setServerList(List<String> serverList) {
        this.serverList = serverList;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
}
