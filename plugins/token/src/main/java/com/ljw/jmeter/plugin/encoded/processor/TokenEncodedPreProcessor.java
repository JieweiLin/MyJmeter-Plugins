package com.ljw.jmeter.plugin.encoded.processor;

import com.ljw.jmeter.plugin.common.AlgorithmEnum;
import com.ljw.jmeter.plugin.common.TokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.processor.PreProcessor;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.AbstractScopedTestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author 林杰炜 linjw
 *  token编码前置处理器
 * @Description token编码前置处理器
 * @date 2019/1/22 16:32
 */
public class TokenEncodedPreProcessor extends AbstractScopedTestElement implements TestBean, PreProcessor, Serializable, TestStateListener {
    private static final Logger log = LoggerFactory.getLogger(TokenEncodedPreProcessor.class);
    private String variableName;
    private String data;
    private String tokenSecret;
    private AlgorithmEnum algorithm;

    @Override
    public void process() {
        final JMeterVariables variables = getThreadContext().getVariables();
        String token  = null;
        try {
            token = TokenUtils.createToken(data, tokenSecret, algorithm);
        } catch (Exception e) {
            log.error("token encoded pre processor error! payload:{}", data, e);
        }
        if (StringUtils.isNotBlank(token)){
            variables.put(variableName.trim(), token);
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
