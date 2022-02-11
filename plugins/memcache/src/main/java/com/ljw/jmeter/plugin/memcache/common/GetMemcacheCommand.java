package com.ljw.jmeter.plugin.memcache.common;

/**
 * @author 林杰炜 linjw
 *  Memcache命令枚举
 * @Description Memcache命令枚举
 * 
 * @date 2019/1/17 16:07
 */
public enum GetMemcacheCommand {

    /**
     * GET
     */
    GET((byte) 0),
    SET((byte) 1),
    ADD((byte) 2),
    REPLACE((byte) 3),
    DELETE((byte) 4);

    private final byte value;

    private GetMemcacheCommand(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
