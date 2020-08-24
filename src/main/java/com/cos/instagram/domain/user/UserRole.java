package com.cos.instagram.domain.user;

import lombok.Getter;

@Getter
public enum UserRole {
	USER("ROLE_USER", "일반유저"), ADMIN("ROLE_ADMIN");

	UserRole(String key) {
		this.key = key;
	}
	
	UserRole(String key, String value) {
		this.key = key;
		this.value = value;
	}

	private String key;
	private String value;
}
