package com.cos.instagram.service;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.instagram.config.auth.dto.LoginUser;
import com.cos.instagram.config.handler.ex.MyUserIdNotFoundException;
import com.cos.instagram.domain.image.Image;
import com.cos.instagram.domain.image.ImageRepository;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.domain.user.UserRepository;
import com.cos.instagram.web.dto.JoinReqDto;
import com.cos.instagram.web.dto.UserProfileRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final ImageRepository imageRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Transactional
	public void 회원가입(JoinReqDto joinReqDto) {
		String encPassword = bCryptPasswordEncoder.encode(joinReqDto.getPassword());
		joinReqDto.setPassword(encPassword);
		userRepository.save(joinReqDto.toEntity());
	}
	
	// 읽기 전용 트랜잭션 - DB 변경 불가능
	// (1) 변경 감지 연산을 하지 않음
	// (2) isoloation(고립성)을 위해 
	@Transactional(readOnly = true)
	public UserProfileRespDto 회원프로필(int id, LoginUser loginUser) {
				
		int imageCount;
		int followerCount;
		int followingCount;
		
		// 1. User 찾기 
		User userEntity = userRepository.findById(id)
				.orElseThrow(new Supplier<MyUserIdNotFoundException>() { // user를 못찾았을 때 throw
					@Override
					public MyUserIdNotFoundException get() {
						return new MyUserIdNotFoundException();
					}					
				});
		// 2. 이미지 카운트
		List<Image> imagesEntity = imageRepository.findByUserId(id); // 해당 페이지의 UserId
		imageCount = imagesEntity.size();
	
		// 3. 팔로우 수 (수정해야함)
		followerCount = 50;
		followingCount = 100;
	
		// 4. 이미지들
		
		// 5. 최종 마무리
		UserProfileRespDto userProfileRespDto = 
				UserProfileRespDto.builder()
				.pageHost(id==loginUser.getId())
				.user(userEntity)
				.images(imagesEntity) // 수정 필요(댓글 수, 좋아요 수)
				.imageCount(imageCount)
				.followerCount(followerCount)
				.followingCount(followingCount)
				.build();
	
		return userProfileRespDto;
	}

}
