package com.yy.xh.example.web.controller;

import lombok.experimental.UtilityClass;

/**
 * Data
 *
 * @Date 2021/3/17
 * @Author Dandy
 */
@UtilityClass
public class Data {

    private String category = "aa";

    public String print(String name){
        System.out.println("hi" + name);
        return name;
    }

    public static void main(String[] args) {
        System.out.println(Data.category);
        System.out.println(Data.print("dandy"));
    }
}
