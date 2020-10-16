package com.xiaoju.uemc.tinyid.server.controller;

import com.xiaoju.uemc.tinyid.server.common.annotation.Module;
import com.xiaoju.uemc.tinyid.server.service.SnowflakeIdService;
import com.xiaoju.uemc.tinyid.server.service.TinyIdTokenService;
import com.xiaoju.uemc.tinyid.server.vo.ErrorCode;
import com.xiaoju.uemc.tinyid.server.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhangbingbing
 * @date 2020/10/16
 */
@Module(value = "snowflake.enable")
@RestController
@RequestMapping("/id/snowflake")
public class SnowflakeIdController {

    private static final Logger logger = LoggerFactory.getLogger(SnowflakeIdController.class);

    @Autowired
    private TinyIdTokenService tinyIdTokenService;
    @Autowired
    private SnowflakeIdService idService;

    @RequestMapping("/nextIds")
    public Response<List<Long>> nextIds(String bizType, Integer batchSize, String token) {
        Response<List<Long>> response = new Response<>();
        if (!tinyIdTokenService.canVisit(bizType, token)) {
            response.setCode(ErrorCode.TOKEN_ERR.getCode());
            response.setMessage(ErrorCode.TOKEN_ERR.getMessage());
            return response;
        }
        try {
            response.setData(idService.nextIdBatch(bizType, batchSize));
        } catch (Exception e) {
            response.setCode(ErrorCode.SYS_ERR.getCode());
            response.setMessage(e.getMessage());
            logger.error("nextId error", e);
        }
        return response;
    }

    @RequestMapping("/nextId")
    public Response<Long> nextId(String bizType, String token) {
        Response<Long> response = new Response<>();
        if (!tinyIdTokenService.canVisit(bizType, token)) {
            response.setCode(ErrorCode.TOKEN_ERR.getCode());
            response.setMessage(ErrorCode.TOKEN_ERR.getMessage());
            return response;
        }
        try {
            response.setData(idService.nextId(bizType));
        } catch (Exception e) {
            response.setCode(ErrorCode.SYS_ERR.getCode());
            response.setMessage(e.getMessage());
            logger.error("nextId error", e);
        }
        return response;
    }
}
