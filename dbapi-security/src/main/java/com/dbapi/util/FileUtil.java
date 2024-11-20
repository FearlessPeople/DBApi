package com.dbapi.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.dbapi.exception.ServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class FileUtil {

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 上传文档
     *
     * @param file
     * @return
     */
    public String upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ServerException("请至少选择一个要上传的文件.");
        }

        String fileUrl = null;

        try {
            // 获取上传的文件并保存起来
            byte[] bytes = file.getBytes();
            String originalFilename = file.getOriginalFilename();
            long currentTimeStamp = DateUtil.current();
            String randomString = RandomUtil.randomString(10);
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String randomFileName = randomString + "_" + currentTimeStamp + fileExtension;

            // 获取当前日期并格式化为目录名
            String dateDir = new SimpleDateFormat("yyyyMMdd").format(new Date());
            Path datePath = Paths.get(uploadDir, dateDir);

            // 如果目录不存在，则创建
            if (!Files.exists(datePath)) {
                Files.createDirectories(datePath);
            }

            // 保存文件到日期目录下
            Path filePath = datePath.resolve(randomFileName);
            Files.write(filePath, bytes);

            StringBuilder fileUrlBuilder = new StringBuilder();
            fileUrlBuilder.append("/files/")
                    .append(dateDir)
                    .append("/")
                    .append(randomFileName);
            fileUrl = fileUrlBuilder.toString();
        } catch (Exception e) {
            System.err.println(e);
        }
        return fileUrl;
    }

    /**
     * 下载文件
     *
     * @param path
     * @return
     */
    public Resource download(String path) {
        File file = new File(uploadDir + path);
        if (!file.exists()) {
            throw new ServerException("文件未找到");
        }
        try {
            UrlResource resource = new UrlResource(file.toURI());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new ServerException("文件不可读");
            }
        } catch (MalformedURLException e) {
            throw new ServerException("无效的文件URL");
        }
    }
}
