package com.ljw.jmeter.plugin.common;

import net.sf.json.util.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.util.JMeterStopThreadException;
import org.apache.jorphan.util.JOrphanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 林杰炜 linjw
 * @Title redis工具类
 * @Description redis工具类
 * @Copyright 2014-现在 厦门神州鹰掌通家园项目组
 * @date 2018/12/18 17:07
 */
public class RedisUtils {
    private static Logger log = LoggerFactory.getLogger(RedisUtils.class);

    public static String callBack(SampleResult result, JMeterVariables variables, String poolVariableName, String database, GetMode getMode, String redisKey, String parameter, String resultVariableNames, String delimiter){
        JedisPool pool = (JedisPool) variables.getObject(poolVariableName);
        Jedis jedis = pool.getResource();
        if (StringUtils.isNotBlank(database)){
            jedis.select(Integer.parseInt(database));
        }
        String line = "";
        try {
            if (GetMode.HGETALL.equals(getMode)){
                line = JSONUtils.valueToString(jedis.hgetAll(redisKey));
            } else if (GetMode.TTL.equals(getMode)){
                line = String.valueOf(jedis.ttl(redisKey));
            } else if (GetMode.EXPIRE.equals(getMode)){
                line = String.valueOf(jedis.expire(redisKey, Integer.parseInt(parameter)));
            } else if (GetMode.GET.equals(getMode)){
                line = String.valueOf(jedis.get(redisKey));
            } else if (GetMode.DEL.equals(getMode)){
                line = String.valueOf(jedis.del(redisKey));
            } else if (GetMode.HGET.equals(getMode)){
                line = String.valueOf(jedis.hget(redisKey, parameter));
            } else if (GetMode.EXISTS.equals(getMode)){
                line = String.valueOf(jedis.exists(redisKey));
            } else if (GetMode.SET.equals(getMode)){
                line = String.valueOf(jedis.set(redisKey, parameter));
            } else if (GetMode.KEYS.equals(getMode)){
                Set<String> keys = jedis.keys(redisKey);
                int i = 1;
                for (String key : keys) {
                    variables.put(resultVariableNames + "_" + i, key);
                    i++;
                }
                variables.put(resultVariableNames + "_N", String.valueOf(i-1));
                line = keys.toString();
                return line;
            } else if (GetMode.TYPE.equals(getMode)){
                line = jedis.type(redisKey);
            } else if (GetMode.SMEMBERS.equals(getMode)){
                Set<String> set = jedis.smembers(redisKey);
                int i = 1;
                for (String key : set) {
                    variables.put(resultVariableNames + "_" + i, key);
                    i++;
                }
                variables.put(resultVariableNames + "_N", String.valueOf(i-1));
                line = set.toString();
                return line;
            } else if (GetMode.SISMEMBER.equals(getMode)){
                Boolean b = jedis.sismember(redisKey, parameter);
                variables.put(resultVariableNames, String.valueOf(b));
                line = String.valueOf(b);
                return line;
            }
            if (StringUtils.isBlank(line)){
                throw new JMeterStopThreadException("End of redis data");
            }
            if (!GetMode.KEYS.equals(getMode) || !GetMode.SMEMBERS.equals(getMode)){
                String[] vars = JOrphanUtils.split(resultVariableNames, ",");
                String[] values = {};
                if (StringUtils.isNotBlank(delimiter)){
                    values = JOrphanUtils.split(line, delimiter, false);
                }
                if (values.length > 0) {
                    for (int i = 0; i < vars.length && i < values.length; i++) {
                        variables.put(vars[i], values[i]);
                    }
                } else {
                    variables.put(vars[0], line);
                }
            }
            if (result != null){
                result.setSuccessful(true);
            }
        } catch (Exception e) {
            line = "redis operation failed";
            log.error("redis operation failed", e);
            if (result != null){
                result.setSuccessful(false);
            }
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return line;
    }
}
