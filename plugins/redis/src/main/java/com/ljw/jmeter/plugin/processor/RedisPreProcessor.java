package com.ljw.jmeter.plugin.processor;

import com.ljw.jmeter.plugin.common.GetMode;
import com.ljw.jmeter.plugin.common.RedisUtils;
import org.apache.jmeter.processor.PreProcessor;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testbeans.gui.GenericTestBeanCustomizer;
import org.apache.jmeter.testelement.AbstractScopedTestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.io.Serializable;
import java.util.ResourceBundle;

/**
 * @author 林杰炜 linjw
 *  redis前置处理器
 * @Description redis前置处理器
 * 
 * @date 2018/12/18 17:03
 */
public class RedisPreProcessor extends AbstractScopedTestElement implements TestBean, PreProcessor, Serializable, TestStateListener {
    private static final Logger log = LoggerFactory.getLogger(RedisPreProcessor.class);
    private String poolVariableName;
    private String database;
    private String redisKey;
    private String resultVariableNames;
    private String delimiter;
    private GetMode getMode;
    private String parameter;

    @Override
    public String toString() {
        return "RedisPreProcessor{" +
                "poolVariableName='" + poolVariableName + '\'' +
                ", database='" + database + '\'' +
                ", redisKey='" + redisKey + '\'' +
                ", resultVariableNames='" + resultVariableNames + '\'' +
                ", delimiter='" + delimiter + '\'' +
                ", getMode=" + getMode +
                ", parameter='" + parameter + '\'' +
                '}';
    }

    @Override
    public void process() {
        final JMeterVariables variables = getThreadContext().getVariables();
        RedisUtils.callBack(null, variables, poolVariableName, database, getMode, redisKey, parameter, resultVariableNames, delimiter);
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
        if (property instanceof StringProperty) {
            final String pn = property.getName();
            if ("getMode".equals(pn)) {
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

    public String getPoolVariableName() {
        return poolVariableName;
    }

    public void setPoolVariableName(String poolVariableName) {
        this.poolVariableName = poolVariableName;
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

    public String getResultVariableNames() {
        return resultVariableNames;
    }

    public void setResultVariableNames(String resultVariableNames) {
        this.resultVariableNames = resultVariableNames;
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
}
