package com.xiaoju.uemc.tinyid.server.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.List;
import java.util.Random;

/**
 * @author du_imba
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private List<String> dataSourceKeys;

    @Override
    protected Object determineCurrentLookupKey() {
        if(dataSourceKeys.size() == 1) {
            return dataSourceKeys.get(0);
        }
        Random r = new Random();
        return dataSourceKeys.get(r.nextInt(dataSourceKeys.size()));
    }

    public List<String> getDataSourceKeys() {
        return dataSourceKeys;
    }

    public void setDataSourceKeys(List<String> dataSourceKeys) {
        this.dataSourceKeys = dataSourceKeys;
    }
}
