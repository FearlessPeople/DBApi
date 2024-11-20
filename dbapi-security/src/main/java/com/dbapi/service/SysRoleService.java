package com.dbapi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dbapi.entity.SysRole;

/**
 * 角色表(SysRole)表服务接口
 *
 * @author zfang
 * @time 2024-09-11 11:23:34
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 分页查询
     *
     * @return
     */
    Page<SysRole> selectPage(Page<SysRole> page, SysRole role);

}

