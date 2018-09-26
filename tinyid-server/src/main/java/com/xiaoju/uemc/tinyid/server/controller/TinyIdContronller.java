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
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author du_imba
 */
@RestController
@RequestMapping("/")
public class TinyIdContronller {

    private static final Logger logger = LoggerFactory.getLogger(TinyIdContronller.class);
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
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            IdGenerator idGenerator = idGeneratorFactoryServer.getIdGenerator(bizType);
            List<Long> ids = idGenerator.nextId(newBatchSize);
            response.setData(ids);
        } catch (Exception e) {
            response.setCode(ErrorCode.SYS_ERR.getCode());
            response.setMessage(e.getMessage());
            logger.error("nextId error", e);
        }
        stopWatch.stop();
        logger.info("nextId bizType:{}, batchSize:{}, token:{}, cost:{}", bizType, batchSize, token, stopWatch.getTotalTimeMillis());
        return response;
    }

    private Integer checkBatchSize(Integer batchSize) {
        Integer newBatchSize = batchSize;
        if (newBatchSize == null) {
            newBatchSize = 1;
        }
        if (newBatchSize > batchSizeMax) {
            newBatchSize = batchSizeMax;
        }
        return newBatchSize;
    }

    @RequestMapping("nextIdSimple")
    public String nextIdSimple(String bizType, Integer batchSize, String token) {
        Integer newBatchSize = checkBatchSize(batchSize);
        if (!tinyIdTokenService.canVisit(bizType, token)) {
            return "";
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
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
        stopWatch.stop();
        logger.info("nextIdSimple bizType:{}, batchSize:{}, token:{}, cost:{}", bizType, batchSize, token, stopWatch.getTotalTimeMillis());
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
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SegmentId segmentId = null;
        try {
            segmentId = segmentIdService.getNextSegmentId(bizType);
            response.setData(segmentId);
        } catch (Exception e) {
            response.setCode(ErrorCode.SYS_ERR.getCode());
            response.setMessage(e.getMessage());
            logger.error("nextSegmentId error", e);
        }
        stopWatch.stop();
        logger.info("nextSegmentId bizType:{}, token:{}, response:{}, cost:{}", bizType, token, segmentId, stopWatch.getTotalTimeMillis());
        return response;
    }

    @RequestMapping("nextSegmentIdSimple")
    public String nextSegmentIdSimple(String bizType, String token) {
        if (!tinyIdTokenService.canVisit(bizType, token)) {
            return "";
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String response = "";
        try {
            SegmentId segmentId = segmentIdService.getNextSegmentId(bizType);
            response = segmentId.getCurrentId() + "," + segmentId.getLoadingId() + "," + segmentId.getMaxId()
                    + "," + segmentId.getDelta() + "," + segmentId.getRemainder();
        } catch (Exception e) {
            logger.error("nextSegmentIdSimple error", e);
        }
        stopWatch.stop();
        logger.info("nextSegmentIdSimple bizType:{}, token:{}, response:{}, cost:{}", bizType, token, response, stopWatch.getTotalTimeMillis());
        return response;
    }

}
