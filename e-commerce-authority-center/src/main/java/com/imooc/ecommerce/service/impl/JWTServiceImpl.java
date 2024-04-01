package com.imooc.ecommerce.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.ecommerce.constant.AuthorityConstant;
import com.imooc.ecommerce.constant.CommonConstant;
import com.imooc.ecommerce.mapper.EcommerceUserMapper;
import com.imooc.ecommerce.entity.EcommerceUser;
import com.imooc.ecommerce.service.IJWTService;
import com.imooc.ecommerce.vo.LoginUserInfo;
import com.imooc.ecommerce.vo.UserNameAndPassword;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Decoder;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class JWTServiceImpl extends ServiceImpl<EcommerceUserMapper, EcommerceUser> implements IJWTService {

    private final EcommerceUserMapper ecommerceUserMapper;

    public JWTServiceImpl(EcommerceUserMapper ecommerceUserMapper) {
        this.ecommerceUserMapper = ecommerceUserMapper;
    }
    @Override
    public String generateToken(String username, String password) throws Exception {
        return generateToken(username, password, 0);
    }

    @Override
    public String generateToken(String username, String password, int expire) throws Exception {
        if (expire <= 0) {
            expire = AuthorityConstant.DEFAULT_EXPIRE_DAY;
        }
        LambdaQueryWrapper<EcommerceUser> lqw = new LambdaQueryWrapper<>();
        lqw.eq(EcommerceUser::getUsername, username);
        lqw.eq(EcommerceUser::getPassword, password);
        EcommerceUser ecommerceUser = ecommerceUserMapper.selectOne(lqw);

        if (null == ecommerceUser) {
            log.error("can not find user: [{}], [{}]", username, password);
            return null;
        }
        // Token 中塞入对象, 即 JWT 中存储的信息, 后端拿到这些信息就可以知道是哪个用户在操作
        LoginUserInfo loginUserInfo = new LoginUserInfo(ecommerceUser.getId(), ecommerceUser.getUsername());


        // 计算超时时间
        ZonedDateTime zdt = LocalDate.now().plus(expire, ChronoUnit.DAYS).atStartOfDay(ZoneId.systemDefault());
        Date expireDate = Date.from(zdt.toInstant());

        return Jwts.builder()
                // jwt payload --> KV
                .claim(CommonConstant.JWT_USER_INFO_KEY, JSON.toJSONString(loginUserInfo))
                // jwt id
                .setId(UUID.randomUUID().toString())
                // jwt 过期时间
                .setExpiration(expireDate)
                // jwt 签名 --> 加密
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    /**
     * 注册后生成token
     */
    @Override
    public String registerUserAndGenerateToken(UserNameAndPassword up) throws Exception {
        LambdaQueryWrapper<EcommerceUser> qw = new LambdaQueryWrapper<>();
        qw.eq(EcommerceUser::getUsername, up.getUsername());
        EcommerceUser byUsername = ecommerceUserMapper.selectOne(qw);
        if (Objects.nonNull(byUsername)) {
            return null;
        }

        EcommerceUser eu = new EcommerceUser();
        eu.setUsername(up.getUsername()).setPassword(up.getPassword());

        int insert = ecommerceUserMapper.insert(eu);
        log.info("register user: [{}], [{}], result is {}", eu.getUsername(), eu.getId(), insert);

        return generateToken(eu.getUsername(), eu.getPassword());

    }

    /**
     * <h2>根据本地存储的私钥获取到 PrivateKey 对象</h2>
     * */
    private PrivateKey getPrivateKey() throws Exception {

        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                new BASE64Decoder().decodeBuffer(AuthorityConstant.PRIVATE_KEY));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(priPKCS8);
    }
}
