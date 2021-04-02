package com.yy.xh.example.web.controller;

import com.yy.xh.example.logging.LogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.xml.ws.ResponseWrapper;

/**
 * DemoController
 *
 * @Date 2021/3/11
 * @Author Dandy
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Resource
    private LogService logService;

    @RequestMapping("/sayHi")
    public String demo(String word) {
        return "hi " + word;
    }

    @RequestMapping("/log")
    public Response tryPrintLog(Integer times) {
        return logService.printLog(times);
    }

    @RequestMapping("/process")
    public boolean process(Integer concurrency, Integer times) {
        logService.multiProcess(concurrency, times);
        return true;
    }

    @RequestMapping("/print")
    public boolean print(String name) {
        logService.print(name);
        return true;
    }
}
