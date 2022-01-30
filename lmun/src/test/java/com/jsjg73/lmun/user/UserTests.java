package com.jsjg73.lmun.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jsjg73.lmun.dto.AuthenticationRequest;
import com.jsjg73.lmun.dto.LocationDto;
import com.jsjg73.lmun.dto.UserDto;
import com.jsjg73.lmun.exceptions.DuplicatedUsernameException;
import com.jsjg73.lmun.exceptions.PasswordException;
import com.jsjg73.lmun.jwt.JwtUtil;
import com.jsjg73.lmun.model.Category;
import com.jsjg73.lmun.resources.UserResource;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTests {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	JwtUtil jwtUtil;
	
	static LocationDto departure;
	static UserDto user;

	private String token;
	
	@BeforeAll
	public static void beforeAll() {
		departure = new LocationDto(16618597L,
				"장생당약국", 
				127.05897078335246, 37.506051888130386, 
				"서울 강남구 대치동 943-16", "서울 강남구 테헤란로84길 17", Category.PM9);
		List<LocationDto> list = List.of(departure);

		user =new UserDto("jsjg73", "password", "김제궁", list);
//		user = new User();
//		user.setUsername("jsjg73");
//		user.setPassword("password");
//		user.setNick("김제궁");
//		user.appendDeparture(departure);
		
	}
	@Test
	public void context() {
		ObjectMapper mapper = new ObjectMapper();
		String requestContent = null;
		try {
			requestContent = mapper.writeValueAsString(user);
			System.out.println(requestContent);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			fail();
		}
		assertNotNull(requestContent);
	}
	@Test
	@Order(1)
	@DisplayName("계정 생성 성공")
	public void createUserSuccess() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String requestContent = null;
		try {
			requestContent = mapper.writeValueAsString(user);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			fail();
		}
		assertNotNull(requestContent);
		
		mockMvc.perform(
				MockMvcRequestBuilders.
				post("/user")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(requestContent)
				
		).andDo(print())
		.andExpect(status().isCreated())
		.andExpect(re->{
			token = JsonPath.read(re.getResponse().getContentAsString(), "$.token");
		});
		
		assertNotNull(token);
		
		assertEquals(user.getUsername(), jwtUtil.extractUsername(token));
	}

	@Test
	@Order(2)
	@DisplayName("계정 생성 실패(중복된 아이디)")
	public void createUserFailDuplicatedUsername() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String requestContent = null;
		try {
			requestContent = mapper.writeValueAsString(user);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			fail();
		}
		assertNotNull(requestContent);
		
		mockMvc.perform(
				MockMvcRequestBuilders.
				post("/user")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(requestContent)
				
		).andDo(print())
		.andExpect(status().is4xxClientError())
		.andExpect(handler().handlerType(UserResource.class))
		.andExpect(handler().methodName("registerUser"))
		.andExpect(result-> assertInstanceOf(DuplicatedUsernameException.class, result.getResolvedException()))
		.andExpect(result->{
			assertEquals("Duplicated user ID", result.getResolvedException().getMessage());
		});
	}

	@Test
	@Order(2)
	@DisplayName("로그인 실패(잘못된 아이디)")
	public void loginFailWrongUsername() throws Exception {
		AuthenticationRequest loginForm = new AuthenticationRequest("wrongId", user.getPassword());
		
		ObjectMapper mapper = new ObjectMapper();
		String requestContent = null;
		try {
			requestContent = mapper.writeValueAsString(loginForm);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			fail();
		}
		assertNotNull(requestContent);
		
		mockMvc.perform(
				MockMvcRequestBuilders
				.post("/user/login")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(requestContent)
		).andDo(print())
		.andExpect(status().is4xxClientError())
		.andExpect(result -> assertEquals("Unauthorized", result.getResponse().getErrorMessage()));
//		.andExpect(result-> assertInstanceOf(UsernameNotFoundException.class, result.getResolvedException()))
//		.andExpect(result->{
//			assertEquals("Username wrongId not found", result.getResolvedException().getMessage());
//		});
		
	}

	@Test
	@Order(2)
	@DisplayName("로그인 실패(잘못된 패스워드)")
	public void loginFailWrongPassword() throws Exception {
		AuthenticationRequest loginForm = new AuthenticationRequest(user.getUsername(), "wrongPassword");
		
		ObjectMapper mapper = new ObjectMapper();
		String requestContent = null;
		try {
			requestContent = mapper.writeValueAsString(loginForm);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			fail();
		}
		assertNotNull(requestContent);
		
		mockMvc.perform(
				MockMvcRequestBuilders
				.post("/user/login")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(requestContent)
		).andDo(print())
		.andExpect(status().is4xxClientError())
		.andExpect(result -> assertEquals("Unauthorized", result.getResponse().getErrorMessage()));
//		.andExpect(result-> assertInstanceOf(PasswordException.class, result.getResolvedException()))
//		.andExpect(result->{
//			assertEquals("Invalid Username/Password", result.getResolvedException().getMessage());
//		});
	}

	@Test
	@Order(2)
	@DisplayName("로그인 성공")
	public void loginSuccess() throws Exception{
		AuthenticationRequest loginForm = new AuthenticationRequest(user.getUsername(), user.getPassword());
		
		ObjectMapper mapper = new ObjectMapper();
		String requestContent = null;
		try {
			requestContent = mapper.writeValueAsString(loginForm);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			fail();
		}
		assertNotNull(requestContent);
		
		mockMvc.perform(
				MockMvcRequestBuilders
				.post("/user/login")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(requestContent)
		).andDo(print())
		.andExpect(status().isOk())
		.andExpect(re->{
			String localToken = re.getResponse().getHeader("Authorization");
			localToken = jwtUtil.eliminatePrefix(localToken);
			assertNotNull(localToken);
			assertEquals(user.getUsername(), jwtUtil.extractUsername(localToken));
		});
		
		
	}
}
