package com.ljw.jmeter.plugin.memcache.sampler;

import net.spy.memcached.MemcachedClient;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author 林杰炜 linjw
 * @Title Memcache采样器
 * @Description Memcache采样器
 * @date 2018/11/29 18:46
 */
public class MemcacheSampler extends AbstractSampler implements TestBean, Serializable, TestStateListener {
    private static final Logger log = LoggerFactory.getLogger(MemcacheSampler.class);

    private String variableName;
    private String function;

    @Override
    public SampleResult sample(Entry e) {
        SampleResult result = new SampleResult();
        result.setSampleLabel(getName());
        result.sampleStart();
        final JMeterContext context = getThreadContext();
        JMeterVariables variables = context.getVariables();

        MemcachedClient client = (MemcachedClient) variables.getObject(variableName);


        result.setDataType(SampleResult.TEXT);
        result.setSamplerData(toString());
        result.setResponseOK();
        result.sampleEnd();
        return result;
    }

    @Override
    public void testStarted() {

    }

    @Override
    public void testStarted(String host) {

    }

    @Override
    public void testEnded() {

    }

    @Override
    public void testEnded(String host) {

    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }
}
