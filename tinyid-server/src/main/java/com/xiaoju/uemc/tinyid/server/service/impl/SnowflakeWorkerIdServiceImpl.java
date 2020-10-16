package com.xiaoju.uemc.tinyid.server.service.impl;

import com.xiaoju.uemc.tinyid.base.exception.SystemClockCallbackException;
import com.xiaoju.uemc.tinyid.base.util.IPUtil;
import com.xiaoju.uemc.tinyid.server.common.annotation.Module;
import com.xiaoju.uemc.tinyid.server.service.SnowflakeWorkIdService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 通过zk的有序节点，来获取一个自增的数作为workerId
 *
 * @author zhangbingbing
 * @date 2020/10/16
 */
@Service
@Module(value = "snowflake.enable")
public class SnowflakeWorkerIdServiceImpl implements SnowflakeWorkIdService {

    private static final Logger log = LoggerFactory.getLogger(SnowflakeWorkerIdServiceImpl.class);

    @Autowired
    @Qualifier("updateDataToZKScheduledExecutorService")
    private ScheduledExecutorService scheduledExecutorService;
    /**
     * 保存所有数据持久的节点
     */
    private static final String ZK_PATH = "/tinyid/snowflake/forever";
    /**
     * 持久化workerId，文件存放位置
     */
    private static final String DUMP_PATH = "workerID/workerID.properties";
    /**
     * 是否在连接不上zk时，使用之前备份的workerId
     */
    @Value("${snowflake.zk.load-worker-id-from-file-when-zk-down:true}")
    private boolean loadWorkerIdFromFileWhenZkDown;
    /**
     * zk连接信息
     */
    @Value("${snowflake.zk.connection-string}")
    private String connectionString;
    /**
     * 本机地址
     */
    private String ip;
    /**
     * 本机端口
     */
    @Value("${server.port}")
    private Integer port;
    /**
     * 上次更新数据时间
     */
    private long lastUpdateAt;
    /**
     * workerId
     */
    private volatile Integer workerId;

    @Override
    public int workId() {
        try {
            if (workerId != null) {
                return workerId;
            }
            doInitWorkId();
            return workerId;
        } catch (Exception e) {
            throw new IllegalStateException("获取workerId失败", e);
        }
    }

    private synchronized void doInitWorkId() throws Exception {
        if (workerId != null) {
            return;
        }
        try {
            ip = IPUtil.getHostAddress();
            String localZKPath = ZK_PATH + "/" + ip + ":" + port;
            CuratorFramework client = getZKConnection();
            final Stat stat = client.checkExists().forPath(ZK_PATH);
            // 不存在根结点，第一次使用，创建根结点
            if (stat == null) {
                // 创建有序永久结点 /tinyid/snowflake/forever/ip:port-xxx,并上传数据
                localZKPath = createPersistentSequentialNode(client, localZKPath, buildData());
                workerId = getWorkerId(localZKPath);
                // 持久化workerId
                updateWorkerId(workerId);
                // 定时上报本机时间到zk
                scheduledUploadTimeToZK(client, localZKPath);
                return;
            }
            // Map<localAddress,workerId>
            Map<String, Integer> localAddressWorkerIdMap = new HashMap<>(16);
            // Map<localAddress,path>
            Map<String, String> localAddressPathMap = new HashMap<>(16);
            for (String key : client.getChildren().forPath(ZK_PATH)) {
                final String[] split = key.split("-");
                localAddressPathMap.put(split[0], key);
                // value=zk有序结点的需要
                localAddressWorkerIdMap.put(split[0], Integer.valueOf(split[1]));
            }
            String localAddress = ip + ":" + port;
            workerId = localAddressWorkerIdMap.get(localAddress);
            if (workerId != null) {
                localZKPath = ZK_PATH + "/" + localAddressPathMap.get(localAddress);
                // 校验时间是否回调
                checkTimestamp(client, localZKPath);
                scheduledUploadTimeToZK(client, localZKPath);
                updateWorkerId(workerId);
                return;
            }
            localZKPath = createPersistentSequentialNode(client, localZKPath, buildData());
            workerId = Integer.parseInt((localZKPath.split("-"))[1]);
            scheduledUploadTimeToZK(client, localZKPath);
            updateWorkerId(workerId);
        } catch (Exception e) {
            if (!loadWorkerIdFromFileWhenZkDown) {
                throw e;
            }
            SnowflakeWorkerIdServiceImpl.log.error("can load worker id from zk , try to load worker id from file", e);
            // 从本地文件中读取workerId，如果系统时针回调，可能会出现
            final Integer workerIdFromFile = loadWorkerIdFromFile();
            if (workerIdFromFile != null) {
                workerId = workerIdFromFile;
                return;
            }
            throw e;
        }
    }


