package com.xiaoju.uemc.tinyid.server.service;

import java.util.List;

/**
 * @author zhangbingbing
 * @date 2020/10/16
 */
public interface SnowflakeIdService {

    Long nextId(String businessType);

    List<Long> nextIdBatch(String businessType, int batchSize);
}
