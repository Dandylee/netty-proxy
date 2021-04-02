package com.yy.xh.example.web.controller;

import lombok.Data;

/**
 * Response
 *
 * @Date 2021/3/12
 * @Author Dandy
 */
@Data
public class Response {
    private int code;
    private String message;
    private boolean success;

    public Response(){
    }

    public Response(int code, String message, boolean success) {
        this.code = code;
        this.message = message;
        this.success = success;
    }
}
