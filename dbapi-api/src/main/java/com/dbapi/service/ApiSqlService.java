package com.dbapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dbapi.entity.ApiSql;
import com.dbapi.entity.ApiSqlParam;

import java.util.List;

/**
 * 接口SQL(ApiSql)表服务接口
 *
 * @author zfang
 * @time 2024-10-24 14:29:56
 */
public interface ApiSqlService extends IService<ApiSql> {
    /**
     * 替换SQL中的参数
     * @param apiSql
     * @return
     */
    String replaceSqlParam(ApiSql apiSql);

    /**
     * 替换SQL中的参数
     * @param apiSql
     * @return
     */
    String replaceSqlParam(String apiSql, List<ApiSqlParam> apiSqlParams);
}

