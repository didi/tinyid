package com.xiaoju.uemc.tinyid.server.service;

/**
 * @author zhangbingbing
 * @date 2020/10/16
 */
public interface SnowflakeWorkIdService {

    /**
     * 获取雪花算法中的workId
     */
    int workId();
}
