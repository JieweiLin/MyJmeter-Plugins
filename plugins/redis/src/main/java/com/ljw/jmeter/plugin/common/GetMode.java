package com.ljw.jmeter.plugin.common;

/**
 * @author 林杰炜 linjw
 * @Title TODO 类描述
 * @Description TODO 详细描述
 * @Copyright 2014-现在 厦门神州鹰掌通家园项目组
 * @date 2018/12/18 17:06
 */
public enum GetMode {

    HGETALL((byte) 0),
    TTL((byte) 1),
    EXPIRE((byte) 2),
    GET((byte) 3),
    DEL((byte) 4),
    HGET((byte) 5),
    EXISTS((byte) 6),
    SET((byte) 7),
    KEYS((byte) 8),
    TYPE((byte) 9),
    SMEMBERS((byte)10);
    private byte value;

    private GetMode(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
