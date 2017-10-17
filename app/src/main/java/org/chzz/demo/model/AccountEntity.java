package org.chzz.demo.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by copy on 2017/10/16.
 */

public class AccountEntity extends BaseEntity {

    private List<DataEntity> list;

    public List<DataEntity> getList() {
        return list;
    }

    public void setList(List<DataEntity> list) {
        this.list = list;
    }

    public static class DataEntity implements Serializable {
        public DataEntity(String nick, String coupon) {
            this.nick = nick;
            this.coupon = coupon;
        }

        public DataEntity(String name, String password, String nick) {
            this.name = name;
            this.password = password;
            this.nick = nick;
        }

        public DataEntity(String name, String password, String nick, boolean isImportant) {
            this.name = name;
            this.password = password;
            this.nick = nick;
            this.isImportant = isImportant;
        }

        String name;
        String password;
        String nick;
        String coupon;
        boolean isImportant;

        public boolean isImportant() {
            return isImportant;
        }

        public void setImportant(boolean important) {
            isImportant = important;
        }

        public String getCoupon() {
            return coupon;
        }

        public void setCoupon(String coupon) {
            this.coupon = coupon;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

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

}
