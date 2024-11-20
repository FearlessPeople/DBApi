package com.dbapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dbapi.entity.SysDatasource;
import org.apache.ibatis.annotations.Mapper;

/**
 * (SysDatasource)表数据库访问层
 *
 * @author zfang
 * @time 2024-10-12 14:35:12
 */
@Mapper
public interface SysDatasourceDao extends BaseMapper<SysDatasource> {

}

