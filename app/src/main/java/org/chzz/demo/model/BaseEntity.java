package org.chzz.demo.model;

import android.databinding.BaseObservable;

import java.io.Serializable;

/**
 * Created by copy on 2017/4/19.
 * 
 */

public class BaseEntity extends BaseObservable implements Serializable {

    private int code;
    private int itemFlag;

    public int getItemFlag() {
        return itemFlag;
    }

    public void setItemFlag(int itemFlag) {
        this.itemFlag = itemFlag;
    }

    private String desc;

    public void setCode(int code) {
        this.code = code;
    }


    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
