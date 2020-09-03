package com.cos.instagram.web.dto;

import com.cos.instagram.domain.user.User;
import com.cos.instagram.domain.user.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinReqDto {
	private String email;
	private String name;
	private String username;
	private String password;

	// Request dto 값을 User에 있는 것에 하나씩 set해서 바꿔주면 번거로움 → save, update시 받아서 바로 처리할 수 있게 함수 만들기
	public User toEntity() {
		return User.builder()
				.email(email)
				.name(name)
				.username(username)
				.password(password)
				.role(UserRole.USER).build();
	}
}
