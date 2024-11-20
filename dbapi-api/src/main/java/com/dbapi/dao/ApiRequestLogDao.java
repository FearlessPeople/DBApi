package com.dbapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dbapi.entity.ApiRequestLog;
import com.dbapi.entity.LineChartRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 接口请求日志表(ApiRequestLog)表数据库访问层
 *
 * @author zfang
 * @time 2024-11-18 16:31:14
 */
@Mapper
public interface ApiRequestLogDao extends BaseMapper<ApiRequestLog> {
    List<LineChartRecord> getTrendData();
}

