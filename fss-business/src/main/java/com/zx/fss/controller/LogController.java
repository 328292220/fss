package com.zx.fss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class LogController {
    @RequestMapping("log")
    public String log(){
        return "log";
    }
}
