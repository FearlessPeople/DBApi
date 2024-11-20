package com.dbapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dbapi.entity.SysPermissions;
import org.apache.ibatis.annotations.Mapper;

/**
 * 权限表(SysPermissions)表数据库访问层
 *
 * @author zfang
 * @time 2024-09-11 11:23:34
 */
@Mapper
public interface SysPermissionsDao extends BaseMapper<SysPermissions> {

}

