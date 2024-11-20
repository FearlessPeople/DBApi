package com.dbapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbapi.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * (SysUser)表数据库访问层
 *
 * @author zfang
 * @time 2024-09-03 15:30:17
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUser> {

    Page<SysUser> selectPage(IPage<SysUser> page, SysUser sysUser);

}
