package com.cos.blogtest.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
	private int id;
	private String username;
	private String password;
	private String email;
	private String role; // 회원가입일 경우 user 로 고정
}
