package com.xiaoju.uemc.tinyid.server.dao;

import com.xiaoju.uemc.tinyid.server.dao.entity.TinyIdInfo;

/**
 * @author du_imba
 */
public interface TinyIdInfoDAO {
    /**
     * 根据bizType获取db中的tinyId对象
     * @param bizType
     * @return
     */
    TinyIdInfo queryByBizType(String bizType);

    /**
     * 根据id、oldMaxId、version更新最新的maxId
     * @param id
     * @param newMaxId
     * @param oldMaxId
     * @param version
     * @return
     */
    int updateMaxId(Long id, Long newMaxId, Long oldMaxId, Long version);
}
