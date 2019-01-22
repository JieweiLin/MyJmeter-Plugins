package com.ljw.jmeter.plugin.encoded.config;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.ljw.jmeter.plugin.common.AlgorithmEnum;
import com.ljw.jmeter.plugin.common.TokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.engine.util.NoConfigMerge;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 林杰炜 linjw
 * @Title token编码配置元件
 * @Description token编码配置元件
 * @date 2019/1/19 17:07
 */
public class TokenEncodedConfig extends ConfigTestElement implements TestBean, LoopIterationListener, NoConfigMerge, TestStateListener {
    private static final Logger log = LoggerFactory.getLogger(TokenEncodedConfig.class);
    private String variableName;
    private String data;
    private String tokenSecret;
    private AlgorithmEnum algorithm;

    @Override
    public void iterationStart(LoopIterationEvent loopIterationEvent) {
        JMeterVariables variables = getThreadContext().getVariables();
        if (StringUtils.isBlank(variables.get(variableName))) {
            if (log.isDebugEnabled()){
                log.debug("data:{}, tokenSecret:{}", data, tokenSecret);
            }
            try {
                String token = TokenUtils.createToken(data, tokenSecret, algorithm);
                if (StringUtils.isNotBlank(variableName)) {
                    variables.put(variableName.trim(), token);
                }
            } catch (Exception e) {
                log.error("init Token error, data:{}, tokenSecret:{}", data, tokenSecret, e);
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

    /*@Override
    public void setProperty(JMeterProperty property) {
        if (property instanceof StringProperty) {
            final String pn = property.getName();
            if ("algorithm".equals(pn)) {
                final Object obj = property.getObjectValue();
                try {
                    final BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
                    final ResourceBundle rb = (ResourceBundle) beanInfo.getBeanDescriptor().getValue(GenericTestBeanCustomizer.RESOURCE_BUNDLE);
                    for (Enum<AlgorithmEnum> e : AlgorithmEnum.values()) {
                        final String propName = e.toString();
                        if (obj.equals(rb.getObject(propName))) {
                            final int tmpMode = e.ordinal();
                            if (log.isDebugEnabled()) {
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
    }*/

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
