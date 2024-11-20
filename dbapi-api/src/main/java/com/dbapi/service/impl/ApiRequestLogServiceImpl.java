package com.dbapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbapi.dao.ApiRequestLogDao;
import com.dbapi.entity.ApiRequestLog;
import com.dbapi.entity.LineChartRecord;
import com.dbapi.service.ApiRequestLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 接口请求日志表(ApiRequestLog)表服务实现类
 *
 * @author zfang
 * @time 2024-11-18 16:31:14
 */
@Service("apiRequestLogService")
public class ApiRequestLogServiceImpl extends ServiceImpl<ApiRequestLogDao, ApiRequestLog> implements ApiRequestLogService {

    @Autowired
    private ApiRequestLogDao apiRequestLogDao;
    @Override
    public List<LineChartRecord> getTrendData() {
        return this.apiRequestLogDao.getTrendData();
    }
}

