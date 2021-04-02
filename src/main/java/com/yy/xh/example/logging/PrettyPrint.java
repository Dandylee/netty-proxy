package com.yy.xh.example.logging;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PrettyPrint {
    String value() default "json";
}
