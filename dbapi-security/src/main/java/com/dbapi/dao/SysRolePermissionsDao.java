package com.dbapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dbapi.entity.SysRolePermissions;
import com.dbapi.entity.SysPermissions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色权限关系表(SysRolePermissions)表数据库访问层
 *
 * @author zfang
 * @time 2024-09-11 11:23:34
 */
@Mapper
public interface SysRolePermissionsDao extends BaseMapper<SysRolePermissions> {

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

    /**
     * 批量插入角色权限
     *
     * @param rolePermissions
     * @return
     */
    int batchInsertRolePermissions(@Param("list") List<SysRolePermissions> rolePermissions);

}

