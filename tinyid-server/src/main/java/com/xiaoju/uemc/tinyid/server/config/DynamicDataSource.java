package com.xiaoju.uemc.tinyid.server.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import java.util.List;

/**
 * @author du_imba
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private List<String> dataSourceKeys;

    private Node indexRing = null;

    @Override
    protected Object determineCurrentLookupKey() {
        if(dataSourceKeys.size() == 1) {
            return dataSourceKeys.get(0);
        }
        if (indexRing == null){
            synchronized (DynamicDataSource.class){
                if (indexRing == null){
                    indexRing = new Node(0);

                    Node pre = indexRing;
                    for (int i = 1; i < dataSourceKeys.size(); i++) {
                        Node next = new Node(i);
                        pre.next = next;
                        pre = next;
                    }
                    pre.next = indexRing;
                }
            }
        }

        return dataSourceKeys.get(indexRing.nextIndex());
    }

    public void setDataSourceKeys(List<String> dataSourceKeys) {
        this.dataSourceKeys = dataSourceKeys;
    }


    public static class Node {
        private int value;

        private Node next;
        private Node current;

        public Node(int value) {
            this.value = value;
        }

        public int nextIndex(){
            if (current == null){
                current = this;
            }
            int nextIndex = current.value;
            current = current.next;
            return nextIndex;
        }
    }

}
