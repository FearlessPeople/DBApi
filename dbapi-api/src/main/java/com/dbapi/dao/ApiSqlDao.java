package com.dbapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dbapi.entity.ApiSql;
import org.apache.ibatis.annotations.Mapper;

/**
 * 接口SQL(ApiSql)表数据库访问层
 *
 * @author zfang
 * @time 2024-10-24 14:29:56
 */
@Mapper
public interface ApiSqlDao extends BaseMapper<ApiSql> {

}

