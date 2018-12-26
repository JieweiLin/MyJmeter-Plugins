package com.ljw.jmeter.plugin.ons;

import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.engine.util.NoConfigMerge;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Properties;

/**
 * @author 林杰炜 linjw
 * @Title Ons配置元件
 * @Description Ons配置元件
 * @date 2018/12/6 10:54
 */
public class OnsConfig extends ConfigTestElement implements TestBean, LoopIterationListener, NoConfigMerge, TestStateListener {
    private static final Logger log = LoggerFactory.getLogger(OnsConfig.class);

    private String variableName;
    private String accessKey;
    private String secretKey;
    private String sendMsgTimeout;
    private String onsAddr;

    @Override
    public void iterationStart(LoopIterationEvent iterEvent) {
        try {
            final JMeterContext context = getThreadContext();
            JMeterVariables variables = context.getVariables();
            if (Objects.isNull(variables.getObject(variableName))){
                if (log.isDebugEnabled()){
                    log.debug("init OnsConfig!");
                }
                Properties properties = new Properties();
                properties.put(PropertyKeyConst.AccessKey, getAccessKey());
                properties.put(PropertyKeyConst.SecretKey, getSecretKey());
                properties.put(PropertyKeyConst.SendMsgTimeoutMillis, getSendMsgTimeout());
                properties.put(PropertyKeyConst.ONSAddr, getOnsAddr());
                Producer producer = ONSFactory.createProducer(properties);
                producer.start();
                variables.putObject(variableName, producer);
            }
        } catch (Exception e) {
            log.error("init OnsConfig error!", e);
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

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSendMsgTimeout() {
        return sendMsgTimeout;
    }

    public void setSendMsgTimeout(String sendMsgTimeout) {
        this.sendMsgTimeout = sendMsgTimeout;
    }

    public String getOnsAddr() {
        return onsAddr;
    }

    public void setOnsAddr(String onsAddr) {
        this.onsAddr = onsAddr;
    }
}
