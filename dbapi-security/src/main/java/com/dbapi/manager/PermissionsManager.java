package com.dbapi.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dbapi.common.HttpStatusEnum;
import com.dbapi.constants.Constants;
import com.dbapi.constants.DbConstants;
import com.dbapi.exception.ServerException;
import com.dbapi.entity.*;
import com.dbapi.service.SysRolePermissionsService;
import com.dbapi.service.SysUserRolesService;
import com.dbapi.util.JwtUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * PermissionsManager的主要职责是处理认证（Authentication）和授权（Authorization），包括登录验证、用户权限检查、生成和解析JWT令牌等。
 * 通过它，可以将认证和授权的核心逻辑从具体的服务类（如AuthServiceImpl）中分离出来，简化其他服务类的职责，使整个项目的认证逻辑更为集中和可控。
 */
@Component
public class PermissionsManager {

    private static final Logger logger = LoggerFactory.getLogger(PermissionsManager.class);

    @Value("${user.admin.allPermissions}")
    private boolean allPermissions;

    @Autowired
    private SysUserRolesService sysUserRolesService;

    @Autowired
    private SysRolePermissionsService sysRolePermissionsService;

    @Autowired
    private JwtUtil jwtUtil;


    /**
     * 认证用户并返回包含token的用户
     *
     * @param sysUser 须验证的用户
     * @return 认证结果用户信息SysUser，SysUser中包括认证token及Permission
     */
    public SysUser authenticate(SysUser sysUser) {
        if (sysUser == null) {
            throw new ServerException(HttpStatusEnum.SUCCESS.getCode(), "用户名或密码输入错误！");
        } else {
            if (sysUser.getStatus() == 1 || sysUser.getIsDelete() == 0) {
                throw new ServerException("登录失败，用户被禁用或已删除,请联系管理员处理~");
            }
            sysUser.setPassword(null);
            // 用户登录成功，获取token，设置cookie
            String jwt = jwtUtil.generateToken(String.valueOf(sysUser.getId()));
            sysUser.setToken(jwt);
            return sysUser;
        }
    }

    /**
     * 给用户分配权限
     *
     * @param sysUser
     * @return
     */
    public SysUser setUserPermissions(SysUser sysUser) {
        if (sysUser != null) {
            List<String> permissions = this.getCurrentUserPermissions(sysUser);
            sysUser.setPermissions(permissions);
        }
        return sysUser;
    }

    /**
     * 给角色分配权限
     *
     * @param role
     * @return
     */
    public boolean setRolePermissions(SysRole role) {
        // 清空现有角色所有权限
        QueryWrapper removeWrapper = new QueryWrapper();
        removeWrapper.eq("role_id", role.getId());
        this.sysRolePermissionsService.remove(removeWrapper);

        // 重新分配权限
        for (Integer permissionId : role.getPermissionIds()) {
            try {
                SysRolePermissions rolePermission = new SysRolePermissions();
                rolePermission.setRoleId(role.getId());
                rolePermission.setPermissionId(permissionId);
                this.sysRolePermissionsService.save(rolePermission);
            } catch (DuplicateKeyException e) {
                if (e.getMessage().contains("Duplicate entry")) {
                    logger.warn("Duplicate entry found, ignoring: " + e.getMessage());
                } else {
                    logger.error("Duplicate key error: " + e.getMessage());
                }
            } catch (Exception e) {
                logger.error("An unexpected error occurred: " + e.getMessage());
                return false;
            }
        }
        return true;

    }


    /**
     * 设置当前用户角色
     *
     * @return
     */
    public boolean setUserRoles(SysUser sysUser) {
        Integer userId = sysUser.getId();
        try {
            QueryWrapper<SysUserRoles> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            this.sysUserRolesService.remove(queryWrapper);

            for (Integer roleId : sysUser.getRoleIds()) {
                SysUserRoles sysUserRoles = new SysUserRoles();
                sysUserRoles.setUserId(userId);
                sysUserRoles.setRoleId(roleId);
                this.sysUserRolesService.save(sysUserRoles);
            }
            return true;
        } catch (Exception e) {
            logger.error("为用户分配角色失败，用户ID: {}", userId, e);
            throw new ServerException("获取用户角色失败");
        }
    }


    /**
     * 获取当前用户所有角色列表
     *
     * @return
     */
    public List<SysRole> getCurrentUserRoles(Integer userId) {
        Integer resolvedUserId = Optional.ofNullable(userId).orElseGet(this::getCurrentUserId);
        logger.info("获取用户ID为 {} 的角色列表", resolvedUserId);
        try {
            return sysUserRolesService.getCurrentUserRoles(resolvedUserId);
        } catch (Exception e) {
            logger.error("获取用户角色失败，用户ID: {}", resolvedUserId, e);
            throw new RuntimeException("获取用户角色失败", e);
        }
    }

    /**
     * 所有已启用权限列表
     *
     * @return
     */
    public List<SysPermissions> getAllPermissions() {
        return this.sysRolePermissionsService.getAllPermissions();
    }

    /**
     * 获取sys_permissions表所有权限表达式列表
     *
     * @return
     */
    public List<String> getAllPermissionsExpressions() {
        List<SysPermissions> allPermissions = this.sysRolePermissionsService.getAllPermissions();
        List<String> allExpressions = new ArrayList<>();
        for (SysPermissions allPermission : allPermissions) {
            allExpressions.add(allPermission.getExpression());
        }
        return allExpressions;
    }

