package com.ljw.jmeter.plugin.memcache.sampler;

import com.ljw.jmeter.plugin.memcache.common.GetMemcacheCommand;
import com.ljw.jmeter.plugin.memcache.common.MemcacheUtils;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testbeans.gui.GenericTestBeanCustomizer;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ResourceBundle;

/**
 * @author 林杰炜 linjw
 *  Memcache采样器
 * @Description Memcache采样器
 * @date 2018/11/29 18:46
 */
public class MemcacheSampler extends AbstractSampler implements TestBean, Serializable, TestStateListener {
    private static final Logger log = LoggerFactory.getLogger(MemcacheSampler.class);

    private String variableName;
    private GetMemcacheCommand getMemcacheCommand;
    private String memcacheKey;
    private String memcacheValue;
    private String resultVariableName;

    @Override
    public String toString() {
        return "MemcacheSampler{" +
                "variableName='" + variableName + '\'' +
                ", getMemcacheCommand=" + getMemcacheCommand +
                ", memcacheKey='" + memcacheKey + '\'' +
                ", memcacheValue='" + memcacheValue + '\'' +
                ", resultVariableName='" + resultVariableName + '\'' +
                '}';
    }

    @Override
    public SampleResult sample(Entry e) {
        SampleResult result = new SampleResult();
        result.setSampleLabel(getName());
        result.sampleStart();
        result.setSamplerData(toString());
        final JMeterContext context = getThreadContext();
        JMeterVariables variables = context.getVariables();
        result.setSuccessful(true);
        result.setResponseData(MemcacheUtils.callBack(result, variables, variableName, getMemcacheCommand, memcacheKey, memcacheValue, resultVariableName).getBytes(Charset.forName("utf-8")));
        result.setDataType(SampleResult.TEXT);
        result.setResponseOK();
        result.sampleEnd();
        return result;
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
            if ("getMemcacheCommand".equals(pn)) {
                final Object obj = property.getObjectValue();
                try {
                    final BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
                    final ResourceBundle rb = (ResourceBundle) beanInfo.getBeanDescriptor().getValue(GenericTestBeanCustomizer.RESOURCE_BUNDLE);
                    for (Enum<GetMemcacheCommand> e : GetMemcacheCommand.values()) {
                        final String propName = e.toString();
                        if (obj.equals(rb.getObject(propName))) {
                            final int tmp = e.ordinal();
                            if (log.isDebugEnabled()){
                                log.debug("Converted {} = {} to mode = {} using Local: {} ", pn, obj, tmp, rb.getLocale());
                            }
                            super.setProperty(pn, tmp);
                            return;
                        }
                    }
                    log.warn("Could not convert {} = {} using Locale: {}", pn, obj, rb.getLocale());
                } catch (IntrospectionException e) {
                    log.error("Could not find BeanInfo", e);
                }
            }
        }
        super.setProperty(property);
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public int getGetMemcacheCommand() {
        return getMemcacheCommand.ordinal();
    }

    public void setGetMemcacheCommand(int getMemcacheCommand) {
        this.getMemcacheCommand = GetMemcacheCommand.values()[getMemcacheCommand];
    }

    public String getMemcacheKey() {
        return memcacheKey;
    }

    public void setMemcacheKey(String memcacheKey) {
        this.memcacheKey = memcacheKey;
    }

    public String getMemcacheValue() {
        return memcacheValue;
    }

    public void setMemcacheValue(String memcacheValue) {
        this.memcacheValue = memcacheValue;
    }

    public String getResultVariableName() {
        return resultVariableName;
    }

    public void setResultVariableName(String resultVariableName) {
        this.resultVariableName = resultVariableName;
    }
}
