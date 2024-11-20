package com.dbapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbapi.common.Result;
import com.dbapi.entity.ApiCall;
import com.dbapi.entity.ApiList;
import com.dbapi.entity.ApiSql;
import com.dbapi.entity.ApiSqlParam;
import com.dbapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 接口SQL(ApiSql)表控制层
 *
 * @author zfang
 * @time 2024-10-24 14:30:51
 */
@RestController
@RequestMapping("sql")
public class ApiSqlController {
    @Resource
    private ApiSqlService apiSqlService;

    @Autowired
    private ApiListService apiListService;
    @Resource
    private ApiSqlParamService apiSqlParamService;

    @Autowired
    private DataSourceManager dataSourceManager;

    /**
     * 分页查询所有数据
     *
     * @param page   分页对象
     * @param apiSql 查询实体
     * @return 所有数据
     */
    @PostMapping("/list")
    public Result list(Page<ApiSql> page, ApiSql apiSql) {
        return Result.success(this.apiSqlService.page(page, new QueryWrapper<>(apiSql)));
    }

    /**
     * 保存SQL
     *
     * @param apiSql
     * @return
     */
    @PostMapping("/save")
    public Result save(ApiSql apiSql) {
        boolean saveStatus;
        if (apiSql.getId() != null) {
            ApiSql byId = this.apiSqlService.getById(apiSql.getId());
            if (byId != null) {
                saveStatus = this.apiSqlService.updateById(apiSql);
            } else {
                return Result.fail("ApiSQL ID:" + apiSql.getId() + " 在数据库中未找到！保存失败！");
            }
        } else {
            saveStatus = this.apiSqlService.save(apiSql);
        }
        if (saveStatus) {
            return Result.success("保存成功~");
        } else {
            return Result.fail("保存失败");
        }
    }

    /**
     * 执行SQL
     */
    @PostMapping("/call")
    public Result call(@RequestBody ApiCall apiCall) {
        try {
            QueryWrapper<ApiList> apiQueryWrapper = new QueryWrapper<>();
            apiQueryWrapper.eq("api_path", apiCall.getApiPath());
            ApiList api = this.apiListService.getOne(apiQueryWrapper);
            if (api == null) {
                return Result.fail("接口不存在！");
            }

            QueryWrapper<ApiSql> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("api_id", api.getId());
            ApiSql apisql = this.apiSqlService.getOne(queryWrapper);
            if (apisql == null) {
                return Result.fail("接口存在，但未配置SQL，请先配置SQL后再进行查询。");
            }

            DataSourceProvider provider = dataSourceManager.getDataSourceProvider(apisql.getDatasourceId());
            String finalSql = apiSqlService.replaceSqlParam(apisql.getApiSql(), apiCall.getApiSqlParams());
            Map<String, Object> result = provider.executeQuery(finalSql);
            return Result.success("执行成功~", result);
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            return Result.fail("Internal server error: " + e.getMessage());
        }
    }

    /**
     * 执行SQL
     */
    @PostMapping("/execute")
    public Result execute(ApiSql params) {
        try {
            // QueryWrapper<ApiSql> queryWrapper = new QueryWrapper<>();
            // queryWrapper.eq("api_id", params.getApiId());
            // ApiSql apisql = apiSqlService.getOne(queryWrapper);
            // if (apisql == null) {
            //     apisql.setApiSql(params.getApiSql());
            //     apisql.setDatasourceId(params.getDatasourceId());
            // }

            DataSourceProvider provider = dataSourceManager.getDataSourceProvider(params.getDatasourceId());
            String finalSql = apiSqlService.replaceSqlParam(params);
            Map<String, Object> result = provider.executeQuery(finalSql);
            return Result.success("执行成功~", result);
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            return Result.fail("Internal server error: " + e.getMessage());
        }
    }

    /**
     * 根据api_id获取SQL
     */
    @GetMapping("/getSql")
    public Result getSql(Integer apiId) {
        QueryWrapper<ApiSql> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("api_id", apiId);
        return Result.success(this.apiSqlService.getOne(queryWrapper));
    }

    /**
     * 获取SQL参数
     *
     * @param apiId
     * @return
     */
    @GetMapping("/getSqlParam")
    public Result getSqlParam(Integer apiId) {
        QueryWrapper<ApiSqlParam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("api_id", apiId);
        List<ApiSqlParam> list = apiSqlParamService.list(queryWrapper);
        return Result.success(list);
    }

    /**
     * 添加SQL参数
     *
     * @param apiSqlParam
     * @return
     */
    @PostMapping("/addSqlParam")
    public Result addSqlParam(ApiSqlParam apiSqlParam) {
        boolean save = apiSqlParamService.save(apiSqlParam);
        return Result.success(save);
    }

    /**
     * 编辑SQL参数
     *
     * @param apiSqlParam
     * @return
     */
    @PostMapping("/editSqlParam")
    public Result editSqlParam(ApiSqlParam apiSqlParam) {
        boolean update = apiSqlParamService.updateById(apiSqlParam);
        return Result.success(update);
    }

    /**
     * 删除SQL参数
     *
     * @param apiSqlParam
     * @return
     */
    @GetMapping("/deleteSqlParam")
    public Result deleteSqlParam(ApiSqlParam apiSqlParam) {
        boolean remove = apiSqlParamService.removeById(apiSqlParam.getId());
        return Result.success(remove);
    }
}

