package com.ljw.jmeter.plugin.decoded.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ljw.jmeter.plugin.common.Payload;
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

import java.util.Map;
import java.util.Objects;

/**
 * @author 林杰炜 linjw
 * @Title Token解码
 * @Description Token解码
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
            try {
                if (StringUtils.isNotBlank(token)) {
                    DecodedJWT jwt = JWT.decode(token);
                    Payload payload = new Payload();
                    payload.setId(jwt.getClaim("id").asString());
                    payload.setUserId(jwt.getClaim("userId").asString());
                    payload.setUserType(jwt.getClaim("userType").asString());
                    payload.setDevType(jwt.getClaim("devType").asString());
                    payload.setAppType(jwt.getClaim("appType").asString());
                    payload.setSessionId(jwt.getClaim("sessionId").asString());
                    payload.setOperation(jwt.getClaim("isOperation").asBoolean());
                    payload.setIat(jwt.getClaim("iat").asDate().getTime());
                    payload.setExp(jwt.getClaim("exp").asDate().getTime());
                    payload.setTokenType(jwt.getClaim("tokenType").asInt().byteValue());
                    if (StringUtils.isNotBlank(variableName)) {
                        variables.putObject(variableName.trim(), payload);
                        Map<String, Claim> map = jwt.getClaims();
                        for (String key : map.keySet()) {
                            if ("isOperation".equals(key)) {
                                variables.put(key, String.valueOf(map.get(key).asBoolean()));
                            } else if ("iat".equals(key) || "exp".equals(key)) {
                                variables.put(key, String.valueOf(map.get(key).asDate().getTime()));
                            } else if ("tokenType".equals(key)) {
                                variables.put(key, String.valueOf(map.get(key).asInt()));
                            } else {
                                variables.put(key, map.get(key).asString());
                            }
                        }
                    }
                }
            } catch (JWTDecodeException e) {
                e.printStackTrace();
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
