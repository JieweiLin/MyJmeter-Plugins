package com.ljw.jmeter.plugin.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.engine.util.NoConfigMerge;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testbeans.gui.GenericTestBeanCustomizer;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author 林杰炜 linjw
 *  redis配置元件
 * @Description redis配置元件
 * @date 2018/12/17 14:13
 */
public class RedisConfig extends ConfigTestElement implements TestBean, LoopIterationListener, NoConfigMerge, TestStateListener {
    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    private String variableName;
    private String host;
    private String port;
    private String timeout;
    private String password;
    private String database;

    private int maxActive;
    private int maxIdle;
    private long maxWait;
    private int minIdle;
    private WhenExhaustedAction whenExhaustedAction;
    private boolean testWhileIdle;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private long timeBetweenEvictionRunsMillis;
    private long softMinEvictableIdleTimeMillis;
    private int numTestsPerEvictionRun;
    private long minEvictableIdleTimeMillis;

    private JedisPool pool;

    @Override
    public void iterationStart(LoopIterationEvent iterEvent) {
        final JMeterVariables variables = getThreadContext().getVariables();
        if (Objects.isNull(variables.getObject(variableName))){
            if (log.isDebugEnabled()){
                log.debug("init redis config");
            }
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(maxActive);
            config.setMaxIdle(maxIdle);
            config.setMinIdle(minIdle);
            config.setMaxWaitMillis(maxWait);
            config.setTestOnBorrow(testOnBorrow);
            config.setTestOnReturn(testOnReturn);
            config.setTestWhileIdle(testWhileIdle);
            config.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
            config.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
            config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
            config.setSoftMinEvictableIdleTimeMillis(softMinEvictableIdleTimeMillis);
            int port = Protocol.DEFAULT_PORT;
            if (StringUtils.isNotBlank(this.port)){
                port = Integer.parseInt(this.port);
            }
            int timeout = Protocol.DEFAULT_TIMEOUT;
            if (StringUtils.isNotBlank(this.timeout)){
                timeout = Integer.parseInt(this.timeout);
            }
            int database = Protocol.DEFAULT_DATABASE;
            if (StringUtils.isNotBlank(this.database)){
                database = Integer.parseInt(this.database);
            }
            String password = null;
            if (StringUtils.isNotBlank(this.password)){
                password = this.password;
            }
            this.pool = new JedisPool(config, this.host, port, timeout, password, database);
            if (pool != null){
                variables.putObject(variableName, this.pool);
            }else {
                log.error("init redis pool error");
            }
        }
    }

    @Override
    public void testStarted() {
        testStarted("");
    }

    @Override
    public void testStarted(String host) {

    }

    @Override
    public void testEnded() {
        testEnded("");
    }

    @Override
    public void testEnded(String host) {

    }

    @Override
    public void setProperty(JMeterProperty property) {
        if (property instanceof StringProperty){
            final String pn = property.getName();
            if ("whenExhaustedAction".equals(pn)){
                final Object obj = property.getObjectValue();
                try {
                    final BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
                    final ResourceBundle rb = (ResourceBundle) beanInfo.getBeanDescriptor().getValue(GenericTestBeanCustomizer.RESOURCE_BUNDLE);
                    for (Enum<WhenExhaustedAction> e : WhenExhaustedAction.values()) {
                        final String propName = e.toString();
                        if (obj.equals(rb.getObject(propName))){
                            final int tmpMode = e.ordinal();
                            if (log.isDebugEnabled()){
                                log.debug("Converted {} = {} to mode = {} using Locale: {}", pn, obj, tmpMode, rb.getLocale());
                            }
                            super.setProperty(pn, tmpMode);
                            return;
                        }
                    }
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

        private final byte value;

        private WhenExhaustedAction(byte value) {
            this.value = value;
        }

        public byte getValue() {
            return value;
        }
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
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

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getWhenExhaustedAction() {
        return whenExhaustedAction.ordinal();
    }

    public void setWhenExhaustedAction(int whenExhaustedAction) {
        this.whenExhaustedAction = WhenExhaustedAction.values()[whenExhaustedAction];
    }

    public boolean getTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public boolean getTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean getTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public long getSoftMinEvictableIdleTimeMillis() {
        return softMinEvictableIdleTimeMillis;
    }

    public void setSoftMinEvictableIdleTimeMillis(long softMinEvictableIdleTimeMillis) {
        this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
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
}
