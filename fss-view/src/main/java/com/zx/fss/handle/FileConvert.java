package com.zx.fss.handle;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
@Documented
public @interface FileConvert {
    String value() default ""; //该值即key值
}
