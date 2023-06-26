package com.lec.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lec.todo.dto.ResponseDTO;
import com.lec.todo.dto.UserDTO;
import com.lec.todo.model.UserEntity;
import com.lec.todo.security.TokenProvider;
import com.lec.todo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
		try {
			if(userDTO == null || userDTO.getPassword() == null) {
				throw new RuntimeException("비밀번호 오류!");
			}
			
			UserEntity user = UserEntity.builder()
				.username(userDTO.getUsername())
				.password(passwordEncoder.encode(userDTO.getPassword()))
				.build();
			
			UserEntity registeredUser = userService.create(user);
			UserDTO registerUserDTO = UserDTO.builder()
					.id(registeredUser.getId())
					.username(registeredUser.getUsername())
					.build();
			return ResponseEntity.ok().body(registeredUser);
		} catch (Exception e) {
			ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
		
		UserEntity user = userService.getByCredentials(
				userDTO.getUsername(), 
				userDTO.getPassword(),
				passwordEncoder);
		
		log.info("1. user = " + user.toString());
		
		if(user != null) {
			
			// token 생성
			final String token = tokenProvider.create(user);
			log.info("2. token = " + token);
			
			final UserDTO responseUserDTO = UserDTO.builder()
					.username(user.getUsername())
					.id(user.getId())
					.token(token)
					.build();
			
			return ResponseEntity.ok().body(responseUserDTO);
		} else {
			ResponseDTO responseDTO = ResponseDTO.builder()
					.error("로그인 실패")
					.build();
			
			return ResponseEntity
					.badRequest()
					.body(responseDTO);
		}
	}	
}











