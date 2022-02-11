package com.ljw.jmeter.plugin.common;

/**
 * @author 林杰炜 linjw
 *  redis命令
 * @Description redis命令
 * 
 * @date 2018/12/18 17:06
 */
public enum GetMode {

    /**
     * hgetAll
     */
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
    SMEMBERS((byte) 10),
    SISMEMBER((byte) 11);
    private final byte value;

    private GetMode(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
