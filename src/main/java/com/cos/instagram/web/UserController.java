package com.cos.instagram.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.cos.instagram.config.auth.LoginUserAnnotation;
import com.cos.instagram.config.auth.dto.LoginUser;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.domain.user.UserRepository;
import com.cos.instagram.service.UserService;
import com.cos.instagram.web.dto.UserProfileRespDto;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/user/{id}")
	public String profile(@PathVariable int id, @LoginUserAnnotation LoginUser loginUser, Model model) {
	
		UserProfileRespDto userProfileRespDto = userService.회원프로필(id, loginUser);
		model.addAttribute("respDto", userProfileRespDto);
		return "user/profile";
	}
	
	@GetMapping("/user/profileEdit")
	public String profileEdit(@LoginUserAnnotation LoginUser loginUser, Model model) {
		// 모델에 해당 user 정보 들고가야함
		User userEntity = userService.회원정보(loginUser);
		model.addAttribute("user", userEntity);
		return "user/profile-edit";
	}
	
	@PutMapping("/user")
	public ResponseEntity<?> profileUpdate(User user) {
		// 모델에 해당 user 정보 들고가야함
		userService.회원정보수정(user);
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}
}
