package com.dbapi.service.impl;

import com.dbapi.service.FileService;
import com.dbapi.common.Result;
import com.dbapi.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传下载服务类
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileUtil fileUtil;

    @Override
    public Result uploadFile(MultipartFile file) {
        String filePath = fileUtil.upload(file);
        if (null == filePath) {
            return Result.fail("上传失败.");
        }
        return Result.success("上传成功", filePath);
    }

    @Override
    public Resource downloadFile(String path) {
        return fileUtil.download(path);
    }
}
