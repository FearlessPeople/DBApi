package com.dbapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dbapi.entity.SysRole;
import com.dbapi.entity.SysUserRoles;

import java.util.List;

/**
 * 用户角色关系表(SysUserRoles)表服务接口
 *
 * @author zfang
 * @time 2024-09-11 11:23:34
 */
public interface SysUserRolesService extends IService<SysUserRoles> {
    /**
     * 获取用户角色id列表
     *
     * @param userId
     * @return
     */
    List<Integer> getRoleIdsByUserId(Integer userId);

    /**
     * 获取当前用户所有角色列表
     *
     * @return
     */
    List<SysRole> getCurrentUserRoles(Integer userId);
}

