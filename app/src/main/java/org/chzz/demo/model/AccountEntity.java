package org.chzz.demo.model;

/**
 * Created by copy on 2017/10/16.
 */

public class AccountEntity extends BaseEntity {
    public AccountEntity(String nick, String coupon) {
        this.nick = nick;
        this.coupon = coupon;
    }

    public AccountEntity(String name, String password, String nick) {
        this.name = name;
        this.password = password;
        this.nick = nick;
    }

    String name;
    String password;
    String nick;
    String coupon;

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
