package com.ljw.jmeter.plugin.processor;

import com.ljw.jmeter.plugin.common.GetMode;
import com.ljw.jmeter.plugin.config.RedisConfig;
import net.sf.json.util.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.processor.PreProcessor;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testbeans.gui.GenericTestBeanCustomizer;
import org.apache.jmeter.testelement.AbstractScopedTestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.util.JMeterStopThreadException;
import org.apache.jorphan.util.JOrphanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.io.Serializable;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author 林杰炜 linjw
 * @Title TODO 类描述
 * @Description TODO 详细描述
 * @Copyright 2014-现在 厦门神州鹰掌通家园项目组
 * @date 2019/5/23 16:38
 */
public class RedisTotalPreProcessor extends AbstractScopedTestElement implements TestBean, PreProcessor, Serializable, TestStateListener {
    private static final Logger log = LoggerFactory.getLogger(RedisTotalPreProcessor.class);

    private String host;
    private String port;
    private String timeout;
    private String password;
    private String database;
    private String redisKey;
    private String variableNames;
    private String delimiter;
    private GetMode getMode;
    private String parameter;
    private int maxIdle;
    private int minIdle;
    private int maxActive;
    private long maxWait;
    private WhenExhaustedAction whenExhaustedAction;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private boolean testWhileIdle;
    private long timeBetweenEvictionRunsMillis;
    private int numTestsPerEvictionRun;
    private long minEvictableIdleTimeMillis;
    private long softMinEvictableIdleTimeMillis;

    private transient JedisPool pool;

