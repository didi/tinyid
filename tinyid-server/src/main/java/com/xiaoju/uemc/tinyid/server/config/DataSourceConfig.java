package com.xiaoju.uemc.tinyid.server.config;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author du_imba
 */
@Configuration
public class DataSourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Autowired
    private Environment environment;
    private static final String SEP = ",";

    private static final String DEFAULT_DATASOURCE_TYPE = "org.apache.tomcat.jdbc.pool.DataSource";

    @Bean
    public DataSource getDynamicDataSource() {
        DynamicDataSource routingDataSource = new DynamicDataSource();
        List<String> dataSourceKeys = new ArrayList<>();
        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(environment, "datasource.tinyid.");
        String names = propertyResolver.getProperty("names");
        String dataSourceType = propertyResolver.getProperty("type");

        Map<Object, Object> targetDataSources = new HashMap<>(4);
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDataSourceKeys(dataSourceKeys);
        // 多个数据源
        for (String name : names.split(SEP)) {
            Map<String, Object> dsMap = propertyResolver.getSubProperties(name + ".");
            DataSource dataSource = buildDataSource(dataSourceType, dsMap);
            buildDataSourceProperties(dataSource, dsMap);
            targetDataSources.put(name, dataSource);
            dataSourceKeys.add(name);
        }
        return routingDataSource;
    }

    private void buildDataSourceProperties(DataSource dataSource, Map<String, Object> dsMap) {
        try {
            // 此方法性能差，慎用
            BeanUtils.copyProperties(dataSource, dsMap);
        } catch (Exception e) {
            logger.error("error copy properties", e);
        }
    }

    private DataSource buildDataSource(String dataSourceType, Map<String, Object> dsMap) {
        try {
            String className = DEFAULT_DATASOURCE_TYPE;
            if (dataSourceType != null && !"".equals(dataSourceType.trim())) {
                className = dataSourceType;
            }
            Class<? extends DataSource> type = (Class<? extends DataSource>) Class.forName(className);
            String driverClassName = dsMap.get("driver-class-name").toString();
            String url = dsMap.get("url").toString();
            String username = dsMap.get("username").toString();
            String password = dsMap.get("password").toString();

            return DataSourceBuilder.create()
                    .driverClassName(driverClassName)
                    .url(url)
                    .username(username)
                    .password(password)
                    .type(type)
                    .build();

        } catch (ClassNotFoundException e) {
            logger.error("buildDataSource error", e);
            throw new IllegalStateException(e);
        }
    }


}
