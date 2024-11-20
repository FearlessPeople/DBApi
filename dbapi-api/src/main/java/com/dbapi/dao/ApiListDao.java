package com.dbapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dbapi.entity.ApiList;
import org.apache.ibatis.annotations.Mapper;

/**
 * (ApiList)表数据库访问层
 *
 * @author zfang
 * @time 2024-10-24 14:29:42
 */
@Mapper
public interface ApiListDao extends BaseMapper<ApiList> {

}