    @Override
    public void process() {
        final JMeterContext context = getThreadContext();
        SampleResult result = context.getPreviousResult();
        if (result == null) {
            return;
        }
        log.debug("redisTotalPreProcessor start...");
        JMeterVariables variable = context.getVariables();
        Jedis jedis = this.pool.getResource();
        try {
            String line = "";
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
                    variable.put(getVariableNames() + "_" + i, key);
                    i++;
                }
                variable.put(getVariableNames() + "_N", String.valueOf(i-1));
                line = keys.toString();
            } else if (GetMode.TYPE.equals(getMode)){
                line = jedis.type(redisKey);
            } else if (GetMode.SMEMBERS.equals(getMode)){
                Set<String> set = jedis.smembers(redisKey);
                int i = 1;
                for (String key : set) {
                    variable.put(getVariableNames() + "_" + i, key);
                    i++;
                }
                variable.put(getVariableNames() + "_N", String.valueOf(i-1));
                line = set.toString();
            } else if (GetMode.SISMEMBER.equals(getMode)){
                Boolean b = jedis.sismember(redisKey, parameter);
                variable.put(getVariableNames(), String.valueOf(b));
                line = String.valueOf(b);
            }
            if (StringUtils.isBlank(line)){
                throw new JMeterStopThreadException("End of redis data");
            }
            if (!GetMode.KEYS.equals(getMode) || !GetMode.SMEMBERS.equals(getMode)){
                String[] vars = JOrphanUtils.split(getVariableNames(), ",");
                String[] values = {};
                if (StringUtils.isNotBlank(delimiter)){
                    values = JOrphanUtils.split(line, delimiter, false);
                }
                if (values.length > 0) {
                    for (int i = 0; i < vars.length && i < values.length; i++) {
                        variable.put(vars[i], values[i]);
                    }
                } else {
                    variable.put(vars[0], "null".equals(line) ? "" : line);
                }
            }
            if (result != null){
                result.setSuccessful(true);
            }
        } catch (Exception e) {
            log.error("redis operation failed", e);
            if (result != null){
                result.setSuccessful(false);
            }
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
    }

    @Override
    public void testStarted() {
        testStarted("");
    }

    @Override
    public void testStarted(String host) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(getMaxActive());
        config.setMaxWaitMillis(getMaxWait());
        config.setMaxIdle(getMaxIdle());
        config.setMinIdle(getMinIdle());
        config.setTestOnBorrow(isTestOnBorrow());
        config.setTestOnCreate(false);
        config.setTestWhileIdle(isTestWhileIdle());
        config.setTimeBetweenEvictionRunsMillis(getTimeBetweenEvictionRunsMillis());
        int port = Protocol.DEFAULT_PORT;
        if (StringUtils.isNotBlank(getPort())) {
            port = Integer.parseInt(getPort());
        }
        int timeout = Protocol.DEFAULT_TIMEOUT;
        if (StringUtils.isNotBlank(getTimeout())) {
            timeout = Integer.parseInt(getTimeout());
        }
        int database = Protocol.DEFAULT_DATABASE;
        if (StringUtils.isNotBlank(getDatabase())) {
            database = Integer.parseInt(getDatabase());
        }
        String password = null;
        if (StringUtils.isNotBlank(getPassword())) {
            password = getPassword();
        }
        this.pool = new JedisPool(config, getHost(), port, timeout, password, database);
    }

    @Override
    public void testEnded() {
        testEnded("");
    }

    @Override
    public void testEnded(String host) {
        this.pool.destroy();
    }

    @Override
    public Object clone() {
        RedisTotalPreProcessor clonedElement = (RedisTotalPreProcessor) super.clone();
        clonedElement.pool = this.pool;
        return clonedElement;
    }

    @Override
    public void setProperty(JMeterProperty property) {
        if (property instanceof StringProperty) {
            final String pn = property.getName();
            if ("whenExhaustedAction".equals(pn)) {
                final Object obj = property.getObjectValue();
                try {
                    final BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
                    final ResourceBundle rb = (ResourceBundle) beanInfo.getBeanDescriptor().getValue(GenericTestBeanCustomizer.RESOURCE_BUNDLE);
                    for (Enum<RedisConfig.WhenExhaustedAction> e : RedisConfig.WhenExhaustedAction.values()) {
                        final String propName = e.toString();
                        if (obj.equals(rb.getObject(propName))) {
                            final int tmpMode = e.ordinal();
                            if (log.isDebugEnabled()) {
                                log.debug("Converted {} = {} to mode = {} using Locale: {}", pn, obj, tmpMode, rb.getLocale());
                            }
                            super.setProperty(pn, tmpMode);
                            return;
                        }
                    }
                } catch (IntrospectionException e) {
                    log.error("Could not find BeanInfo", e);
                }
            } else if ("getMode".equals(pn)) {
                final Object obj = property.getObjectValue();
                try {
                    final BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
                    final ResourceBundle rb = (ResourceBundle) beanInfo.getBeanDescriptor().getValue(GenericTestBeanCustomizer.RESOURCE_BUNDLE);
                    for (Enum<GetMode> e : GetMode.values()) {
                        final String propName = e.toString();
                        if (obj.equals(rb.getObject(propName))) {
                            final int tmp = e.ordinal();
                            if (log.isDebugEnabled()) {
                                log.debug("Converted {} = {} to mode = {} using Locale: {} ", pn, obj, tmp, rb.getLocale());
                            }
                            super.setProperty(pn, tmp);
                            return;
                        }
                    }
                    log.warn("Could not convert {} = {} using Locale: ", pn, obj, rb.getLocale());
                } catch (IntrospectionException e) {
                    log.error("Could not find BeanInfo", e);
                }
            }
        }
        super.setProperty(property);
    }

    public enum WhenExhaustedAction {
        FAIL((byte) 0),
        BLOCK((byte) 1),
        GROW((byte) 2);

        private byte value;

        WhenExhaustedAction(byte value) {
            this.value = value;
        }

        public byte getValue() {
            return value;
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public String getVariableNames() {
        return variableNames;
    }

    public void setVariableNames(String variableNames) {
        this.variableNames = variableNames;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public int getGetMode() {
        return getMode.ordinal();
    }

    public void setGetMode(int getMode) {
        this.getMode = GetMode.values()[getMode];
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    public int getWhenExhaustedAction() {
        return whenExhaustedAction.ordinal();
    }

    public void setWhenExhaustedAction(int whenExhaustedAction) {
        this.whenExhaustedAction = WhenExhaustedAction.values()[whenExhaustedAction];
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public int getNumTestsPerEvictionRun() {
        return numTestsPerEvictionRun;
    }

    public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }

    public long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public long getSoftMinEvictableIdleTimeMillis() {
        return softMinEvictableIdleTimeMillis;
    }

    public void setSoftMinEvictableIdleTimeMillis(long softMinEvictableIdleTimeMillis) {
        this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
    }
}
