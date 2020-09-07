package com.cos.instagram.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserProfileEditRespDto {
	private String name;
	private String username;
	private String website;
	private String bio;
	private String email;
	private String phone;
	private String gender;
}
