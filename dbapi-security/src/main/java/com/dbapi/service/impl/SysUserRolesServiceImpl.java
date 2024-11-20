package com.dbapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbapi.dao.SysUserRolesDao;
import com.dbapi.entity.SysRole;
import com.dbapi.entity.SysUserRoles;
import com.dbapi.service.SysUserRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户角色关系表(SysUserRoles)表服务实现类
 *
 * @author zfang
 * @time 2024-09-11 11:23:34
 */
@Service("sysUserRolesService")
public class SysUserRolesServiceImpl extends ServiceImpl<SysUserRolesDao, SysUserRoles> implements SysUserRolesService {

    @Autowired
    private SysUserRolesDao sysUserRolesDao;

    /**
     * 获取用户角色id列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<Integer> getRoleIdsByUserId(Integer userId) {
        QueryWrapper queryWrapper = new QueryWrapper<SysUserRoles>();
        queryWrapper.eq("user_id", userId);
        List<SysUserRoles> list = sysUserRolesDao.selectList(queryWrapper);
        List<Integer> roleIds = new ArrayList<>();
        if (!list.isEmpty()) {
            for (SysUserRoles authUserRole : list) {
                roleIds.add(authUserRole.getRoleId());
            }
        }
        return roleIds;
    }

    /**
     * 获取当前用户所有角色列表
     *
     * @return
     */
    @Override
    public List<SysRole> getCurrentUserRoles(Integer userId) {
        QueryWrapper queryWrapper = new QueryWrapper<SysUserRoles>();
        queryWrapper.eq("user_id", userId);
        List<SysRole> currentUserRoles = sysUserRolesDao.getCurrentUserRoles(userId);
        return currentUserRoles;
    }
}

