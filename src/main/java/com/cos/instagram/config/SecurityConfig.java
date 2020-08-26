package com.cos.instagram.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.cos.instagram.config.oauth.PrincipalOAuth2UserService;
import com.cos.instagram.util.Script;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalOAuth2UserService principalOAuth2UserService;
	
	@Bean // SecurityConfig 메모리에 띄울 때 @Bean 붙은  BCryptPasswordEncoder도 같이 띄움
	public BCryptPasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
		.antMatchers("/", "/user/**", "/follow/**", "/image/**")
		.authenticated()
		.antMatchers("/admin/**")
		.access("hasRole('ROLE_ADMIN')")
		.anyRequest()
		.permitAll()
		.and()
		.formLogin()
		.loginPage("/auth/loginForm")  // 누구나 들어올 수 있음
		.loginProcessingUrl("/auth/login")
		.defaultSuccessUrl("/")
		.failureHandler(new AuthenticationFailureHandler() {
			
			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.print(Script.back("아이디 또는 비밀번호를 찾을 수 없습니다."));
				return;
			}
		})
		.and()
		.logout()
		.logoutUrl("/auth/logout")
		.logoutSuccessUrl("/auth/loginForm")
		.and()
		.oauth2Login() // oauth 요청 주소가 다 활성화됨
		.userInfoEndpoint() // oauth 로그인 성공 이후 사용자 정보를 가져오기 위한 설정을 담당함. 혼자 쓸 수는 없고 설정을 담당할 서비스가 필요함 
		.userService(principalOAuth2UserService) // 담당할 서비스 등록함. 로그인 후 후처리 되는 곳!
		; 
	}
}
