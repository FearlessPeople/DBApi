package com.dbapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbapi.entity.SysPermissions;
import com.dbapi.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色表(SysRole)表数据库访问层
 *
 * @author zfang
 * @time 2024-09-11 11:23:34
 */
@Mapper
public interface SysRoleDao extends BaseMapper<SysRole> {
    Page<SysRole> selectPage(IPage<SysRole> page, SysRole sysRole);

    List<SysPermissions> getPermissionsByRoleId(Integer id);
}

