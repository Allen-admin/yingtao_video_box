package com.k365.video_common.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.k365.video_common.constant.Constants;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtUtil {

    // 过期时间一周
//    public static final long EXPIRE_TIME = 7 * 24 * 60 *60 * 1000;
    public static final long DEFAULT_EXPIRE_TIME = 24 * 60 * 60 * 1000;

    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 是否正确
     */
    public static boolean verify(String token, String account) {
        return verify(token,MD5Util.CIPHERTEXT, account);
    }

    public static boolean verify(String token,String secret, String account) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(account + secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim(Constants.ACCOUNT, account)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static String getAccount(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(Constants.ACCOUNT).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得Token中的信息无需secret解密也能获得
     *
     * @param token
     * @param claim
     * @return
     */
    public static String getClaim(String token, String claim) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    /**
     *
     * @param account
     * @param time
     * @param expireTime 过期时间 单位：毫秒
     * @return
     */
    public static String sign(String account,String secret, String time,long expireTime) {
        // 此处过期时间，单位：毫秒
        Date date = new Date(System.currentTimeMillis() + expireTime);
        Algorithm algorithm = Algorithm.HMAC256(account + secret);

        return JWT.create()
                .withClaim(Constants.ACCOUNT, account)
                .withClaim(Constants.CURRENT_TIME_MILLIS, time)
                .withExpiresAt(date)
                .sign(algorithm);
    }


    public static String sign(String account, String time) {
        return sign( account,MD5Util.CIPHERTEXT, time,DEFAULT_EXPIRE_TIME);
    }

    public static String sign(String account,String secret, String time) {
        return sign( account, secret,time,DEFAULT_EXPIRE_TIME);
    }

    public static String getToken(ServletRequest request){
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        return httpRequest.getHeader(Constants.REQUEST_TOKEN_HEADER);

    }

}
