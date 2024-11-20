package com.dbapi.controller;

import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dbapi.common.Result;
import com.dbapi.entity.ApiList;
import com.dbapi.entity.ApiRequestLog;
import com.dbapi.entity.ApiSql;
import com.dbapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 通用Controller处理器，主要处理动态接口请求
 */
@RestController
@RequestMapping("/dynamic")
public class DynamicController {

    @Autowired
    private DataSourceManager dataSourceManager;
    @Autowired
    private ApiListService apiListService;
    @Autowired
    private ApiSqlService apiSqlService;
    @Autowired
    private ApiRequestLogService requestLogService;

    @RequestMapping("/**")
    public Result handleDynamicRequest(HttpServletRequest request) {
        long startTime = System.currentTimeMillis(); // 记录开始时间
        String fullPath = request.getRequestURI().trim();
        String apiPath = fullPath.replace("/api/", "");
        if (apiPath.startsWith("/")) {
            apiPath = apiPath.substring(1);
        }

        String requestParams = request.getQueryString(); // 获取请求参数
        String clientIp = ServletUtil.getClientIP(request, null);
        ApiRequestLog log = new ApiRequestLog();
        log.setApiPath(apiPath);
        log.setRequestParams(requestParams);
        log.setClientIp(clientIp);

        try {
            // 查找 API 信息
            QueryWrapper<ApiList> apiQueryWrapper = new QueryWrapper<>();
            apiQueryWrapper.eq("api_path", apiPath);
            ApiList api = apiListService.getOne(apiQueryWrapper);
            if (api == null) {
                log.setResponseStatus(404);
                log.setResponseMessage("接口不存在！");
                requestLogService.save(log);
                return Result.fail("接口不存在！");
            }

            log.setApiId(api.getId()); // 设置 API ID
            log.setApiName(api.getApiName()); // 设置 API 名称

            // 查找 API SQL 信息
            QueryWrapper<ApiSql> sqlQueryWrapper = new QueryWrapper<>();
            sqlQueryWrapper.eq("api_id", api.getId());
            ApiSql apiSql = apiSqlService.getOne(sqlQueryWrapper);

            if (apiSql == null) {
                log.setResponseStatus(400);
                log.setResponseMessage("接口存在，但未配置SQL！");
                requestLogService.save(log);
                return Result.fail("接口存在，但未配置SQL！");
            }

            // 执行 SQL 查询
            DataSourceProvider provider = dataSourceManager.getDataSourceProvider(apiSql.getDatasourceId());
            String finalSql = apiSqlService.replaceSqlParam(apiSql);
            Map<String, Object> result = provider.executeQuery(finalSql);

            log.setResponseStatus(200);
            log.setResponseMessage("执行成功");
            log.setResponseData(result.toString()); // 保存响应数据
            return Result.success("执行成功~", result);
        } catch (Exception e) {
            log.setResponseStatus(500);
            log.setResponseMessage("请求失败: " + e.getMessage());
            return Result.fail("请求失败:" + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis(); // 记录结束时间
            log.setRequestDuration(endTime - startTime); // 记录耗时
            requestLogService.save(log); // 保存日志
        }
    }
}
