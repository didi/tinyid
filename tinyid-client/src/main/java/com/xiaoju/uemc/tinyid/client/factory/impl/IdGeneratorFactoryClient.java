package com.xiaoju.uemc.tinyid.client.factory.impl;

import com.xiaoju.uemc.tinyid.base.factory.AbstractIdGeneratorFactory;
import com.xiaoju.uemc.tinyid.base.generator.IdGenerator;
import com.xiaoju.uemc.tinyid.base.generator.impl.CachedIdGenerator;
import com.xiaoju.uemc.tinyid.client.config.TinyIdClientConfig;
import com.xiaoju.uemc.tinyid.client.service.impl.HttpSegmentIdServiceImpl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author du_imba
 */
public class IdGeneratorFactoryClient extends AbstractIdGeneratorFactory {

    private static final Logger logger = Logger.getLogger(IdGeneratorFactoryClient.class.getName());

    private static IdGeneratorFactoryClient idGeneratorFactoryClient;

    private static final int DEFAULT_TIME_OUT = 5000;

    private static String serverUrl = "http://{0}/tinyid/id/nextSegmentIdSimple?token={1}&bizType=";

    private IdGeneratorFactoryClient() {

    }

    public static IdGeneratorFactoryClient getInstance(TinyIdClientConfig config) {
        if (idGeneratorFactoryClient == null) {
            synchronized (IdGeneratorFactoryClient.class) {
                if (idGeneratorFactoryClient == null) {
                    init(config);
                }
            }
        }
        return idGeneratorFactoryClient;
    }

    private static void init(TinyIdClientConfig config) {
        idGeneratorFactoryClient = new IdGeneratorFactoryClient();

        String tinyIdToken = config.getTinyIdToken();
        String tinyIdServer = config.getTinyIdServer();
        if (config.getReadTimeout() == null){
            config.setReadTimeout(DEFAULT_TIME_OUT);
        }
        if (config.getConnectTimeout() == null){
            config.setConnectTimeout(DEFAULT_TIME_OUT);
        }

        if (tinyIdToken == null || "".equals(tinyIdToken.trim())
                || tinyIdServer == null || "".equals(tinyIdServer.trim())) {
            throw new IllegalArgumentException("cannot find tinyid.token and tinyid.server");
        }

        String[] tinyIdServers = tinyIdServer.split(",");
        List<String> serverList = new ArrayList<>(tinyIdServers.length);
        for (String server : tinyIdServers) {
            String url = MessageFormat.format(serverUrl, server, tinyIdToken);
            serverList.add(url);
        }
        logger.info("init tinyId client success url info:" + serverList);
        config.setServerList(serverList);
    }

    @Override
    protected IdGenerator createIdGenerator(String bizType) {
        return new CachedIdGenerator(bizType, new HttpSegmentIdServiceImpl());
    }

}
