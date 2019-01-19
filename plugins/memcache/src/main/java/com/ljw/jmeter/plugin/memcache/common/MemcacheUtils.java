package com.ljw.jmeter.plugin.memcache.common;

import net.spy.memcached.MemcachedClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * @author 林杰炜 linjw
 * @Title Memcache工具类
 * @Description Memcache工具类
 * @Copyright 2014-现在 厦门神州鹰掌通家园项目组
 * @date 2019/1/17 16:38
 */
public class MemcacheUtils {
    private static Logger log = LoggerFactory.getLogger(MemcacheUtils.class);

    public static String callBack(SampleResult result, JMeterVariables variables, String variableName, GetMemcacheCommand mode, String memcacheKey, String memcacheValue, String resultVariableName) {
        MemcachedClient client = (MemcachedClient) variables.getObject(variableName);
        String line = "";
        try {
            if (GetMemcacheCommand.GET.equals(mode)) {
                line = String.valueOf(client.get(memcacheKey));
            } else if (GetMemcacheCommand.ADD.equals(mode)) {
                line = String.valueOf(client.add(memcacheKey, 0, memcacheValue).get());
            } else if (GetMemcacheCommand.SET.equals(mode)) {
                line = String.valueOf(client.set(memcacheKey, 0, memcacheValue).get());
            } else if (GetMemcacheCommand.REPLACE.equals(mode)) {
                line = String.valueOf(client.replace(memcacheKey, 0, memcacheValue).get());
            } else if (GetMemcacheCommand.DELETE.equals(mode)){
                line = String.valueOf(client.delete(memcacheKey).get());
            }
            if (StringUtils.isNotBlank(resultVariableName)){
                variables.put(resultVariableName, line);
            }
        } catch (InterruptedException e) {
            line = "memcache operation failed";
            log.error("memcache operation failed", e);
            if (result != null){
                result.setSuccessful(false);
            }
        } catch (ExecutionException e) {
            line = "memcache operation failed";
            log.error("memcache operation failed", e);
            if (result != null){
                result.setSuccessful(false);
            }
        }
        return line;
    }
}
