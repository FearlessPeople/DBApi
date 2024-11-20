package com.dbapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String index() {
        return "forward:/index.html"; // 转发到静态的 index.html 文件
    }
}
