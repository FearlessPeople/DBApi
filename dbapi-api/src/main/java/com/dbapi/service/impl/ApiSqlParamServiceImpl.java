package com.dbapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbapi.dao.ApiSqlParamDao;
import com.dbapi.entity.ApiSqlParam;
import com.dbapi.service.ApiSqlParamService;
import org.springframework.stereotype.Service;

/**
 * 接口SQL参数(ApiSqlParam)表服务实现类
 *
 * @author zfang
 * @time 2024-11-12 15:26:36
 */
@Service("apiSqlParamService")
public class ApiSqlParamServiceImpl extends ServiceImpl<ApiSqlParamDao, ApiSqlParam> implements ApiSqlParamService {

}

