package com.dbapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dbapi.entity.ApiSqlParam;
import org.apache.ibatis.annotations.Mapper;

/**
 * 接口SQL参数(ApiSqlParam)表数据库访问层
 *
 * @author zfang
 * @time 2024-11-12 15:26:36
 */
@Mapper
public interface ApiSqlParamDao extends BaseMapper<ApiSqlParam> {

}

