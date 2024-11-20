package com.dbapi.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbapi.common.DatabaseType;
import com.dbapi.common.Result;
import com.dbapi.entity.SysDatasource;
import com.dbapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * (DbApiDatasource)表控制层
 *
 * @author zfang
 * @time 2024-10-12 14:35:12
 */
@RestController
@RequestMapping("db")
public class SysDatasourceController {
    /**
     * 服务对象
     */
    @Resource
    private SysDatasourceService sysDatasourceService;

    @Autowired
    private DataSourceManager dataSourceManager;

    @Autowired
    private SqlExecutor sqlExecutor;


    /**
     * 获取所有列表数据
     *
     * @return Result对象，其中包含数据源服务的列表数据
     */
    @GetMapping("/allList")
    public Result allList() {
        List<SysDatasource> list = sysDatasourceService.list();
        return Result.success(list);
    }

    /**
     * 分页查询所有数据
     *
     * @param page          分页对象
     * @param sysDatasource 查询实体
     * @return 所有数据
     */
    @PostMapping("/list")
    public Result list(Page<SysDatasource> page, SysDatasource sysDatasource) {

        // 非空检查
        if (page == null || sysDatasource == null) {
            throw new IllegalArgumentException("分页对象Page不能为空");
        }
        QueryWrapper<SysDatasource> queryWrapper = buildQueryWrapper(sysDatasource);

        try {
            return Result.success(sysDatasourceService.page(page, queryWrapper));
        } catch (Exception e) {
            return Result.fail("获取数据源列表失败：" + e.getMessage());
        }
    }

    /**
     * 构造查询条件
     *
     * @param sysDatasource
     * @return
     */
    private QueryWrapper<SysDatasource> buildQueryWrapper(SysDatasource sysDatasource) {
        QueryWrapper<SysDatasource> queryWrapper = new QueryWrapper<>();
        if (sysDatasource.getId() != null) {
            queryWrapper.eq("id", sysDatasource.getId());
        }
        if (!StrUtil.isBlank(sysDatasource.getName())) {
            queryWrapper.like("name", sysDatasource.getName());
        }
        if (!StrUtil.isBlank(sysDatasource.getRemark())) {
            queryWrapper.like("remark", sysDatasource.getRemark());
        }
        return queryWrapper;
    }

    /**
     * 添加数据源
     *
     * @param sysDatasource
     * @return
     */
    @PostMapping("/add")
    public Result addDataSource(@RequestBody SysDatasource sysDatasource) {
        if (sysDatasource == null || sysDatasource.getName() == null || sysDatasource.getJdbcUrl() == null || sysDatasource.getUsername() == null || sysDatasource.getPassword() == null) {
            return Result.fail("请求参数不完整，请检查输入");
        }
        QueryWrapper<SysDatasource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", sysDatasource.getName());
        SysDatasource db = sysDatasourceService.getOne(queryWrapper);
        if (db == null) {
            Boolean aBoolean = dataSourceManager.testConnection(sysDatasource.getJdbcUrl(), sysDatasource.getUsername(), sysDatasource.getPassword());
            if (aBoolean) {
                boolean saved = sysDatasourceService.save(sysDatasource);
                if (saved) {
                    dataSourceManager.addDataSource(sysDatasource.getId(), sysDatasource.getJdbcUrl(), sysDatasource.getUsername(), sysDatasource.getPassword());
                    return Result.success("数据源添加成功~");
                } else {
                    return Result.fail("数据源添加失败，请检查数据库配置");
                }
            } else {
                return Result.fail("数据源连接失败，请检查数据库配置");
            }
        } else {
            return Result.fail("数据源名称" + sysDatasource.getName() + "已存在，不允许重复添加");
        }
    }

    /**
     * 更新数据源
     *
     * @param sysDatasource 更新的数据源对象
     * @return 更新结果
     */
    @PostMapping("/edit")
    public Result editDataSource(@RequestBody SysDatasource sysDatasource) {
        if (sysDatasource == null || sysDatasource.getName() == null || sysDatasource.getJdbcUrl() == null || sysDatasource.getUsername() == null || sysDatasource.getPassword() == null) {
            return Result.fail("请求参数不完整，请检查输入");
        }

        // 校验数据源是否存在
        SysDatasource existingDataSource = sysDatasourceService.getById(sysDatasource.getId());
        if (existingDataSource == null) {
            return Result.fail("数据源不存在");
        }

        // 更新数据源信息
        sysDatasource.setId(sysDatasource.getId());
        boolean updated = sysDatasourceService.updateById(sysDatasource);

        if (updated) {
            // 更新数据源管理器中的数据源信息
            dataSourceManager.updateDataSource(sysDatasource.getId(), sysDatasource.getName(), sysDatasource.getJdbcUrl(), sysDatasource.getUsername(), sysDatasource.getPassword());
            return Result.success("数据源更新成功~");
        } else {
            return Result.fail("数据源更新失败，请检查数据库配置");
        }
    }


    /**
     * 测试数据源
     *
     * @param sysDatasource
     * @return
     */
    @PostMapping("/test")
    public Result testDataSource(@RequestBody SysDatasource sysDatasource) {
        try {
            if (sysDatasource == null || sysDatasource.getName() == null || sysDatasource.getJdbcUrl() == null ||
                    sysDatasource.getUsername() == null || sysDatasource.getPassword() == null) {
                return Result.fail("请求参数不完整，请检查输入");
            }

            // 调用测试连接方法
            Boolean testd = dataSourceManager.testConnection(
                    sysDatasource.getJdbcUrl(),
                    sysDatasource.getUsername(),
                    sysDatasource.getPassword()
            );
            if (testd) {
                return Result.success("数据源连接成功~");
            } else {
                return Result.fail("数据源连接失败~");
            }
        } catch (Exception e) {
            return Result.fail("500", "Internal server error: " + e.getMessage());
        }
    }

    @GetMapping("/tables")
    public Result getTables(@RequestParam int id) {
        DataSourceProvider provider = dataSourceManager.getDataSourceProvider(id);
        return Result.success(provider.getTables());
    }

    @GetMapping("/columns")
    public Result getColumns(@RequestParam int id, @RequestParam String tableName) {
        DataSourceProvider provider = dataSourceManager.getDataSourceProvider(id);
        List<Map<String, Object>> columns = provider.getColumns(tableName);
        return Result.success(columns);
    }

    /**
     * SQL查询
     *
     * @param id  数据库连接标识符
     * @param sql 要执行的SQL查询语句
     * @return 查询结果，包括成功与否和数据内容
     * @throws SQLException 数据库访问错误
     */
    @PostMapping("/execute")
    public Result executeQuery(@RequestParam Integer id,
                               @RequestParam String sql) {
        try {
            // 调用sqlExecutor执行查询，并将结果封装进Result对象中
            List<Map<String, Object>> list = sqlExecutor.executeQuery(id, sql);
            return Result.success(list);
        } catch (IllegalArgumentException e) {
            return Result.fail("400", e.getMessage());
        } catch (SQLException e) {
            return Result.fail("500", "SQL execution error: " + e.getMessage());
        } catch (Exception e) {
            return Result.fail("500", "Internal server error: " + e.getMessage());
        }
    }

}

