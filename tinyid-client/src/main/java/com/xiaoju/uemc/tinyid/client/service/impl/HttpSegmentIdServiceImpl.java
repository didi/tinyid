package com.xiaoju.uemc.tinyid.client.service.impl;

import com.xiaoju.uemc.tinyid.base.entity.SegmentId;
import com.xiaoju.uemc.tinyid.base.service.SegmentIdService;
import com.xiaoju.uemc.tinyid.client.config.TinyIdClientConfig;
import com.xiaoju.uemc.tinyid.client.utils.TinyIdHttpUtils;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * @author du_imba
 */
public class HttpSegmentIdServiceImpl implements SegmentIdService {

    private static final Logger logger = Logger.getLogger(HttpSegmentIdServiceImpl.class.getName());

    @Override
    public SegmentId getNextSegmentId(String bizType) {
        String url = chooseService(bizType);
        String response = TinyIdHttpUtils.post(url, TinyIdClientConfig.getInstance().getReadTimeout(),
                TinyIdClientConfig.getInstance().getConnectTimeout());
        logger.info("tinyId client getNextSegmentId end, response:" + response);
        if (response == null || "".equals(response.trim())) {
            return null;
        }
        SegmentId segmentId = new SegmentId();
        String[] arr = response.split(",");
        segmentId.setCurrentId(new AtomicLong(Long.parseLong(arr[0])));
        segmentId.setLoadingId(Long.parseLong(arr[1]));
        segmentId.setMaxId(Long.parseLong(arr[2]));
        segmentId.setDelta(Integer.parseInt(arr[3]));
        segmentId.setRemainder(Integer.parseInt(arr[4]));
        return segmentId;
    }

    private String chooseService(String bizType) {
        List<String> serverList = TinyIdClientConfig.getInstance().getServerList();
        String url = "";
        if (serverList != null && serverList.size() == 1) {
            url = serverList.get(0);
        } else if (serverList != null && serverList.size() > 1) {
            Random r = new Random();
            url = serverList.get(r.nextInt(serverList.size()));
        }
        url += bizType;
        return url;
    }


}
