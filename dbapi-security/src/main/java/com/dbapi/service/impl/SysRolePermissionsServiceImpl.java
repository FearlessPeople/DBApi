package com.dbapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbapi.dao.SysRolePermissionsDao;
import com.dbapi.entity.SysRolePermissions;
import com.dbapi.entity.SysPermissions;
import com.dbapi.service.SysRolePermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色权限关系表(SysRolePermissions)表服务实现类
 *
 * @author zfang
 * @time 2024-09-11 11:23:34
 */
@Service("sysRolePermissionsService")
public class SysRolePermissionsServiceImpl extends ServiceImpl<SysRolePermissionsDao, SysRolePermissions> implements SysRolePermissionsService {

    @Autowired
    private SysRolePermissionsDao sysRolePermissionsDao;

    /**
     * 获取角色对应权限列表
     *
     * @param roleIds
     * @return
     */
    @Override
    public List<String> getPermissionsByRoleIds(List<Integer> roleIds) {
        if (roleIds.isEmpty()) {
            return null;
        }
        return this.sysRolePermissionsDao.getPermissionsByRoleIds(roleIds);
    }

    /**
     * 根据角色id获取权限列表
     *
     * @param roleId
     * @return
     */
    @Override
    public List<SysPermissions> getPermissionsByRoleId(Integer roleId) {
        return this.sysRolePermissionsDao.getPermissionsByRoleId(roleId);
    }

    /**
     * 获取sys_permissions表所有权限
     *
     * @return
     */
    @Override
    public List<SysPermissions> getAllPermissions() {
        return this.sysRolePermissionsDao.getAllPermissions();
    }
}

