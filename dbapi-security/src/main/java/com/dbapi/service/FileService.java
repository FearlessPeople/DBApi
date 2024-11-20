package com.dbapi.service;

import com.dbapi.common.Result;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传下载服务类
 */
public interface FileService {

    Result uploadFile(MultipartFile file);

    Resource downloadFile(@RequestParam String path);
}
