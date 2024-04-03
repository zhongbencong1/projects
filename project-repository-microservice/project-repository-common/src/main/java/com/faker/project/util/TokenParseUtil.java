package com.faker.project.util;

import com.alibaba.fastjson.JSON;
import com.faker.project.vo.LoginUserInfo;
import com.faker.project.constant.CommonConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang.StringUtils;
import sun.misc.BASE64Decoder;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;


/**
 * jwt token 解析工具类
 */
public class TokenParseUtil {

    /**
     * 从jwt token 中解析出 LoginUserInfo 登录对象
     */
    public static LoginUserInfo parseLoginUserInfoFromToken(String token) throws Exception {
        if (StringUtils.isEmpty(token)) {
            return null;
        }

        Jws<Claims> claimsJws = parseToken(token, getPublicKey());
        Claims body = claimsJws.getBody();
        /* token 过期 返回null */
        if (body.getExpiration().before(Calendar.getInstance().getTime())) {
            return null;
        }

        return JSON.parseObject(body.get(CommonConstant.JWT_USER_INFO_KEY).toString(), LoginUserInfo.class);
    }

    /**
     * 通过公钥解析 jwt token
     */
    private static Jws<Claims> parseToken(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }


    /**
     * 根据本地存储的公钥获取 publicKey 对象
     */
    private static PublicKey getPublicKey() throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
                new BASE64Decoder().decodeBuffer(CommonConstant.PUBLIC_KEY)
        );
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }

}
