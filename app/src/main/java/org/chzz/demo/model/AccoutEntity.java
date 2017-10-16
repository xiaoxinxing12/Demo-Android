package org.chzz.demo.model;

/**
 * Created by copy on 2017/10/16.
 */

public class AccoutEntity extends BaseEntity {

    public AccoutEntity(String name, String password) {
        this.name = name;
        this.password = password;
    }

    String name;
    String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
