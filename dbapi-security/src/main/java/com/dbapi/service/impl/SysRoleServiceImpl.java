package com.dbapi.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbapi.dao.SysRoleDao;
import com.dbapi.manager.PermissionsManager;
import com.dbapi.entity.SysRole;
import com.dbapi.service.SysRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 角色表(AuthRole)表服务实现类
 *
 * @author zfang
 * @time 2024-09-11 11:23:34
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRole> implements SysRoleService {

    private static final Logger logger = LoggerFactory.getLogger(SysRoleServiceImpl.class);

    @Autowired
    private SysRoleDao sysRoleDao;

    @Autowired
    private PermissionsManager permissionsManager;

    /**
     * 分页查询
     *
     * @param page
     * @param role
     * @return
     */
    @Override
    public Page<SysRole> selectPage(Page<SysRole> page, SysRole role) {
        return this.sysRoleDao.selectPage(page, role);
    }

}

