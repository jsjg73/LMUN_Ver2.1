package com.jsjg73.lmun.resources;


import com.jsjg73.lmun.dto.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jsjg73.lmun.model.AuthenticationResponse;
import com.jsjg73.lmun.services.UserService;

@RestController
@RequestMapping("/user")
public class UserResource {

	UserService userService;

	@Autowired
	public UserResource(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody UserRequest	userRequest){
		userService.registry(userRequest);
		return new ResponseEntity<>(new AuthenticationResponse(true, "유저 생성 성공"), HttpStatus.CREATED);
	}
}
