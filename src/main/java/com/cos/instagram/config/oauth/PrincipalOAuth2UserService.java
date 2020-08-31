package com.cos.instagram.config.oauth;

import java.util.function.Supplier;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.instagram.config.auth.PrincipalDetails;
import com.cos.instagram.config.auth.dto.LoginUser;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.domain.user.UserRepository;
import com.cos.instagram.domain.user.UserRole;

@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

	private static final Logger log = LoggerFactory.getLogger(PrincipalOAuth2UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private HttpSession session;
	
	@Value("${cos.secret}")
	private String cosSecret; // application.yml에 적어두고 비밀번호 숨겨놓기

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info(userRequest.toString());
		log.info(userRequest.getAccessToken().getTokenValue());
		log.info(userRequest.getAccessToken().getExpiresAt().toString());
		log.info(userRequest.getClientRegistration().toString());
		// ↑ Registration 정보만 있으면 구글, 페이스북, 네이버 중 어떤 것으로 로그인했는지 알 수 있음

		// userRequest로 페이스북 자원에 접근해서 데이터를 끌어올 수 있음
		// 이걸 실행해주는 것이 super.loadUser()
		// OAuth 서버에 내 서버 정보와 AccessToken을 던져서 회원 프로필 정보를 OAuth2User타입으로 받아온다.
		OAuth2User oAuth2User = super.loadUser(userRequest);
		System.out.println("OAuth2User : " + oAuth2User.getAttributes());

		// oauth 로 들어오면 맨 처음에 회원 가입을 시켜야 User Obj에 담김
		// 꼭 안담아도 되는데 나중에 어떤 페이지를 갔을 때 user 정보가 필요하다? 하면 세션 정보에 getUser하면 안 나옴
		// 일반 로그인과 OAuth 로그인할 때 꺼내오는게 달라지니까 또 불편함..

		User userEntity = oauthLoginOrJoin(oAuth2User);

		return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
	}

	private User oauthLoginOrJoin(OAuth2User oAuth2User) {
		String provider = "facebook";
		String providerId = oAuth2User.getAttribute("id");
		String username = provider + "_" + providerId;
		String password = bCryptPasswordEncoder.encode(cosSecret);
		String email = oAuth2User.getAttribute("email");
		// 추가 
		String name = oAuth2User.getAttribute("name");
		

		User userEntity = userRepository.findByUsername(username).orElseGet(new Supplier<User>() {
			// .get() 했을 때 못찾으면 exception 터지기 때문에 orElseGet() 사용
			@Override
			public User get() {
				// 회원가입
				User user = User.builder()
						.username(username)
						.name(name)
						.password(password)
						.email(email)
						.role(UserRole.USER)
						.provider(provider)
						.providerId(providerId)
						.build();
				return userRepository.save(user);
			}
		});
		session.setAttribute("loginUser", new LoginUser(userEntity));
		return userEntity;
	}

}
