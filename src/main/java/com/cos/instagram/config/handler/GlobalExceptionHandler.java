package com.cos.instagram.config.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.instagram.config.handler.ex.MyImageIdNotFoundException;
import com.cos.instagram.config.handler.ex.MyUserIdNotFoundException;
import com.cos.instagram.config.handler.ex.MyUsernameNotFoundException;
import com.cos.instagram.util.Script;

@RestController
@ControllerAdvice // 모든 exception 낚아채기

public class GlobalExceptionHandler {
	
	@ExceptionHandler(value = MyUserIdNotFoundException.class)
	public String myUserIdNotFoundException(Exception e) {
		return Script.back(e.getMessage());
	}
	
	@ExceptionHandler(value = MyUsernameNotFoundException.class)
	public String myUsernameNotFoundException(Exception e) {
		return Script.alert(e.getMessage());
	}
	
	@ExceptionHandler(value=MyImageIdNotFoundException.class)
	public String myImageIdNotFoundException(Exception e) {
		return Script.alert(e.getMessage());
	}
}
