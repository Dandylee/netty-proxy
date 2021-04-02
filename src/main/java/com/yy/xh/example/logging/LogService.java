package com.yy.xh.example.logging;

import com.yy.xh.example.web.controller.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * logService
 *
 * @Date 2021/3/11
 * @Author Dandy
 */
@Slf4j
@Service
public class LogService {

    @Resource
    private RestTemplate restTemplate;

    static {
    }

    @PrettyPrint(value = "xml")
    public String print(String name){
        return "xml:" + name;
    }

    public String multiProcess(int concurrency, int times) {

        List<CompletableFuture> features = new ArrayList<>(concurrency);
        for (int i = 0; i < concurrency; i++) {
            CompletableFuture<Response> feature = CompletableFuture.supplyAsync(() -> restTemplate.getForObject("http://localhost:8080/demo/log?times=" + times, Response.class));
            features.add(feature);
        }

        try {
            CompletableFuture.allOf(features.toArray(new CompletableFuture[concurrency])).get();
            return "true";
        } catch (Exception e){
            log.error("multiProcess error");
        }
        return "fail";
    }

    public Response printLog(int times) {
        for (int i = 0; i < times; i++) {
            log.info(i + " times print log");
        }
        return new Response();
    }

}
