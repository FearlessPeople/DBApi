package com.dbapi.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dbapi.common.Result;
import com.dbapi.entity.ApiRequestLog;
import com.dbapi.entity.LineChartRecord;
import com.dbapi.service.ApiListService;
import com.dbapi.service.ApiRequestLogService;
import com.dbapi.service.SysDatasourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 通用Controller处理器，主要处理动态接口请求
 */
@RestController
@RequestMapping("/workplace")
public class WorkPlaceController {

    @Autowired
    private SysDatasourceService sysDatasourceService;
    @Autowired
    private ApiListService apiListService;
    @Autowired
    private ApiRequestLogService requestLogService;

    /**
     * 总数据
     *
     * @return
     */
    @GetMapping("/data")
    public Result data() {
        Map<String, Object> result = new HashMap<>();
        // 数据源总数量
        result.put("datasourceCount", sysDatasourceService.count());
        //接口总数量
        result.put("apiCount", apiListService.count());
        //请求总数量
        result.put("requestCount", requestLogService.count());
        //昨日请求量
        Date startOfYesterday = DateUtil.beginOfDay(DateUtil.yesterday());  // 00:00:00 of yesterday
        Date endOfYesterday = DateUtil.endOfDay(DateUtil.yesterday());      // 23:59:59 of yesterday
        QueryWrapper<ApiRequestLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("created_time", startOfYesterday, endOfYesterday);
        result.put("yesterdayRequestCount", requestLogService.count(queryWrapper));
        return Result.success(result);
    }

    /**
     * 接口请求走势
     *
     * @return
     */
    @GetMapping("/apiTrend")
    public Result apiTrend() {
        List<LineChartRecord> trendData = this.requestLogService.getTrendData();
        return Result.success(trendData);
    }

}
