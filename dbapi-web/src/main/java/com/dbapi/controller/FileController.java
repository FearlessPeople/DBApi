package com.dbapi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("files")
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/{date}/{filename}")
    public ResponseEntity<byte[]> getFile(@PathVariable String date, @PathVariable String filename) {
        try {
            // 构建文件路径
            File file = new File(uploadDir + "/" + date + "/" + filename);
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // 读取文件内容
            byte[] fileContent = Files.readAllBytes(file.toPath());

            // 设置响应头，返回文件
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", Files.probeContentType(file.toPath())); // 设置文件类型
            headers.add("Content-Disposition", "inline; filename=" + filename); // 使文件在浏览器中打开

            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
