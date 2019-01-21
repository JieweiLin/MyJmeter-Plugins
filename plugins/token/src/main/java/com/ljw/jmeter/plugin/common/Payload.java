package com.ljw.jmeter.plugin.common;

/**
 * @author 林杰炜 linjw
 * @Title token中的payload实体
 * @Description token中的payload实体
 * @Copyright 2014-现在 厦门神州鹰掌通家园项目组
 * @date 2019/1/19 17:29
 */
public class Payload {

    private String id;
    private String userId;
    private String userType;
    private String devType;
    private String appType;
    private String sessionId;
    private boolean isOperation;
    private long iat;
    private long exp;
    private Byte tokenType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isOperation() {
        return isOperation;
    }

    public void setOperation(boolean operation) {
        isOperation = operation;
    }

    public long getIat() {
        return iat;
    }

    public void setIat(long iat) {
        this.iat = iat;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public Byte getTokenType() {
        return tokenType;
    }

    public void setTokenType(Byte tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public String toString() {
        return "Payload{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", userType='" + userType + '\'' +
                ", devType='" + devType + '\'' +
                ", appType='" + appType + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", isOperation=" + isOperation +
                ", iat=" + iat +
                ", exp=" + exp +
                ", tokenType=" + tokenType +
                '}';
    }
}
