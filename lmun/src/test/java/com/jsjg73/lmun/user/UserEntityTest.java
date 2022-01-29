package com.jsjg73.lmun.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jsjg73.lmun.model.User;
import com.jsjg73.lmun.services.UserService;

@SpringBootTest
public class UserEntityTest {
	@Autowired
	UserService service;
	@Test
	public void context() {
		User user = new User();
		user.setUsername("jsjg73");
		user.setPassword("password");
		user.setNick("김제궁");
		
		service.registry(user);
		user.setUsername("kkk");
		user.setNick("굴");
		service.registry(user);
		
		User other = (User)service.loadUserByUsername("jsjg73");
		assertNotNull(other);
		assertNotSame(user, other);
		assertEquals(user.getNick(), other.getNick());
		
	}
}
