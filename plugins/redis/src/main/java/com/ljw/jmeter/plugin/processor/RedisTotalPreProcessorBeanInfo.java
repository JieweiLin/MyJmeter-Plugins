package com.ljw.jmeter.plugin.processor;

import com.ljw.jmeter.plugin.common.GetMode;
import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Protocol;

import java.beans.PropertyDescriptor;

/**
 * @author 林杰炜 linjw
 *  TODO 类描述
 * 
 *
 * @date 2019/5/23 16:22
 */
public class RedisTotalPreProcessorBeanInfo extends BeanInfoSupport {
    private static final Logger log = LoggerFactory.getLogger(RedisTotalPreProcessorBeanInfo.class);
    private static final String VARIABLE_NAMES = "variableNames";
    private static final String DELIMITER = "delimiter";
    private static final String REDIS_KEY = "redisKey";
    private static final String PARAMETER = "parameter";
    private static final String GET_MODE = "getMode";
    private static final String HOST = "host";
    private static final String PORT = "port";
    private static final String TIMEOUT = "timeout";
    private static final String PASSWORD = "password";
    private static final String DATABASE = "database";
    private static final String MAX_ACTIVE = "maxActive";
    private static final String MAX_IDLE = "maxIdle";
    private static final String MAX_WAIT = "maxWait";
    private static final String MIN_IDLE = "minIdle";
    private static final String WHEN_EXHAUSTED_ACTION = "whenExhaustedAction";
    private static final String TEST_WHILE_IDLE = "testWhileIdle";
    private static final String TEST_ON_BORROW = "testOnBorrow";
    private static final String TEST_ON_RETURN = "testOnReturn";
    private static final String TIME_BETWEEN_EVICTION_RUNS_MS = "timeBetweenEvictionRunsMillis";
    private static final String SOFT_MIN_EVICTABLE_IDLE_TIME_MS = "softMinEvictableIdleTimeMillis";
    private static final String NUM_TEST_PER_EVICTION_RUN = "numTestsPerEvictionRun";
    private static final String MIN_EVICTABLE_IDLE_TIME_MS = "minEvictableIdleTimeMillis";

    public RedisTotalPreProcessorBeanInfo() {
        super(RedisTotalPreProcessor.class);
        try {
            createPropertyGroup("redis_data", new String[]{REDIS_KEY, VARIABLE_NAMES, DELIMITER, GET_MODE, PARAMETER});
            PropertyDescriptor p = property(REDIS_KEY);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(VARIABLE_NAMES);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(DELIMITER);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, ",");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(GET_MODE, GetMode.class);
            p.setValue(DEFAULT, GetMode.HGETALL.ordinal());
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);

            p = property(PARAMETER);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            createPropertyGroup("redis_connection",
                    new String[] { HOST, PORT, TIMEOUT, PASSWORD, DATABASE });

            p = property(HOST);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(PORT);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, Protocol.DEFAULT_PORT);
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(TIMEOUT);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, Protocol.DEFAULT_TIMEOUT);
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(PASSWORD);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(DATABASE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, Protocol.DEFAULT_DATABASE);
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            createPropertyGroup("pool_config",
                    new String[] { MIN_IDLE, MAX_IDLE, MAX_ACTIVE, MAX_WAIT, WHEN_EXHAUSTED_ACTION,
                            TEST_ON_BORROW, TEST_ON_RETURN, TEST_WHILE_IDLE, TIME_BETWEEN_EVICTION_RUNS_MS,
                            NUM_TEST_PER_EVICTION_RUN, MIN_EVICTABLE_IDLE_TIME_MS, SOFT_MIN_EVICTABLE_IDLE_TIME_MS});

            p = property(MIN_IDLE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "0");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(MAX_IDLE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "10");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(MAX_ACTIVE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "20");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(MAX_WAIT);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "30000");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(WHEN_EXHAUSTED_ACTION, RedisTotalPreProcessor.WhenExhaustedAction.class);
            p.setValue(DEFAULT, RedisTotalPreProcessor.WhenExhaustedAction.GROW.ordinal());
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);

            p = property(TEST_ON_BORROW);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, Boolean.FALSE);
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
            p.setValue(NOT_OTHER, Boolean.TRUE);

            p = property(TEST_ON_RETURN);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, Boolean.FALSE);
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
            p.setValue(NOT_OTHER, Boolean.TRUE);

            p = property(TEST_WHILE_IDLE);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, Boolean.FALSE);
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
            p.setValue(NOT_OTHER, Boolean.TRUE);


            p = property(TIME_BETWEEN_EVICTION_RUNS_MS);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "30000");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(NUM_TEST_PER_EVICTION_RUN);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "0");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(MIN_EVICTABLE_IDLE_TIME_MS);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "60000");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);

            p = property(SOFT_MIN_EVICTABLE_IDLE_TIME_MS);
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "60000");
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
        } catch (Exception e) {
            log.error("init Redis Pre Processor BeanInfo error! ", e);
        }
    }
}
