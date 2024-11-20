package com.dbapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbapi.dao.SysDatasourceDao;
import com.dbapi.entity.SysDatasource;
import com.dbapi.service.SysDatasourceService;
import org.springframework.stereotype.Service;

/**
 * (SysDatasource)表服务实现类
 *
 * @author zfang
 * @time 2024-10-12 14:35:16
 */
@Service("sysDatasourceService")
public class SysDatasourceServiceImpl extends ServiceImpl<SysDatasourceDao, SysDatasource> implements SysDatasourceService {

}

