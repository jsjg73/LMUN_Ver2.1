package com.jsjg73.lmun.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jsjg73.lmun.dto.LocationDto;
import com.jsjg73.lmun.dto.UserDto;
import com.jsjg73.lmun.services.UserService;

@SpringBootTest
public class UserEntityTest {
	@Autowired
	UserService service;
	@Test
	public void context() {
		UserDto user =new UserDto("jsjg73", "password", "김제궁", new ArrayList<LocationDto>());
		
		service.registry(user);
		
		UserDto other = (UserDto)service.loadUserByUsername("jsjg73");
		assertNotNull(other);
		assertNotSame(user, other);
		assertEquals(user.getNick(), other.getNick());
		
	}
}
