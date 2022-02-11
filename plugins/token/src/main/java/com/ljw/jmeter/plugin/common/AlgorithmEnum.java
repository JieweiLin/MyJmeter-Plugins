package com.ljw.jmeter.plugin.common;

/**
 * @author 林杰炜 linjw
 *  算法枚举
 * @Description 算法枚举
 * @date 2019/1/19 17:20
 */
public enum AlgorithmEnum {
    /**
     * HS256
     */
    HS256((byte) 0),
    HS384((byte) 1),
    HS512((byte) 2),
    RS256((byte) 3),
    RS384((byte) 4),
    RS512((byte) 5),
    ES256((byte) 6),
    ES384((byte) 7),
    ES512((byte) 8),
    PS256((byte) 9),
    PS384((byte) 10),
    PS512((byte) 11);

    private final byte value;

    private AlgorithmEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
