package org.chzz.demo.model;

import java.util.List;

/**
 * Created by copy on 2017/10/12.
 */

public class InformationEntity extends BaseEntity {

    List<DataEntity> data;

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity {
        String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
