package com.dbapi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dbapi.entity.ApiGroup;

import java.util.List;

/**
 * 接口分组表(ApiGroup)表服务接口
 *
 * @author zfang
 * @time 2024-10-24 14:28:38
 */
public interface ApiGroupService extends IService<ApiGroup> {

    /**
     * 分组下接口列表
     * @return
     */
    Page<ApiGroup> apiList(Page<ApiGroup> page, String apiName);

    /**
     * 查询分组下是否有接口
     */
    boolean hasApi(Integer groupId);
}

