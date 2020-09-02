package com.cos.instagram.test;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.instagram.config.auth.PrincipalDetails;

@Controller
public class TestController {

	@GetMapping("/test/facebook2")
	public @ResponseBody String facebook2(@AuthenticationPrincipal UserDetails principal) {
		// UserDetails를 상속한 AuthenticationPrincipal 
		// PrincipalDetails 대신에 UserDetails 를 적으면 getUser() 사용 불가
		System.out.println(principal.getUsername()); 
		return "facebook2 로그인 완료";
	}
	
	@GetMapping("/test/facebook")
	public @ResponseBody String facebook(Authentication authentication) {
		System.out.println("authentication : " + authentication.getPrincipal());
		System.out.println("authentication : " + authentication.getDetails());
		OAuth2User oauth2user = (OAuth2User) authentication.getPrincipal(); // Object Type → 다운 캐스팅
		System.out.println("authentication : " + oauth2user.getAttributes());
//		System.out.println("authentication : " + principalDetails.getUser());
		return "facebook 로그인 완료";
	}
	
	@GetMapping("/test/login")
	public String test1() {
		return "auth/login";
	}

	@GetMapping("/test/join")
	public String test2() {
		return "auth/join";
	}

	@GetMapping("/test/following")
	public String test3() {
		return "follow/following";
	}

	@GetMapping("/test/explore")
	public String test4() {
		return "image/explore";
	}

	@GetMapping("/test/feed")
	public String test5() {
		return "image/feed";
	}

	@GetMapping("/test/upload")
	public String test6() {
		return "image/image-upload";
	}

	@GetMapping("/test/proedit")
	public String test7() {
		return "user/profile-edit";
	}

	@GetMapping("/test/profile")
	public String test8() {
		return "user/profile";
	}

}
