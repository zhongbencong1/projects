package com.faker.project.filter;

import com.faker.project.constant.CommonConstant;
import com.faker.project.util.TokenParseUtil;
import com.faker.project.vo.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户身份统一登录拦截
 * */
@SuppressWarnings("all")
@Slf4j
@Component
public class LoginUserInfoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        // 部分请求不需要带有身份信息, 即白名单
        if (checkWhiteListUrl(request.getRequestURI())) {
            return true;
        }

        // 先尝试从 http header 里面拿到 token
        String token = request.getHeader(CommonConstant.JWT_USER_INFO_KEY);

        LoginUserInfo loginUserInfo = null;
        try {
            loginUserInfo = TokenParseUtil.parseLoginUserInfoFromToken(token);
        } catch (Exception ex) {
            log.error("parse login user info error: [{}]", ex.getMessage(), ex);
        }

        // 如果程序走到这里, 说明 header 中没有 token 信息
        if (null == loginUserInfo) {
            throw new RuntimeException("can not parse current login user");
        }

        log.info("set login user info: [{}]", request.getRequestURI());
        // 设置当前请求上下文, 把用户信息填充进去
        AccessContext.setLoginUserInfo(loginUserInfo);

        return true;
    }

    /** 修改返回结果时 */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {

    }

    /** 在请求完全结束后调用, 常用于清理资源等工作 释放缓存的对象(防止内存泄漏)*/
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        if (null != AccessContext.getLoginUserInfo()) {
            AccessContext.clearLoginUserInfo();
        }
    }

    /** 校验是否是白名单接口 */
    private boolean checkWhiteListUrl(String url) {
        return StringUtils.containsAny(
                url,
                "springfox", "swagger", "v2",
                "webjars", "doc.html"
        );
    }
}
