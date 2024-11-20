package com.dbapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dbapi.entity.SysRolePermissions;
import com.dbapi.entity.SysPermissions;

import java.util.List;

/**
 * 角色权限关系表(SysRolePermissions)表服务接口
 *
 * @author zfang
 * @time 2024-09-11 11:23:34
 */
public interface SysRolePermissionsService extends IService<SysRolePermissions> {

    /**
     * 获取角色对应权限列表
     *
     * @param roleIds
     * @return
     */
    List<String> getPermissionsByRoleIds(List<Integer> roleIds);

    /**
     * 根据角色id获取权限列表
     *
     * @param roleId
     * @return
     */
    List<SysPermissions> getPermissionsByRoleId(Integer roleId);

    /**
     * 获取sys_permissions表所有权限
     *
     * @return
     */
    List<SysPermissions> getAllPermissions();
}

