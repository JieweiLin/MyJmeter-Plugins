package com.ljw.jmeter.plugin.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 林杰炜 linjw
 * @Title TODO 类描述
 * @Description TODO 详细描述
 * @Copyright 2014-现在 厦门神州鹰掌通家园项目组
 * @date 2019/1/19 17:32
 */
public class JsonUtils {
    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    public static String getJson(Object obj) {
        return JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm:ss", SerializerFeature.DisableCircularReferenceDetect);
    }

    public static <T> T readValue(String json, Class<? extends T> cls) {
        try {
            return JSON.parseObject(json, cls);
        } catch (Exception e) {
            log.error("json to class[{}] is error!, json:[{}]", cls.getName(), json, e);
        }
        return null;
    }
}
