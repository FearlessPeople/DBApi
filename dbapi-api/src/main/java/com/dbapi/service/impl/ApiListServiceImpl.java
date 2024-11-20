package com.dbapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbapi.dao.ApiListDao;
import com.dbapi.entity.ApiList;
import com.dbapi.service.ApiListService;
import org.springframework.stereotype.Service;

/**
 * (ApiList)表服务实现类
 *
 * @author zfang
 * @time 2024-10-24 14:29:42
 */
@Service("apiListService")
public class ApiListServiceImpl extends ServiceImpl<ApiListDao, ApiList> implements ApiListService {

}

