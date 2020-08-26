package com.cos.instagram.config.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.instagram.domain.user.User;
import com.cos.instagram.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

	private static final Logger log = LoggerFactory.getLogger(PrincipalDetailsService.class);
	private final UserRepository userRepository;

	// Security Session > Authentication > UserDetails 
	// 정상적으로 리턴되면 @AuthenticationPrincipal 어노테이션 활성화됨!
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("loadUserByUsername : username : " + username);
		// request 시 username, password 받았을 때 데이터베이스에 있는지 없는지 판단함
		User userEntity = userRepository.findByUsername(username).get();
		if (userEntity == null) {
			return null;
		}
		return new PrincipalDetails(userEntity);
	}
}
