package com.pass.service.common.entity;

import java.util.Date;

public class SystemLog {
    
    private String model;
    
    private String url;
    
    private String username;
    
    private String content;
    
    private Date requestTime;
    
    private String ip;
    
    private String systemCode;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    @Override
    public String toString() {
        return "SystemLog [model=" + model + ", url=" + url + ", username=" + username
                + ", content=" + content + ", requestTime=" + requestTime + ", ip=" + ip
                + ", systemCode=" + systemCode + "]";
    }
    

}
