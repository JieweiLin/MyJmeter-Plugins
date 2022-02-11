package com.ljw.jmeter.plugin.decoded.sampler;

import com.ljw.jmeter.plugin.common.JsonUtils;
import com.ljw.jmeter.plugin.common.Payload;
import com.ljw.jmeter.plugin.common.TokenUtils;
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
import java.util.Objects;

/**
 * @author 林杰炜 linjw
 *  token解码采样器
 * @Description token解码采样器
 * @date 2019/1/21 16:58
 */
public class TokenDecodedSampler extends AbstractSampler implements TestBean, Serializable, TestStateListener {
    private static final Logger log = LoggerFactory.getLogger(TokenDecodedSampler.class);
    private String variableName;
    private String token;

    @Override
    public String toString() {
        return "TokenDecodedSampler{" +
                "variableName='" + variableName + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    @Override
    public SampleResult sample(Entry e) {
        SampleResult result = new SampleResult();
        result.setSampleLabel(getName());
        result.sampleStart();
        result.setSamplerData(toString());
        result.setSuccessful(true);
        final JMeterVariables variables = getThreadContext().getVariables();
        Payload payload = TokenUtils.decodedToken(variables, result, variableName, token);
        if (Objects.nonNull(payload)) {
            result.setResponseData(JsonUtils.getJson(payload).getBytes(Charset.forName("utf-8")));
        } else {
            result.setResponseData("token decoded error".getBytes(Charset.forName("utf-8")));
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
