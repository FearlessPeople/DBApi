package com.dbapi.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.ShearCaptcha;
import com.dbapi.constants.Constants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 验证码控制层
 *
 * @author zfang
 * @time 2024-05-22 15:15:38
 */
@RequestMapping("/gv")
@RestController
public class CaptchaController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 给前端返回一个验证码图片
     *
     * @return
     */
    @ApiOperation("获取图形验证码")
    @GetMapping("/identifyImage")
    public void identifyImage(
            HttpServletResponse response,
            @ApiParam(value = "图形验证码id,无值：生成验证码，有值:刷新验证码")
            @RequestParam(name = "codeId", required = false) String codeId,
            // 新增的 type 参数
            @ApiParam(value = "验证码类型")
            @RequestParam(name = "type", required = false) String type) throws IOException {
        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(300, 90);
        // 验证码值
        String code = shearCaptcha.getCode();

        // 如果没有传入 codeId 或 codeId 是 undefined，则生成一个新的 UUID 作为 codeId
        if (codeId == null || Constants.Authorization.equals(codeId)) {
            // 生成唯一的 captchaId
            codeId = UUID.randomUUID().toString();
        }

        // 将验证码值存入 Redis，设置 5 分钟有效期（5 分钟后自动过期）
        redisTemplate.opsForValue().set(codeId, code, 5, TimeUnit.MINUTES);
        // 设置响应类型为 PNG 图片
        response.setContentType("image/png");
        // 图形验证码对应的UUID,id和验证码一一对应，用于刷新、验证
        response.setHeader("codeId", codeId);
        // 输出到客户端
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            shearCaptcha.write(outputStream);
            outputStream.flush();  // 确保数据完全写出
        } catch (Exception e) {
            System.out.println("图形验证码输出错误");
        }
    }

    /**
     * 给前端返回一个base64格式的图片
     *
     * @return
     */
    @ApiOperation("获取图形验证码base64")
    @GetMapping("/identifyImageBase64")
    public String identifyImageBase64() {
        // 定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 90, 4, 100);
        // 验证码值
        String code = lineCaptcha.getCode();
        System.out.println("验证码值：" + code);
        // 验证码以base64的格式返回到客户端
        return lineCaptcha.getImageBase64Data();
    }

    /**
     * 校验图形验证码
     *
     * @param request 请求对象，包含用户输入的验证码和codeId
     * @return true 如果验证码校验成功，false 反之
     */
    @GetMapping("/verifyCaptcha")
    public boolean verifyCaptcha(HttpServletRequest request,
                                 @RequestParam("codeId") String codeId,
                                 @RequestParam("captcha") String inputCaptcha) {
        // 从 Redis 中获取验证码值
        String storedCaptcha = redisTemplate.opsForValue().get(codeId).toString();

        // 校验验证码
        boolean isValid = storedCaptcha != null && storedCaptcha.equals(inputCaptcha);

        // 如果验证码校验成功，且需要的话，可以在这里删除 Redis 中的验证码，以避免重复使用
        if (isValid) {
            redisTemplate.delete(codeId);
        }

        return isValid;
    }

}
