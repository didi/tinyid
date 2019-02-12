package com.xiaoju.uemc.tinyid.server.controller;

import com.xiaoju.uemc.tinyid.base.entity.SegmentId;
import com.xiaoju.uemc.tinyid.base.generator.IdGenerator;
import com.xiaoju.uemc.tinyid.base.service.SegmentIdService;
import com.xiaoju.uemc.tinyid.server.factory.impl.IdGeneratorFactoryServer;
import com.xiaoju.uemc.tinyid.server.service.TinyIdTokenService;
import com.xiaoju.uemc.tinyid.server.vo.ErrorCode;
import com.xiaoju.uemc.tinyid.server.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author du_imba
 */
@RestController
@RequestMapping("/id/")
public class IdContronller {

    private static final Logger logger = LoggerFactory.getLogger(IdContronller.class);
    @Autowired
    private IdGeneratorFactoryServer idGeneratorFactoryServer;
    @Autowired
    private SegmentIdService segmentIdService;
    @Autowired
    private TinyIdTokenService tinyIdTokenService;
    @Value("${batch.size.max}")
    private Integer batchSizeMax;

    @RequestMapping("nextId")
    public Response<List<Long>> nextId(String bizType, Integer batchSize, String token) {
        Response<List<Long>> response = new Response<>();
        Integer newBatchSize = checkBatchSize(batchSize);
        if (!tinyIdTokenService.canVisit(bizType, token)) {
            response.setCode(ErrorCode.TOKEN_ERR.getCode());
            response.setMessage(ErrorCode.TOKEN_ERR.getMessage());
            return response;
        }
        try {
            IdGenerator idGenerator = idGeneratorFactoryServer.getIdGenerator(bizType);
            List<Long> ids = idGenerator.nextId(newBatchSize);
            response.setData(ids);
        } catch (Exception e) {
            response.setCode(ErrorCode.SYS_ERR.getCode());
            response.setMessage(e.getMessage());
            logger.error("nextId error", e);
        }
        return response;
    }

    private Integer checkBatchSize(Integer batchSize) {
        if (batchSize == null) {
            batchSize = 1;
        }
        if (batchSize > batchSizeMax) {
            batchSize = batchSizeMax;
        }
        return batchSize;
    }

    @RequestMapping("nextIdSimple")
    public String nextIdSimple(String bizType, Integer batchSize, String token) {
        Integer newBatchSize = checkBatchSize(batchSize);
        if (!tinyIdTokenService.canVisit(bizType, token)) {
            return "";
        }
        String response = "";
        try {
            IdGenerator idGenerator = idGeneratorFactoryServer.getIdGenerator(bizType);
            if (newBatchSize == 1) {
                Long id = idGenerator.nextId();
                response = id + "";
            } else {
                List<Long> idList = idGenerator.nextId(newBatchSize);
                StringBuilder sb = new StringBuilder();
                for (Long id : idList) {
                    sb.append(id).append(",");
                }
                response = sb.deleteCharAt(sb.length() - 1).toString();
            }
        } catch (Exception e) {
            logger.error("nextIdSimple error", e);
        }
        return response;
    }

    @RequestMapping("nextSegmentId")
    public Response<SegmentId> nextSegmentId(String bizType, String token) {
        Response<SegmentId> response = new Response<>();
        if (!tinyIdTokenService.canVisit(bizType, token)) {
            response.setCode(ErrorCode.TOKEN_ERR.getCode());
            response.setMessage(ErrorCode.TOKEN_ERR.getMessage());
            return response;
        }
        try {
            SegmentId segmentId = segmentIdService.getNextSegmentId(bizType);
            response.setData(segmentId);
        } catch (Exception e) {
            response.setCode(ErrorCode.SYS_ERR.getCode());
            response.setMessage(e.getMessage());
            logger.error("nextSegmentId error", e);
        }
        return response;
    }

    @RequestMapping("nextSegmentIdSimple")
    public String nextSegmentIdSimple(String bizType, String token) {
        if (!tinyIdTokenService.canVisit(bizType, token)) {
            return "";
        }
        String response = "";
        try {
            SegmentId segmentId = segmentIdService.getNextSegmentId(bizType);
            response = segmentId.getCurrentId() + "," + segmentId.getLoadingId() + "," + segmentId.getMaxId()
                    + "," + segmentId.getDelta() + "," + segmentId.getRemainder();
        } catch (Exception e) {
            logger.error("nextSegmentIdSimple error", e);
        }
        return response;
    }

}
