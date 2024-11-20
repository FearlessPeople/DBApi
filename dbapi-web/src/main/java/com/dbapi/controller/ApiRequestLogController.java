package com.dbapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbapi.common.Result;
import com.dbapi.entity.ApiRequestLog;
import com.dbapi.service.ApiRequestLogService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 接口请求日志表(ApiRequestLog)表控制层
 *
 * @author zfang
 * @time 2024-11-18 17:26:23
 */
@RestController
@RequestMapping("apiRequestLog")
public class ApiRequestLogController {
    /**
     * 服务对象
     */
    @Resource
    private ApiRequestLogService apiRequestLogService;

    /**
     * 分页查询所有数据
     *
     * @param page          分页对象
     * @param apiRequestLog 查询实体
     * @return 所有数据
     */
    @PostMapping("/list")
    public Result selectAll(Page<ApiRequestLog> page, ApiRequestLog apiRequestLog) {
        QueryWrapper<ApiRequestLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("api_id", apiRequestLog.getApiId());

        // 处理响应状态码筛选
        if (apiRequestLog.getResponseStatus() != null && apiRequestLog.getResponseStatus() != 0) {
            queryWrapper.eq("response_status", apiRequestLog.getResponseStatus());
        }

        // 处理日期范围筛选
        if (apiRequestLog.getCreatedTimeRange() != null && apiRequestLog.getCreatedTimeRange().size() == 2) {
            queryWrapper.between("created_time", apiRequestLog.getCreatedTimeRange().get(0), apiRequestLog.getCreatedTimeRange().get(1));
        }
        return Result.success(this.apiRequestLogService.page(page, queryWrapper));
    }
}
