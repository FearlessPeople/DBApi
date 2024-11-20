package com.dbapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dbapi.entity.SysDatasource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据源管理类
 */
@Service
public class DataSourceManager {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceManager.class);

    private final Map<Integer, HikariDataSource> dataSources = new ConcurrentHashMap<>();

    @Autowired
    private SysDatasourceService sysDatasourceService;

    /**
     * 对数据源的初始化
     */
    @PostConstruct
    public void init() {
        List<SysDatasource> dataSourceList = sysDatasourceService.list();
        for (SysDatasource entity : dataSourceList) {
            try {
                addDataSource(entity.getId(), entity.getJdbcUrl(), entity.getUsername(), entity.getPassword());
            } catch (Exception e) {
                logger.error("Failed to initialize data source [" + entity.getName() + "]: " + e.getMessage(), e);
            }
        }
    }

    /**
     * 测试数据源连接是否OK
     *
     * @param jdbcUrl  JDBC URL
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public Boolean testConnection(String jdbcUrl, String username, String password) {
        HikariConfig config = createHikariConfig(jdbcUrl, username, password);
        try (HikariDataSource testDataSource = new HikariDataSource(config)) {
            return !testDataSource.getConnection().isClosed();
        } catch (SQLException e) {
            logger.error("Connection test failed: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * 添加数据源之前，检查是否已经存在一个同名的数据源，如果存在，则先关闭旧的数据源以避免资源泄露。
     *
     * @param jdbcUrl  JDBC URL
     * @param username 用户名
     * @param password 密码
     */
    public void addDataSource(Integer id, String jdbcUrl, String username, String password) {
        closeDataSource(id); // 关闭同名旧数据源
        HikariConfig config = createHikariConfig(jdbcUrl, username, password);
        HikariDataSource dataSource = new HikariDataSource(config);
        dataSources.put(id, dataSource);
    }

    /**
     * 更新指定 ID 的数据源。
     *
     * @param id       数据源 ID
     * @param name     数据源名称
     * @param jdbcUrl  JDBC URL
     * @param username 用户名
     * @param password 密码
     */
    public void updateDataSource(Integer id, String name, String jdbcUrl, String username, String password) {
        // 确认数据源存在
        if (!dataSources.containsKey(id)) {
            throw new IllegalArgumentException("Data source with ID " + id + " does not exist.");
        }

        this.addDataSource(id, jdbcUrl, username, password);
    }

    /**
     * 调用 closeDataSource 方法来正确关闭连接池，释放资源。
     *
     * @param id
     */
    public void closeDataSource(Integer id) {
        HikariDataSource dataSource = dataSources.remove(id);
        if (dataSource != null) {
            dataSource.close();
        }
    }

    /**
     * 根据id获取数据源
     *
     * @param id
     * @return
     */
    public DataSource getDataSource(Integer id) {
        return dataSources.get(id);
    }

    /**
     * 根据ID获取数据源提供者
     *
     * @param id
     * @return
     */
    public DataSourceProvider getDataSourceProvider(Integer id) {
        HikariDataSource dataSource = dataSources.get(id);
        SysDatasource sysDatasource = sysDatasourceService.getById(id);
        if (dataSource == null || sysDatasource == null) {
            throw new IllegalArgumentException("Data source not found with ID: " + id);
        }
        return DataSourceProviderFactory.createProvider(sysDatasource.getDbType(), dataSource);
    }

    /**
     * 在应用关闭时，通过 @PreDestroy 注解确保所有数据源都被关闭，防止数据库连接占用以及内存泄露。
     */
    @PreDestroy
    public void shutdown() {
        for (Map.Entry<Integer, HikariDataSource> entry : dataSources.entrySet()) {
            try {
                entry.getValue().close();
            } catch (Exception e) {
                logger.error("Failed to close data source: " + entry.getKey(), e);
            }
        }
    }

    /**
     * 创建并配置 Hikari 数据源
     */
    private HikariConfig createHikariConfig(String jdbcUrl, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(30000);    // 30 seconds
        config.setIdleTimeout(600000);         // 10 minutes
        config.setMaxLifetime(1800000);        // 30 minutes
        config.setConnectionTestQuery("SELECT 1"); // Validation query
        return config;
    }
}
