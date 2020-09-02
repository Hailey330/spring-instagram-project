package com.cos.instagram.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.instagram.service.UserService;
import com.cos.instagram.web.dto.JoinReqDto;

import lombok.RequiredArgsConstructor;

//인증 안된 유저들이 들어올 수 있는 페이지 : 회원가입, 로그인
@RequiredArgsConstructor
@Controller
public class AuthController {

	// log + ctrl + spaceBar
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	private final UserService userService;

	@GetMapping("/auth/loginForm")
	public String loginForm() {
		log.info("/auth/loginForm 진입");
		return "auth/loginForm";
	}

	@GetMapping("/auth/joinForm")
	public String joinForm() {
		log.info("/auth/joinForm 진입");
		return "auth/joinForm";
	}

	@PostMapping("/auth/join")
	public String join(JoinReqDto joinReqDto) { // form은 스프링에서 기본적인 파싱이 가능함. (@RequestBody 안걸어도 됨)
		log.info(joinReqDto.toString());
		userService.회원가입(joinReqDto); // DataBase Connection 켜져있음
		return "redirect:/auth/loginForm"; // 인스타그램 로그인 페이지
	}
}
