package com.dbapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbapi.common.Result;
import com.dbapi.entity.ApiList;
import com.dbapi.service.ApiListService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (ApiList)表控制层
 *
 * @author zfang
 * @time 2024-10-24 14:30:40
 */
@RestController
@RequestMapping("apiList")
public class ApiListController {
    /**
     * 服务对象
     */
    @Resource
    private ApiListService apiListService;

    /**
     * 分页查询所有数据
     *
     * @param page    分页对象
     * @param apiList 查询实体
     * @return 所有数据
     */
    @GetMapping("/list")
    public Result selectAll(Page<ApiList> page, ApiList apiList) {
        return Result.success(this.apiListService.page(page, new QueryWrapper<>(apiList)));
    }

    /**
     * 新增接口
     * @param apiList
     * @return
     */
    @PostMapping("/add")
    public Result addApi(@RequestBody ApiList apiList){
        ApiList apiList1 = this.apiListService.getOne(new QueryWrapper<ApiList>().eq("api_path", apiList.getApiPath()));
        if (apiList1 != null){
            return Result.fail("已存在路径为【"+apiList.getApiPath()+"】的接口【"+apiList1.getApiName()+"】，请修改为其他接口路径~");
        }
        return Result.success(this.apiListService.save(apiList));
    }

}

