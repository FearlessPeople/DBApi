package com.dbapi.interceptor;

import com.dbapi.annotation.RequireLogin;
import com.dbapi.constants.Constants;
import com.dbapi.exception.ServerException;
import com.dbapi.manager.PermissionsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private PermissionsManager permissionsManager;

    public LoginInterceptor(PermissionsManager permissionsManager) {
        this.permissionsManager = permissionsManager;
    }

    /**
     * 请求拦截
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断当前的处理器是否为HandlerMethod类型
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            // 如果方法上有 @RequireLogin 注解，表示需要进行登录验证
            if (method.getMethod().isAnnotationPresent(RequireLogin.class)) {
                try {
                    // 从请求中提取并验证token
                    String token = this.permissionsManager.verifyToken(request);
                    // 将验证后的token设置为请求的属性，供后续使用
                    request.setAttribute(Constants.Authorization, token);
                } catch (ServerException e) {
                    // 如果token验证失败，返回401状态码，表示未授权
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return false;  // 阻止请求继续处理
                }
            }
        }
        // 如果没有 @RequireLogin 注解，或者token验证成功，允许请求继续处理
        return true;
    }

}
