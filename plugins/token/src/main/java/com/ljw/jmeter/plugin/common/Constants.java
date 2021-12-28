package com.ljw.jmeter.plugin.common;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author 林杰炜 linjw
 * @Title token常量类
 * @Description token常量类
 * @date 2019/1/21 15:45
 */
public class Constants {

    protected static final Map<Byte, String> algorithmMap = Maps.newHashMap();

    static {
        algorithmMap.put((byte) 0, "HS256");
        algorithmMap.put((byte) 1, "HS384");
        algorithmMap.put((byte) 2, "HS512");
        algorithmMap.put((byte) 3, "RS256");
        algorithmMap.put((byte) 4, "RS384");
        algorithmMap.put((byte) 5, "RS512");
        algorithmMap.put((byte) 6, "ES256");
        algorithmMap.put((byte) 7, "ES384");
        algorithmMap.put((byte) 8, "ES512");
        algorithmMap.put((byte) 9, "PS256");
        algorithmMap.put((byte) 10, "PS384");
        algorithmMap.put((byte) 11, "PS512");
    }
}
