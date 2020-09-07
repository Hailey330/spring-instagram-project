package com.cos.instagram.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.cos.instagram.web.dto.UserProfileEditRespDto;
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
		Optional<User> userEntity = userRepository.findByUsername(loginUser.getUsername()); 
		model.addAttribute("userProfile", userEntity);
		return "user/profile-edit";
	}
	
	@PutMapping("/user/profileEditProc")
	public String profileEditProc(@LoginUserAnnotation LoginUser loginUser, Model model) {
		// 모델에 해당 user 정보 들고가야함
		userService.회원정보수정(loginUser.getId());
//		User userEntity = userRepository.mUpdateUserProfile(userEntity, loginUser.getId());
		
//		model.addAttribute("userProfileUpdate", userEntity);
		return "user/profile-edit";
	}
}
