package com.dbapi.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dbapi.entity.ApiRequestLog;
import com.dbapi.entity.LineChartRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 接口请求日志表(ApiRequestLog)表服务接口
 *
 * @author zfang
 * @time 2024-11-18 16:31:14
 */
public interface ApiRequestLogService extends IService<ApiRequestLog> {

    List<LineChartRecord> getTrendData();
}