    /**
     * 获取角色对应的所有树形结构权限列表
     *
     * @param role
     * @return
     */
    public SysPermissionTree getAllPermissionsTree(SysRole role) {
        List<SysPermissions> permissions = this.sysRolePermissionsService.getPermissionsByRoleId(role.getId());
        SysPermissionTree sysPermissionTree = new SysPermissionTree();
        sysPermissionTree.setKey(0);
        sysPermissionTree.setTitle("权限列表");
        sysPermissionTree.setChildren(transformToTree(permissions));
        return sysPermissionTree;
    }

    /**
     * 获取所有树形结构权限列表
     *
     * @return
     */
    public SysPermissionTree getAllPermissionsTree() {
        List<SysPermissions> permissions = this.sysRolePermissionsService.getAllPermissions();
        SysPermissionTree sysPermissionTree = new SysPermissionTree();
        sysPermissionTree.setKey(0);
        sysPermissionTree.setTitle("权限列表");
        sysPermissionTree.setChildren(transformToTree(permissions));
        return sysPermissionTree;
    }

    /**
     * 根据角色id获取权限列表
     *
     * @param roleId
     * @return
     */
    public List<SysPermissions> getPermissionsByRoleId(Integer roleId) {
        return this.sysRolePermissionsService.getPermissionsByRoleId(roleId);
    }

    /**
     * 获取当前用户的所有权限
     *
     * @param sysUser
     * @return
     */
    public List<String> getCurrentUserPermissions(SysUser sysUser) {
        if (sysUser != null) {
            // 如果是管理员并且application-pro.yml中开启了allPermissions，则获取所有权限
            // 否则按用户角色获取权限
            List<String> permissions = allPermissions && sysUser.getIsAdmin() == DbConstants.IsAdmin.YES ? getAllPermissionsExpressions() : getPermissionsExpressionsByUserId(sysUser.getId());

            return permissions;
        } else {
            return null;
        }
    }

    /**
     * 获取用户权限
     *
     * @param userId 用户ID
     * @return 用户的权限列表
     */
    public List<String> getPermissionsExpressionsByUserId(Integer userId) {
        List<Integer> roles = sysUserRolesService.getRoleIdsByUserId(userId);
        return sysRolePermissionsService.getPermissionsByRoleIds(roles);
    }


    /**
     * 转换为树形结构
     *
     * @param permissions
     * @return
     */
    private List<SysPermissionTree> transformToTree(List<SysPermissions> permissions) {
        List<SysPermissionTree> childrens = new ArrayList<>();
        for (SysPermissions permission : permissions) {
            SysPermissionTree sysPermissionTree = new SysPermissionTree();
            sysPermissionTree.setKey(permission.getId());
            sysPermissionTree.setTitle(permission.getName());
            sysPermissionTree.setExpression(permission.getExpression());
            sysPermissionTree.setRemark(permission.getRemark());
            sysPermissionTree.setStatus(permission.getStatus());
            sysPermissionTree.setCreateTime(permission.getCreateTime());
            sysPermissionTree.setUpdateTime(permission.getUpdateTime());
            childrens.add(sysPermissionTree);
        }
        return childrens;
    }


    /**
     * 检查用户是否有指定权限
     *
     * @param expression 需要检查的权限表达式
     * @return 是否有权限
     */
    public boolean hasPermission(Integer userId, String expression) {
        List<String> permissions = getPermissionsExpressionsByUserId(userId);
        if (permissions == null || permissions.isEmpty()) {
            return false;
        }
        return permissions.contains(expression);
    }

    /**
     * 检查用户是否有指定权限
     *
     * @param expression 需要检查的权限表达式
     * @return 是否有权限
     */
    public boolean hasPermission(SysUser sysUser, String expression) {
        List<String> permissions = getCurrentUserPermissions(sysUser);
        return permissions.contains(expression);
    }

    /**
     * 检查当前用户是否有指定权限
     *
     * @param expression 需要检查的权限
     * @return 是否有权限
     */
    public boolean hasPermission(String expression) {
        return hasPermission(this.getCurrentUserId(), expression);
    }

    /**
     * 从Request请求中提取并验证token有效性
     *
     * @param request
     * @return token
     */
    public String verifyToken(@NotNull HttpServletRequest request) {
        String token = request.getHeader(Constants.Authorization);
        String newToken = removeBearerPrefix(token);
        if (!jwtUtil.checkToken(newToken)) {
            throw new ServerException(401, "登录失效或token已过期");
        }
        return token;
    }

    /**
     * 验证JWT Token
     *
     * @param token JWT Token
     * @return 验证成功或失败
     */
    public boolean verifyToken(String token) {
        String newToken = removeBearerPrefix(token);
        return jwtUtil.checkToken(newToken);
    }

    /**
     * 从RequestContextHolder中获取当前线程token值
     *
     * @return
     */
    public String getCurrentToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(Constants.Authorization);
        return removeBearerPrefix(token);
    }

    /**
     * 获取当前请求线程中userId
     *
     * @return
     */
    public Integer getCurrentUserId() {
        String token = this.getCurrentToken();
        if (token == null) {
            return null;
        }
        try {
            return jwtUtil.getUserId(token);
        } catch (Exception e) {
            logger.error("获取当前线程中用户ID错误", e);
            throw new RuntimeException("获取当前线程中用户ID错误", e);
        }
    }


    /**
     * 移除token中Bearer前缀
     *
     * @param token
     * @return
     */
    public String removeBearerPrefix(String token) {
        if (token != null && token.startsWith(Constants.Bearer)) {
            return token.substring(7).trim();
        }
        return token;
    }

}

