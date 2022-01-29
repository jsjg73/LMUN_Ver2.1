package com.jsjg73.lmun.resources;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jsjg73.lmun.jwt.JwtUtil;
import com.jsjg73.lmun.model.AuthenticationResponse;
import com.jsjg73.lmun.model.User;
import com.jsjg73.lmun.services.UserService;

@RestController
@RequestMapping("/user")
public class UserResource {
	@Autowired
	UserService userService;
	@Autowired JwtUtil jwtUtil;
	
	@PostMapping
	public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody User user){
		Objects.nonNull(user);
		Objects.nonNull(user.getUsername());
		userService.registry(user);
		String token = jwtUtil.generateToken(userService.loadUserByUsername(user.getUsername()));
		return new ResponseEntity<>(new AuthenticationResponse(token), HttpStatus.CREATED);
	}
}
