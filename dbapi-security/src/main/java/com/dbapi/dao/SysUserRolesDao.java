package com.dbapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dbapi.entity.SysRole;
import com.dbapi.entity.SysUserRoles;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户角色关系表(SysUserRoles)表数据库访问层
 *
 * @author zfang
 * @time 2024-09-11 11:23:34
 */
@Mapper
public interface SysUserRolesDao extends BaseMapper<SysUserRoles> {

    /**
     * 获取当前用户所有角色列表
     *
     * @return
     */
    List<SysRole> getCurrentUserRoles(Integer userId);
}

