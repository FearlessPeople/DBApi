package com.dbapi.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbapi.common.HttpStatusEnum;
import com.dbapi.common.Result;
import com.dbapi.exception.ServerException;
import com.dbapi.dao.SysUserDao;
import com.dbapi.entity.SysUser;
import com.dbapi.manager.PermissionsManager;
import com.dbapi.service.FileService;
import com.dbapi.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * (SysUser)表服务实现类
 *
 * @author zfang
 * @time 2024-05-22 15:15:17
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserService {

    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private FileService fileService;
    @Autowired
    private PermissionsManager permissionsManager;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Result register(SysUser user) {
        try {
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", user.getUsername());
            SysUser sysUser = sysUserDao.selectOne(queryWrapper);
            if (null != sysUser) {
                return Result.fail("用户名[" + user.getUsername() + "]已存在，请重新输入~");
            }
            user.setPassword(DigestUtil.md5Hex(user.getPassword()));
            int cnt = sysUserDao.insert(user);
            if (cnt == 1) {
                return Result.success("注册成功，请前往登录~");
            }
            return Result.fail("用户注册过程中出现错误，请联系管理员~");
        } catch (ServerException e) {
            logger.error(e.getMessage());
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.fail("系统异常");
        }
    }


    @Override
    public Result login(SysUser user) {
        try {
            String codeId = user.getCodeId();
            String checkCode = user.getCheckCode();
            if (null == codeId || checkCode == null) {
                return Result.fail(HttpStatusEnum.SUCCESS.getCode(), "验证码输入错误,请重新输入~");
            }
            if (checkCode.equals(redisTemplate.opsForValue().get(codeId))) {
                user.setPassword(DigestUtil.md5Hex(user.getPassword()));
                QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("username", user.getUsername());
                queryWrapper.eq("password", user.getPassword());
                SysUser sysUser = sysUserDao.selectOne(queryWrapper);
                // 验证用户
                SysUser authenticateUser = permissionsManager.authenticate(sysUser);
                return Result.success("登录成功", authenticateUser);
            } else {
                return Result.fail(HttpStatusEnum.SUCCESS.getCode(), "验证码输入错误！");
            }
        } catch (ServerException e) {
            logger.error(e.getMessage());
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.fail("系统异常");
        }
    }

    /**
     * 查询当前用户
     *
     * @return
     */
    @Override
    public Result info() {
        try {
            Integer id = permissionsManager.getCurrentUserId();
            SysUser sysUser = sysUserDao.selectById(id);
            if (null != sysUser) {
                sysUser.setPassword(null);
                SysUser permissionUser = permissionsManager.setUserPermissions(sysUser);
                sysUser.setPermissions(permissionUser.getPermissions());
                return Result.success(sysUser);
            } else {
                return Result.fail("未查到当前用户");
            }
        } catch (ServerException e) {
            logger.error(e.getMessage());
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.fail("系统异常");
        }
    }

    /**
     * 上传头像
     *
     * @param file
     * @return
     */
    @Override
    public Result upload(MultipartFile file) {
        try {
            Result result = fileService.uploadFile(file);
            if (result.getStatus()) {
                Integer id = Integer.valueOf(permissionsManager.getCurrentUserId());
                if (id > 0) {
                    SysUser sysUser = new SysUser();
                    sysUser.setId(id);
                    sysUser.setAvatar(String.valueOf(result.getData()));
                    sysUserDao.updateById(sysUser);
                }
            }
            return result;
        } catch (ServerException e) {
            logger.error(e.getMessage());
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.fail("系统异常");
        }
    }

    @Override
    public Result findAll() {
        try {
            testRestTemplate();
            List<SysUser> sysUsers = sysUserDao.selectList(new QueryWrapper<>());
            return Result.success(sysUsers);
        } catch (ServerException e) {
            logger.error(e.getMessage());
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.fail("系统异常");
        }
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @Override
    public Result delete(Integer id) {
        try {
            SysUser deleteUser = new SysUser();
            deleteUser.setId(id);
            deleteUser.setIsDelete(0);
            int i = sysUserDao.updateById(deleteUser);
            if (i > 0) {
                return Result.success("删除成功~");
            } else {
                return Result.fail("删除失败~");
            }
        } catch (ServerException e) {
            logger.error(e.getMessage());
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.fail("系统异常");
        }
    }

    /**
     * 更新用户
     *
     * @param user
     * @return
     */
    @Override
    public Result update(SysUser user) {
        try {
            if (null != user) {
                if (null != user.getPassword() && user.getPassword().trim() != "") {
                    user.setPassword(DigestUtil.md5Hex(user.getPassword()));
                }
                int i = sysUserDao.updateById(user);
                if (i > 0) {
                    return Result.success("更新成功");
                } else {
                    return Result.fail("更新失败");
                }
            } else {
                return Result.fail("获取用户信息失败！");
            }
        } catch (ServerException e) {
            logger.error(e.getMessage());
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.fail("系统异常");
        }
    }

    /**
     * 切换用户状态
     *
     * @param id
     * @return
     */
    @Override
    public Result switchStatus(Integer id) {
        try {
            SysUser sysUser = sysUserDao.selectById(id);
            if (null != sysUser) {
                SysUser user = new SysUser();
                user.setId(id);
                user.setStatus(sysUser.getStatus() == 1 ? 0 : 1);
                int i = sysUserDao.updateById(user);
                if (i > 0) {
                    return Result.success("更新成功");
                } else {
                    return Result.fail("更新失败");
                }
            } else {
                return Result.fail("切换失败");
            }
        } catch (ServerException e) {
            logger.error(e.getMessage());
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.fail("系统异常");
        }
    }

    /**
     * 分页查询
     *
     * @param page
     * @param user
     * @return
     */
    @Override
    public Page<SysUser> selectPage(Page<SysUser> page, SysUser user) {
        return this.sysUserDao.selectPage(page, user);
    }

    private void testRestTemplate() {
        // 创建 RestTemplate 实例
        RestTemplate restTemplate = new RestTemplate();

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8");
        headers.add("Accept-Language", "zh-CN,zh;q=0.6");
        headers.add("Cache-Control", "max-age=0");
        headers.add("Connection", "keep-alive");
        headers.add("Sec-Fetch-Dest", "document");
        headers.add("Sec-Fetch-Mode", "navigate");
        headers.add("Sec-Fetch-Site", "none");
        headers.add("Sec-Fetch-User", "?1");
        headers.add("Sec-GPC", "1");
        headers.add("Upgrade-Insecure-Requests", "1");
        headers.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36");
        headers.add("sec-ch-ua", "\"Chromium\";v=\"128\", \"Not;A=Brand\";v=\"24\", \"Brave\";v=\"128\"");
        headers.add("sec-ch-ua-mobile", "?0");
        headers.add("sec-ch-ua-platform", "\"macOS\"");

        // 创建 HttpEntity，包含请求头
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 发送请求
        String url = "https://myip.ipip.net/";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // 输出响应结果
        System.out.println(response.getBody());
    }


    @Override
    public Result checkToken(String token) {
        try {
            if (permissionsManager.verifyToken(token)) {
                return Result.success();
            } else {
                return Result.fail("登录失效，请重新登录~");
            }
        } catch (ServerException e) {
            logger.error(e.getMessage());
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.fail("系统异常");
        }
    }

    /***
     * 退出登录
     * @return
     */
    @Override
    public Result logout() {
        return Result.success("退出成功");
    }

}

