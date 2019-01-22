package com.ljw.jmeter.plugin.decoded.config;

import com.ljw.jmeter.plugin.common.TokenUtils;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.engine.util.NoConfigMerge;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author 林杰炜 linjw
 * @Title Token解码配置元件
 * @Description Token解码配置元件
 * @date 2019/1/21 10:36
 */
public class TokenDecodedConfig extends ConfigTestElement implements TestBean, LoopIterationListener, NoConfigMerge, TestStateListener {
    private static final Logger log = LoggerFactory.getLogger(TokenDecodedConfig.class);
    private String variableName;
    private String token;

    @Override
    public void iterationStart(LoopIterationEvent iterEvent) {
        JMeterVariables variables = getThreadContext().getVariables();
        if (Objects.isNull(variables.getObject(variableName))) {
            TokenUtils.decodedToken(variables, null, variableName, token);
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
