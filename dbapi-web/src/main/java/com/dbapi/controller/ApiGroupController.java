package com.dbapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbapi.common.Result;
import com.dbapi.entity.ApiGroup;
import com.dbapi.service.ApiGroupService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 接口分组表(ApiGroup)表控制层
 *
 * @author zfang
 * @time 2024-10-24 14:30:21
 */
@RestController
@RequestMapping("group")
public class ApiGroupController {
    /**
     * 服务对象
     */
    @Resource
    private ApiGroupService apiGroupService;

    /**
     * 所有数据
     *
     * @return 所有数据
     */
    @GetMapping("/allList")
    public Result allList() {
        return Result.success(this.apiGroupService.list());
    }

    /**
     * 分页查询所有数据
     *
     * @param page     分页对象
     * @param apiGroup 查询实体
     * @return 所有数据
     */
    @PostMapping("/list")
    public Result list(Page<ApiGroup> page, ApiGroup apiGroup) {
        return Result.success(this.apiGroupService.page(page, new QueryWrapper<>(apiGroup)));
    }

    /**
     * 分组下接口列表
     *
     * @return
     */
    @PostMapping("/apiList")
    public Result apiList(Page<ApiGroup> page, String apiName) {
        return Result.success(apiGroupService.apiList(page, apiName));
    }

    /**
     * 新增分组
     *
     * @param apiGroup
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody ApiGroup apiGroup) {
        return Result.success(apiGroupService.save(apiGroup));
    }

    /**
     * 编辑分组
     *
     * @param apiGroup
     * @return
     */
    @PostMapping("/edit")
    public Result edit(@RequestBody ApiGroup apiGroup) {
        return Result.success(apiGroupService.updateById(apiGroup));
    }

    /**
     * 删除分组
     *
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public Result delete(Integer id) {
        if (apiGroupService.hasApi(id)) {
            return Result.fail("该分组下存在接口，无法删除~");
        }
        return Result.success("分组删除成功~", apiGroupService.removeById(id));
    }
}

