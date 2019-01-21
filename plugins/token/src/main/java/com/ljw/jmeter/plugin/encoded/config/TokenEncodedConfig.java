package com.ljw.jmeter.plugin.encoded.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.google.common.collect.Maps;
import com.ljw.jmeter.plugin.common.AlgorithmEnum;
import com.ljw.jmeter.plugin.common.JsonUtils;
import com.ljw.jmeter.plugin.common.Payload;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.engine.util.NoConfigMerge;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testbeans.gui.GenericTestBeanCustomizer;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

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
    private static Map<Byte, String> algorithmMap = Maps.newHashMap();

    static {
        algorithmMap.put((byte) 0, "HS256");
        algorithmMap.put((byte) 1, "HS384");
        algorithmMap.put((byte) 2, "HS512");
        algorithmMap.put((byte) 3, "RS256");
        algorithmMap.put((byte) 4, "RS384");
        algorithmMap.put((byte) 5, "RS512");
        algorithmMap.put((byte) 6, "ES256");
        algorithmMap.put((byte) 7, "ES384");
        algorithmMap.put((byte) 8, "ES512");
        algorithmMap.put((byte) 9, "PS256");
        algorithmMap.put((byte) 10, "PS384");
        algorithmMap.put((byte) 11, "PS512");
    }

    @Override
    public void iterationStart(LoopIterationEvent loopIterationEvent) {
        JMeterVariables variables = getThreadContext().getVariables();
        if (StringUtils.isBlank(variables.get(variableName))) {
            try {
                Payload payload = JsonUtils.readValue(data, Payload.class);

                Map<String, Object> headerClaims = Maps.newHashMap();
                headerClaims.put("alg", algorithmMap.get(algorithm.getValue()));
                headerClaims.put("typ", "JWT");

                String token = JWT.create()
                        .withHeader(headerClaims)
                        .withIssuer("szy")
                        .withClaim("id", payload.getId())
                        .withClaim("userId", payload.getUserId())
                        .withClaim("userType", payload.getUserType())
                        .withClaim("devType", payload.getDevType())
                        .withClaim("appType", payload.getAppType())
                        .withClaim("sessionId", payload.getSessionId())
                        .withClaim("isOperation", payload.isOperation())
                        .withClaim("iat", new Date(payload.getIat() * 1000))
                        .withClaim("exp", new Date(payload.getExp() * 1000))
                        .withClaim("tokenType", (int) payload.getTokenType())
                        .sign(getAlgoritm());
                if (StringUtils.isNotBlank(variableName)) {
                    variables.put(variableName.trim(), token);
                }
            } catch (IllegalArgumentException e) {
                log.error("init Token error", e);
            } catch (JWTCreationException e) {
                log.error("init Token error", e);
            }
        }
    }

    private Algorithm getAlgoritm() {
        if (AlgorithmEnum.HS256.equals(algorithm)) {
            return Algorithm.HMAC256(tokenSecret);
        } else if (AlgorithmEnum.HS384.equals(algorithm)) {
            return Algorithm.HMAC384(tokenSecret);
        } else if (AlgorithmEnum.HS512.equals(algorithm)) {
            return Algorithm.HMAC512(tokenSecret);
        }
        return null;
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
