package com.dbapi.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbapi.dao.ApiGroupDao;
import com.dbapi.dao.ApiListDao;
import com.dbapi.entity.ApiGroup;
import com.dbapi.entity.ApiList;
import com.dbapi.service.ApiGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 接口分组表(ApiGroup)表服务实现类
 *
 * @author zfang
 * @time 2024-10-24 14:28:39
 */
@Service("apiGroupService")
public class ApiGroupServiceImpl extends ServiceImpl<ApiGroupDao, ApiGroup> implements ApiGroupService {

    @Autowired
    private ApiGroupDao apiGroupDao;

    @Autowired
    private ApiListDao apiListDao;

    /**
     * 分组下接口列表
     *
     * @return
     */
    @Override
    public Page<ApiGroup> apiList(Page<ApiGroup> page, String apiName) {
        Page<ApiGroup> apiGroupPage = this.apiGroupDao.selectPage(page, new QueryWrapper<>());
        List<ApiGroup> records = apiGroupPage.getRecords();
        for (ApiGroup group : records) {
            QueryWrapper<ApiList> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("api_group", group.getId());
            if (!StrUtil.isBlankIfStr(apiName)) {
                queryWrapper.like("api_name", apiName);
            }
            List<ApiList> apiLists = apiListDao.selectList(queryWrapper);
            group.setApiList(apiLists);
        }
        return apiGroupPage;
    }

    /**
     * 查询分组下是否有接口
     *
     * @param groupId
     */
    @Override
    public boolean hasApi(Integer groupId) {
        QueryWrapper<ApiList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("api_group", groupId);
        if (apiListDao.selectCount(queryWrapper) > 0) {
            return true;
        }
        return false;
    }
}

