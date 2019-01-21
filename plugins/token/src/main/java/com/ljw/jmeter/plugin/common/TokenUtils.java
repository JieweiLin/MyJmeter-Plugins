package com.ljw.jmeter.plugin.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.collect.Maps;

import java.util.Date;
import java.util.Map;

/**
 * @author 林杰炜 linjw
 * @Title token工具类
 * @Description token工具类
 * @date 2019/1/21 15:50
 */
public class TokenUtils {

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
        headerClaims.put("alg", Constants.algorithmMap.get(algorithm.getValue()));
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
}
