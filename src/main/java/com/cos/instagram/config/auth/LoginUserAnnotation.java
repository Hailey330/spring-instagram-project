package com.cos.instagram.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 메소드에 파라미터로만 들어가야함
@Retention(RetentionPolicy.RUNTIME) // 스프링 서버 실행할 때부터 활성화
public @interface LoginUserAnnotation {

}
