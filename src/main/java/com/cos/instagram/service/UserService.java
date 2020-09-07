package com.cos.instagram.service;

import java.util.List;
import java.util.function.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.instagram.config.auth.dto.LoginUser;
import com.cos.instagram.config.handler.ex.MyUserIdNotFoundException;
import com.cos.instagram.domain.follow.FollowRepository;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.domain.user.UserRepository;
import com.cos.instagram.web.dto.JoinReqDto;
import com.cos.instagram.web.dto.UserProfileImageRespDto;
import com.cos.instagram.web.dto.UserProfileRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	@PersistenceContext // DB Connection 할 수 있는 entity 객체를 넘겨줌
	private EntityManager em; // Entity로 매핑해줌
	private final UserRepository userRepository;
	private final FollowRepository followRepository;
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
		boolean followState;
		
		// 1. User 찾기 
		User userEntity = userRepository.findById(id)
				.orElseThrow(new Supplier<MyUserIdNotFoundException>() { // user를 못찾았을 때 throw
					@Override
					public MyUserIdNotFoundException get() {
						return new MyUserIdNotFoundException();
					}					
				});
		
		// 2. 이미지들과 전체 이미지 카운트(dto 받기)
		StringBuilder sb = new StringBuilder();
		sb.append("select im.id, im.imageUrl, ");
		sb.append("(select count(*) from likes lk where lk.imageId = im.id) as likeCount, ");
		sb.append("(select count(*) from comment ct where ct.imageId = im.id) as commentCount ");
		sb.append("from image im where im.userId = ? "); // 값을 직접 넣으면 injection 공격 받음
		String q = sb.toString();
		Query query = em.createNativeQuery(q, "UserProfileImageRespDtoMapping").setParameter(1, id);
		List<UserProfileImageRespDto> imagesEntity = query.getResultList();
		//em.persist(imagesEntity); - 영속화 

		imageCount = imagesEntity.size();
		
		// 3. 팔로우 수
		followerCount = followRepository.mCountByFollower(id); // 페이지의 주인 id
		followingCount = followRepository.mCountByFollowing(id); // 페이지의 주인 id
		
		// 4. 팔로우 유무 체크
		followState = followRepository.mFollowState(loginUser.getId(), id) == 1 ? true : false;
		
		// 5. 최종 마무리
		UserProfileRespDto userProfileRespDto = 
				UserProfileRespDto.builder()
				.pageHost(id==loginUser.getId())
				.followState(followState)
				.followerCount(followerCount)
				.followingCount(followingCount)
				.imageCount(imageCount)
				.user(userEntity)
				.images(imagesEntity)
				.build();
	
		return userProfileRespDto;
	}
	
	@Transactional
	public User 회원정보수정(int id) {
		
		// 1. 영속화하기 - user 찾기 
		// 2. 업데이트 된 정보 변경하기 
		// 3. Object 에 set값 담으면 자동으로 데이터 update 해줌
		// ==> 더티체킹
		
		
//		userRepository.mUpdateUserProfile(user, loginUserId)
		return null;
	}

}
