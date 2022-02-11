package com.ljw.jmeter.plugin.common;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author 林杰炜 linjw
 *  token常量类
 * @Description token常量类
 * @date 2019/1/21 15:45
 */
public class Constants {

    protected static final Map<Byte, String> ALGORITHM_MAP = Maps.newHashMap();

    static {
        ALGORITHM_MAP.put((byte) 0, "HS256");
        ALGORITHM_MAP.put((byte) 1, "HS384");
        ALGORITHM_MAP.put((byte) 2, "HS512");
        ALGORITHM_MAP.put((byte) 3, "RS256");
        ALGORITHM_MAP.put((byte) 4, "RS384");
        ALGORITHM_MAP.put((byte) 5, "RS512");
        ALGORITHM_MAP.put((byte) 6, "ES256");
        ALGORITHM_MAP.put((byte) 7, "ES384");
        ALGORITHM_MAP.put((byte) 8, "ES512");
        ALGORITHM_MAP.put((byte) 9, "PS256");
        ALGORITHM_MAP.put((byte) 10, "PS384");
        ALGORITHM_MAP.put((byte) 11, "PS512");
    }
}
