package com.cos.instagram.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

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
