package com.dbapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbapi.dao.SysPermissionsDao;
import com.dbapi.entity.SysPermissions;
import com.dbapi.service.SysPermissionsService;
import org.springframework.stereotype.Service;

/**
 * 权限表(SysPermissions)表服务实现类
 *
 * @author zfang
 * @time 2024-09-11 11:23:34
 */
@Service("sysPermissionsService")
public class SysPermissionsServiceImpl extends ServiceImpl<SysPermissionsDao, SysPermissions> implements SysPermissionsService {

}

