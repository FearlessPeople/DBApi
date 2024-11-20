package com.dbapi.interceptor;

import com.dbapi.annotation.Permissions;
import com.dbapi.manager.PermissionsManager;
import com.dbapi.service.SysUserService;
import com.dbapi.constants.Constants;
import com.dbapi.entity.SysUser;
import com.dbapi.exception.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限拦截器
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    @Autowired
    private PermissionsManager permissionsManager;
    @Autowired
    private SysUserService sysUserService;

    public AuthenticationInterceptor(PermissionsManager permissionsManager, SysUserService sysUserService) {
        this.permissionsManager = permissionsManager;
        this.sysUserService = sysUserService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断当前的处理器是否为HandlerMethod类型
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            // 获取方法上的 @Permissions 注解
            Permissions permission = method.getMethodAnnotation(Permissions.class);
            if (permission != null) {
                try {
                    // 从请求中提取并验证token
                    String token = this.permissionsManager.verifyToken(request);
                    Integer currentUserId = this.permissionsManager.getCurrentUserId();
                    SysUser currentUser = this.sysUserService.getById(currentUserId);
                    // 将验证后的token设置为请求的属性，供后续使用
                    request.setAttribute(Constants.Authorization, token);

                    // 获取方法注解上的权限值
                    String requiredPermission = permission.value().trim();
                    // 验证当前token是否拥有所需权限
                    boolean hasPermission = this.permissionsManager.hasPermission(currentUser, requiredPermission);
                    if (!hasPermission) {
                        logger.error("用户" + currentUser.getUsername() + "没有" + requiredPermission + "权限，请检查权限分配以及sys_permission表中是否存在该" + requiredPermission + "权限记录。");
                        // 如果权限验证失败，返回403状态码，表示禁止访问
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        return false;  // 阻止请求继续处理
                    }
                } catch (ServerException e) {
                    // 如果token验证失败，返回401状态码，表示未授权
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return false;  // 阻止请求继续处理
                }
            }
        }
        // 如果没有 @Permissions 注解，或者权限验证成功，允许请求继续处理
        return true;
    }

}
