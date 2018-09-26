package com.xiaoju.uemc.tinyid.client.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author du_imba
 */
public class TinyIdHttpUtils {

    private static final Logger logger = Logger.getLogger(TinyIdHttpUtils.class.getName());


    private TinyIdHttpUtils() {

    }

    public static String post(String url, Integer readTimeout, Integer connectTimeout) {
        return post(url, null, readTimeout, connectTimeout);
    }

    public static String post(String url, Map<String, String> form, Integer readTimeout, Integer connectTimeout) {
        HttpURLConnection conn = null;
        OutputStreamWriter os = null;
        BufferedReader rd = null;
        StringBuilder param = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        String line = null;
        String response = null;
        if (form != null) {
            for (Map.Entry<String, String> entry : form.entrySet()) {
                String key = entry.getKey();
                if (param.length() != 0) {
                    param.append("&");
                }
                param.append(key).append("=").append(entry.getValue());
            }
        }
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(readTimeout);
            conn.setConnectTimeout(connectTimeout);
            conn.setUseCaches(false);
            conn.connect();
            os = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            os.write(param.toString());
            os.flush();
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            response = sb.toString();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "error post url:" + url + param, e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (rd != null) {
                    rd.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException e) {
                logger.log(Level.WARNING, "error close conn", e);
            }
        }
        return response;
    }
}
