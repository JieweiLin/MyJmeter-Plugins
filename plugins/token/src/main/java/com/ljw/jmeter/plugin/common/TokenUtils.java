package com.ljw.jmeter.plugin.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author 林杰炜 linjw
 *  token工具类
 * @Description token工具类
 * @date 2019/1/21 15:50
 */
public class TokenUtils {
    private static final Logger log = LoggerFactory.getLogger(TokenUtils.class);

    public static Algorithm getAlgoritm(AlgorithmEnum algorithm, String tokenSecret) {
        if (AlgorithmEnum.HS256.equals(algorithm)) {
            return Algorithm.HMAC256(tokenSecret);
        } else if (AlgorithmEnum.HS384.equals(algorithm)) {
            return Algorithm.HMAC384(tokenSecret);
        } else if (AlgorithmEnum.HS512.equals(algorithm)) {
            return Algorithm.HMAC512(tokenSecret);
        }
        return null;
    }

    public static String createToken(String data, String tokenSecret, AlgorithmEnum algorithm) {
        Payload payload = JsonUtils.readValue(data, Payload.class);
        Map<String, Object> headerClaims = Maps.newHashMap();
        headerClaims.put("alg", Constants.ALGORITHM_MAP.get(algorithm.getValue()));
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
                .sign(getAlgoritm(algorithm, tokenSecret));
        return token;
    }

    public static Payload decodedToken(JMeterVariables variables, SampleResult result, String variableName, String token) {
        try {
            if (StringUtils.isNotBlank(token)){
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
                            variables.put(variableName + "_" + key, String.valueOf(map.get(key).asBoolean()));
                        } else if ("iat".equals(key) || "exp".equals(key)) {
                            variables.put(variableName + "_" + key, String.valueOf(map.get(key).asDate().getTime()));
                        } else if ("tokenType".equals(key)) {
                            variables.put(variableName + "_" + key, String.valueOf(map.get(key).asInt()));
                        } else {
                            variables.put(variableName + "_" + key, map.get(key).asString());
                        }
                    }
                }
                return payload;
            }
        } catch (JWTDecodeException e) {
            log.error("token decoded error", e);
            if (Objects.nonNull(result)){
                result.setSuccessful(false);
            }
        }
        return null;
    }
}
