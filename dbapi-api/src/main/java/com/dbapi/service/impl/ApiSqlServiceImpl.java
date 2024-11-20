package com.dbapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbapi.dao.ApiGroupDao;
import com.dbapi.dao.ApiSqlDao;
import com.dbapi.dao.ApiSqlParamDao;
import com.dbapi.entity.ApiSql;
import com.dbapi.entity.ApiSqlParam;
import com.dbapi.exception.ServerException;
import com.dbapi.service.ApiSqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;


/**
 * 接口SQL(ApiSql)表服务实现类
 *
 * @author zfang
 * @time 2024-10-24 14:29:56
 */
@Service("apiSqlService")
public class ApiSqlServiceImpl extends ServiceImpl<ApiSqlDao, ApiSql> implements ApiSqlService {
    @Autowired
    private ApiSqlDao apiSqlDao;

    @Autowired
    private ApiSqlParamDao apiSqlParamDao;

    /**
     * 替换SQL中的参数
     *
     * @param apiSql
     * @return
     */
    @Override
    public String replaceSqlParam(ApiSql apiSql) {
        if (apiSql != null) {
            QueryWrapper<ApiSqlParam> queryWrapperParam = new QueryWrapper<>();
            queryWrapperParam.eq("api_id", apiSql.getApiId());
            List<ApiSqlParam> apiSqlParams = apiSqlParamDao.selectList(queryWrapperParam);
            return processSqlTemplate(apiSql.getApiSql(), apiSqlParams);
        } else {
            throw new ServerException("接口不存在");
        }
    }

    /**
     * 替换SQL中的参数
     *
     * @param apiSql
     * @param apiSqlParams
     * @return
     */
    @Override
    public String replaceSqlParam(String apiSql, List<ApiSqlParam> apiSqlParams) {
        if (apiSqlParams == null || apiSqlParams.isEmpty()) {
            return apiSql;
        }
        if (apiSql == null) {
            return null;
        }
        return processSqlTemplate(apiSql, apiSqlParams);
    }

    /**
     * 替换SQL模板中的${}变量，返回可执行的最终SQL语句
     *
     * @param sqlTemplate
     * @param apiSqlParams
     * @return
     */
    public static String processSqlTemplate(String sqlTemplate, List<ApiSqlParam> apiSqlParams) {
        StringBuilder processedSql = new StringBuilder(sqlTemplate);

        // 遍历参数列表，根据 paramName 替换对应的 ${paramName} 为实际的 paramValue
        for (ApiSqlParam apiSqlParam : apiSqlParams) {
            String placeholder = "${" + apiSqlParam.getParamName() + "}";
            String paramValue = formatValue(apiSqlParam);
            int index;
            while ((index = processedSql.indexOf(placeholder)) != -1) {
                processedSql.replace(index, index + placeholder.length(), paramValue);
            }
        }

        return processedSql.toString();
    }

    /**
     * 格式化值以适应SQL语句（为字符串加单引号，为数值、日期等直接转换）
     *
     * @param apiSqlParam
     * @return
     */
    private static String formatValue(ApiSqlParam apiSqlParam) {
        switch (apiSqlParam.getParamType()) {
            case 1: // 字符串，添加单引号
                return "'" + apiSqlParam.getParamValue().replace("'", "''") + "'";
            case 2: // 数值，直接返回
                return apiSqlParam.getParamValue();
            case 3: // 日期，假设格式为 yyyy-MM-dd，添加单引号
                return "'" + apiSqlParam.getParamValue() + "'";
            case 4: // SQL表达式
                return apiSqlParam.getParamValue();
            default:
                throw new IllegalArgumentException("Unsupported parameter type: " + apiSqlParam.getParamType());
        }
    }
}

