package com.ljw.jmeter.plugin.encoded.sampler;

import com.ljw.jmeter.plugin.common.AlgorithmEnum;
import com.ljw.jmeter.plugin.common.TokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * @author 林杰炜 linjw
 *  token编码采样器
 * @Description token编码采样器
 * @date 2019/1/21 15:25
 */
public class TokenEncodedSampler extends AbstractSampler implements TestBean, Serializable, TestStateListener {
    private static final Logger log = LoggerFactory.getLogger(TokenEncodedSampler.class);
    private String variableName;
    private String data;
    private String tokenSecret;
    private AlgorithmEnum algorithm;

    @Override
    public String toString() {
        return "TokenEncodedSampler{" +
                "variableName='" + variableName + '\'' +
                ", data='" + data + '\'' +
                ", tokenSecret='" + tokenSecret + '\'' +
                ", algorithm=" + algorithm +
                '}';
    }

    @Override
    public SampleResult sample(Entry entry) {
        SampleResult result = new SampleResult();
        result.setSampleLabel(getName());
        result.sampleStart();
        result.setSamplerData(toString());
        result.setSuccessful(true);
        final JMeterVariables variables = getThreadContext().getVariables();
        String token = null;
        try {
            token = TokenUtils.createToken(data, tokenSecret, algorithm);
            result.setResponseData(token.getBytes(Charset.forName("utf-8")));
        } catch (Exception e) {
            log.error("createToken error!", e);
            result.setSuccessful(false);
        }
        if (StringUtils.isNotBlank(variableName) && StringUtils.isNotBlank(token)) {
            variables.put(variableName, token);
        }
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

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public int getAlgorithm() {
        return algorithm.ordinal();
    }

    public void setAlgorithm(int algorithm) {
        this.algorithm = AlgorithmEnum.values()[algorithm];
    }
}
