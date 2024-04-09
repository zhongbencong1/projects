package com.faker.project.service.impl;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.faker.project.constant.CommonConstant;
import com.faker.project.entity.RepositoryUser;
import com.faker.project.constant.AuthorityConstant;
import com.faker.project.mapper.RepositoryUserMapper;
import com.faker.project.service.IJWTService;
import com.faker.project.vo.LoginUserInfo;
import com.faker.project.vo.UserNameAndPassword;
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
public class JWTServiceImpl extends ServiceImpl<RepositoryUserMapper, RepositoryUser> implements IJWTService {

    @Override
    public String generateToken(String username, String password) throws Exception {
        return generateToken(username, password, 0);
    }

    @Override
    public String generateToken(String username, String password, int expire) throws Exception {
        if (expire <= 0) {
            expire = AuthorityConstant.DEFAULT_EXPIRE_DAY;
        }
        LambdaQueryWrapper<RepositoryUser> lqw = new LambdaQueryWrapper<>();
        lqw.eq(RepositoryUser::getUsername, username);
        lqw.eq(RepositoryUser::getPassword, password);
        RepositoryUser repositoryUser = this.getOne(lqw);

        if (null == repositoryUser) {
            log.error("can not find user: [{}], [{}]", username, password);
            return null;
        }
        // Token 中塞入对象, 即 JWT 中存储的信息, 后端拿到这些信息就可以知道是哪个用户在操作
        LoginUserInfo loginUserInfo = new LoginUserInfo(repositoryUser.getId(), repositoryUser.getUsername());

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
        LambdaQueryWrapper<RepositoryUser> qw = new LambdaQueryWrapper<>();
        qw.eq(RepositoryUser::getUsername, up.getUsername());
        RepositoryUser byUsername = this.getOne(qw);
        if (Objects.nonNull(byUsername)) {
            log.error("username is registered: [{}]", byUsername);
            return null;
        }
        // 存储
        RepositoryUser eu = new RepositoryUser();
        eu.setUsername(up.getUsername());
        eu.setPassword(MD5.create().digestHex(up.getPassword()));
        boolean saveResult = this.save(eu);

        log.info("register user: [{}], [{}], result is {}", eu.getUsername(), eu.getId(), saveResult);
        return generateToken(eu.getUsername(), eu.getPassword());
    }

    /**
     * 根据本地存储的私钥获取到 PrivateKey 对象
     * */
    private PrivateKey getPrivateKey() throws Exception {

        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                new BASE64Decoder().decodeBuffer(AuthorityConstant.PRIVATE_KEY));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(priPKCS8);
    }
}
