package com.xiaoju.uemc.tinyid.server.config;

import com.xiaoju.uemc.tinyid.server.common.annotation.Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhangbingbing
 * @date 2020/10/16
 */
@Module(value = "snowflake.enable")
@Configuration
public class SnowflakeWorkerIdConfig {

    /**
     * 定时上传数据到zk的定时任务线程池
     */
    @Bean
    @Module(value = "snowflake.enable")
    public ScheduledExecutorService updateDataToZKScheduledExecutorService() {
        final AtomicInteger threadIncr = new AtomicInteger(0);
        return new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                int incr = threadIncr.incrementAndGet();
                if (incr >= 1000) {
                    threadIncr.set(0);
                    incr = 1;
                }
                return new Thread(r, "upload-data-to-zk-schedule-thread" + incr);
            }
        }, new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
