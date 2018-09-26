package com.xiaoju.uemc.tinyid.client.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author du_imba
 */
public class PropertiesLoader {
    private static final Logger logger = Logger.getLogger(PropertiesLoader.class.getName());

    private PropertiesLoader() {

    }

    public static Properties loadProperties(String location) {
        Properties props = new Properties();
        logger.info("Loading properties file from path:" + location);
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(PropertiesLoader.class.getClassLoader().getResourceAsStream(location), "UTF-8");
            props.load(in);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, "error close inputstream", e);
                }
            }
        }
        return props;

    }

}
