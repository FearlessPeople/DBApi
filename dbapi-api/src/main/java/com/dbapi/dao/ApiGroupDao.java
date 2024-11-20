package com.dbapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dbapi.entity.ApiGroup;
import org.apache.ibatis.annotations.Mapper;

/**
 * 接口分组表(ApiGroup)表数据库访问层
 *
 * @author zfang
 * @time 2024-10-24 14:28:34
 */
@Mapper
public interface ApiGroupDao extends BaseMapper<ApiGroup> {

}

