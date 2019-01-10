package com.pass.service.common.vo;

/**
 * APP Token VO类
 *
 * @author liangcm
 */
public class LoginToken {
    private String token;
    private long timeExpire;

    public LoginToken() {

    }

    public LoginToken(String token, long timeExpire) {
        this.token = token;
        this.timeExpire = timeExpire;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(long timeExpire) {
        this.timeExpire = timeExpire;
    }

    @Override
    public String toString() {
        return "AppToken [token=" + token + ", timeExpire=" + timeExpire + "]";
    }
}
