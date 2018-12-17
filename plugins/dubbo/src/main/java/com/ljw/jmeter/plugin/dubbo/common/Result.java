package com.ljw.jmeter.plugin.dubbo.common;

/**
 * @author 林杰炜 linjw
 * @Title dubbo结果集
 * @Description dubbo结果集
 * @date 2018/12/14 17:21
 */
public class Result {

    public int code;
    public Object body;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