    private Integer getWorkerId(String localZKPath) {
        return Integer.parseInt(localZKPath.split("-")[1]);
    }

    /**
     * @return true 检查通过
     */
    private void checkTimestamp(CuratorFramework client, String localZKPath) throws Exception {
        final Endpoint endpoint = parseData(new String(client.getData().forPath(localZKPath)));
        // 该节点的时间不能大于最后一次上报的时间
        if (endpoint.getTimestamp() > System.currentTimeMillis()) {
            throw new SystemClockCallbackException("system clock callback");
        }
    }

    /**
     * 获取zk连接
     */
    public CuratorFramework getZKConnection() {
        CuratorFramework framework = CuratorFrameworkFactory.builder()
                .connectString(connectionString)
                .retryPolicy(new RetryUntilElapsed((int) TimeUnit.SECONDS.toMillis(5), (int) TimeUnit.SECONDS.toMillis(1)))
                .connectionTimeoutMs((int) TimeUnit.SECONDS.toMillis(10))
                .sessionTimeoutMs((int) TimeUnit.SECONDS.toMillis(6))
                .build();
        framework.start();
        return framework;
    }

    /**
     * 上传数据到zk,每5s上传一次
     */
    private void scheduledUploadTimeToZK(final CuratorFramework client, final String localZKPath) {
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                // 如果时针回调了就不同步
                if (System.currentTimeMillis() < lastUpdateAt) {
                    return;
                }
                try {
                    client.setData().forPath(localZKPath, buildData());
                    lastUpdateAt = System.currentTimeMillis();
                    log.debug("upload time to zk at" + lastUpdateAt);
                } catch (Exception e) {
                    log.error("update init data error path is {} error is {}", localZKPath, e);
                }
            }
        }, 5, TimeUnit.SECONDS);
    }

    /**
     * 将获取到的workerId持久化到文件
     *
     * @return
     */
    private Integer loadWorkerIdFromFile() {
        try (InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(DUMP_PATH)) {
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            final String workerID = properties.getProperty("workerID");
            if (workerID != null) {
                return Integer.parseInt(workerID);
            }
            return null;
        } catch (IOException e) {
            SnowflakeWorkerIdServiceImpl.log.error("load worker id from file error", e);
        }
        return null;
    }

    private void updateWorkerId(int workerId) {
        if (!loadWorkerIdFromFileWhenZkDown) {
            return;
        }
        try {
            String classpath = ResourceUtils.getURL("classpath:").getFile();
            File file = new File(classpath + "/" + DUMP_PATH);
            if (!file.exists()) {
                boolean mkdirs = file.getParentFile().mkdirs();
                if (!mkdirs) {
                    log.error("mkdir {} error", file.getParentFile().toString());
                    return;
                }
                log.info("mkdir {}", file.toString());
            }
            Files.write(file.toPath(), ("workerID=" + workerId).getBytes());
        } catch (FileNotFoundException e) {
            log.error("", e);
        } catch (IOException e) {
            log.warn("write workerID to file {} error", DUMP_PATH, e);
        }
    }

    private String createPersistentSequentialNode(CuratorFramework client, String path, byte[] data) throws Exception {
        return client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .forPath(path + "-", data);
    }

    private byte[] buildData() {
        return (ip + "&" + port + "&" + System.currentTimeMillis()).getBytes();
    }

    private Endpoint parseData(String data) {
        String[] split = data.split("&");
        return new Endpoint(split[0], split[1], Long.parseLong(split[2]));
    }

    /**
     * 上传到zk的数据
     */
    private static class Endpoint {
        private String ip;
        private String port;
        private Long timestamp;

        public Endpoint(String ip, String port, Long timestamp) {
            this.ip = ip;
            this.port = port;
            this.timestamp = timestamp;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }
    }
}
