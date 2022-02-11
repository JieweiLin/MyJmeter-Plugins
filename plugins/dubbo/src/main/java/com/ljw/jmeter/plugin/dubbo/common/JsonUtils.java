package com.ljw.jmeter.plugin.dubbo.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author 林杰炜 linjw
 *  Json工具类
 * @Description Json工具类
 * @date 2018/12/14 17:07
 */
public class JsonUtils {
    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    private static final Gson GSON = new GsonBuilder().serializeNulls().create();

    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    public static String toJson(Object obj, Type type) {
        return GSON.toJson(obj, type);
    }

    public static <T> T fromJson(String json, Class<T> tClass) {
        try {
            return GSON.fromJson(json, tClass);
        } catch (JsonSyntaxException e) {
            log.error("json to class[{}] is error!", tClass.getName(), e);
        }
        return null;
    }

    public static <T> T fromJson(String json, Type type) {
        try {
            return GSON.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            log.error("json to class[{}] is error!", type.getClass().getName(), e);
        }
        return null;
    }

    public static String getJson(Object obj) {
        return JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm:ss", SerializerFeature.DisableCircularReferenceDetect);
    }

    public static <T> T readValue(String json, Class<? extends T> cls) {
        try {
            return JSON.parseObject(json, cls);
        } catch (Exception e) {
            log.error("json to class[{}] is error!", cls.getName(), e);
        }
        return null;
    }

    public static <T> T readValue(String json, Type type) {
        try {
            return JSON.parseObject(json, type, null);
        } catch (Exception e) {
            log.error("json to class[{}] is error!", type.getClass().getName(), e);
        }
        return null;
    }

    public static <T> List<? extends T> readValueToList(String json, Class<? extends T> cls) {
        try {
            return JSON.parseArray(json, cls);
        } catch (Exception e) {
            log.error("json to List<{}> is error!", cls.getName(), e);
        }
        return null;
    }
}
