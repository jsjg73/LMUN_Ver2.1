package com.jsjg73.lmun.meeting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jsjg73.lmun.config.UTF8MockMvc;
import com.jsjg73.lmun.dto.AuthenticationRequest;
import com.jsjg73.lmun.dto.LocationDto;
import com.jsjg73.lmun.dto.MeetingDto;
import com.jsjg73.lmun.dto.meeting.MeetingRequest;
import com.jsjg73.lmun.exceptions.AlreadyParticipationException;
import com.jsjg73.lmun.exceptions.MeetingNotFoundException;
import com.jsjg73.lmun.jwt.JwtUtil;
import com.jsjg73.lmun.model.Category;
import com.jsjg73.lmun.resources.MeetingResource;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@UTF8MockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql("classpath:/test/meeting/data.sql")
@Transactional
public class MeetingTests {
    @Autowired
    MockMvc mockMvc;
    static LocationDto departure= new LocationDto(131213L,"장소 이름",1.1, 3.3,"지번 주소", "도로명 주소", Category.PM9);
    static MeetingRequest meetingRequest= new MeetingRequest("Math Study", 3);
    static MeetingDto registeredMeeting = new MeetingDto("abcdefghijklm","모임 이름", "user1", 3, 1);
    static AuthenticationRequest authenticationRequest=new AuthenticationRequest("user1", "password");;
    private static String tokenForUser1 ="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJhYmNkZWZnaGlqa2xtOkhPU1QifV0sImlhdCI6MTY0MzgxMjM5OCwiZXhwIjoxNjQ0NTkxNjAwfQ.GROTIXSEBv-_pq923tM0LIgDQXiD709wW1VfPXqvFko-U6RAQgT9VYmcJ_90OF6QHski8rzaQMU2rZ3XWeIthw";
    private static String tokenForUser2 ="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMiIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJhbGdvcml0aG1zU3R1ZHk6SE9TVCJ9XSwiaWF0IjoxNjQzODEyMzk4LCJleHAiOjk5OTk5OTk5OTl9.ZTQVOuvcMsk0OQFtsfShM2e1cWKYJpMSa9sBxaTGLY7Yy4JbqVfolceTeGmteAZUIA7lSxB48UG3vqOO--13jQ";
    private static String tokenForUser4 = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyNCIsImF1dGhvcml0aWVzIjpbXSwiaWF0IjoxNjQzODEyMzk4LCJleHAiOjk5OTk5OTk5OTl9.jz-xMU-iPq-6LZdT61NPHpKHWmJlG0GY-jaVNI9o6VMk8gHv7b_OjOkuGubr2KTg0bYd3fT7-7PzmgAWm8F9og";
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    @Order(1)
    @DisplayName("로그인 성공")
    public void loginSuccess() throws Exception{
        new ObjectMapper().writeValueAsString(authenticationRequest);
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/user/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(re->{
                    String localToken = re.getResponse().getHeader("Authorization");
                    String token = jwtUtil.eliminatePrefix(localToken);
                    assertNotNull(token);
                    assertEquals(authenticationRequest.getUsername(), jwtUtil.extractUsername(token));
                });
    }
    @Test
    @Order(2)
    @DisplayName("모임 생성 성공")
    public void createMeeting() throws Exception {
        List<String> expectedAuthorities = new ArrayList<>();
        expectedAuthorities.add("abcdefghijklm:HOST");

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/meeting")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+tokenForUser1)
                        .content(new ObjectMapper().writeValueAsString(meetingRequest))
        ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(handler().handlerType(MeetingResource.class))
                .andExpect(handler().methodName("registerMeeting"))
                .andExpect(jsonPath("$.name").value("Math Study"))
                .andExpect(jsonPath("$.atLeast").value(3))
                .andExpect(jsonPath("$.participantsCount").value(1))
                .andExpect(jsonPath("$.host").value("user1"))
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    String newId = JsonPath.read(responseBody, "$.id");
                    assertNotNull(newId);
                    expectedAuthorities.add(newId+":HOST");

                    String newToken = result.getResponse().getHeader("Token");
                    List<String> authorities = jwtUtil.extractAuthorities(newToken);
                    assertTrue(authorities.containsAll(expectedAuthorities));
                });
    }

    // 모임 단일 조회 실패(잘못된 모임 id)
    @Test
    @Order(3)
    @DisplayName("모임 단일 조회 실패(잘못된 모임 ID)")
//    @Disabled
    public void getMeetingByIdFail() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/meeting/abcdefghijk")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+tokenForUser1)
        ).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(MeetingResource.class))
                .andExpect(handler().methodName("getMeetingById"))
                .andExpect(result -> {
                   assertInstanceOf(MeetingNotFoundException.class,result.getResolvedException());
                }).andExpect(result -> assertEquals("MeetingId abcdefghijk not found", result.getResolvedException().getMessage()));
    }

    // 모임 단일 조회 성공
    @Test
    @Order(3)
    @DisplayName("모임 단일 조회 성공")
//    @Disabled
    public void getMeetingByIdSuccess() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/meeting/abcdefghijklm")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer "+tokenForUser1)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(MeetingResource.class))
                .andExpect(handler().methodName("getMeetingById"))
                .andExpect(jsonPath("$.id").value("abcdefghijklm"))
                .andExpect(jsonPath("$.name").value("모임 이름"))
                .andExpect(jsonPath("$.host").value("user1"))
                .andExpect(jsonPath("$.atLeast").value(3))
                .andExpect(jsonPath("$.participantsCount").value(3))
                .andExpect(jsonPath("$.participants[0]").value("nick1"))
                .andExpect(jsonPath("$.participants[1]").value("nick2"))
                .andExpect(jsonPath("$.participants[2]").value("nick3"));

    }
    @Test
    @Order(4)
    @DisplayName("모임 정보 수정 실패(권한 없음)")
    public void updateMeetingFail() throws Exception {
        meetingRequest.setName("updatedName");
        meetingRequest.setAtLeast(2);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/meeting/"+registeredMeeting.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+tokenForUser2)
                        .content(new ObjectMapper().writeValueAsString(meetingRequest))
        ).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(result -> {
                    assertEquals("Forbidden", result.getResponse().getErrorMessage());
                });

    }
    @Test
    @Order(4)
    @DisplayName("모임 정보 수정 성공")
    public void updateMeeting() throws Exception {
        meetingRequest.setName("updatedName");
        meetingRequest.setAtLeast(2);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/meeting/"+registeredMeeting.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+tokenForUser1)
                        .content(new ObjectMapper().writeValueAsString(meetingRequest))
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(MeetingResource.class))
                .andExpect(handler().methodName("updateMeeting"))
                .andExpect(jsonPath("$.id").value("abcdefghijklm"))
                .andExpect(jsonPath("$.name").value(meetingRequest.getName()))  // updateName
                .andExpect(jsonPath("$.host").value("user1"))
                .andExpect(jsonPath("$.atLeast").value(meetingRequest.getAtLeast())) // 2
                .andExpect(jsonPath("$.participantsCount").value(3));
    }

    @Test
    @Order(5)
    @DisplayName("모임 참가 실패(이미 참가 중인 모임)")
    public void entranceMeetingFail1()throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/meeting/"+registeredMeeting.getId()+"/participation")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+tokenForUser1)
        ).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(MeetingResource.class))
                .andExpect(handler().methodName("participateMeeting"))
                .andExpect(result -> assertInstanceOf(AlreadyParticipationException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(registeredMeeting.getId()+" already in attendance.", result.getResolvedException().getMessage()));
//                .andExpect(jsonPath("$.id").value("abcdefghijklm"))
//                .andExpect(jsonPath("$.name").value(meetingRequest.getName()))  // user1
//                .andExpect(jsonPath("$.host").value("user1"))
//                .andExpect(jsonPath("$.atLeast").value(meetingRequest.getAtLeast())) // 3
//                .andExpect(jsonPath("$.participantsCount").value(4));
    }
    @Test
    @Order(6)
    @DisplayName("모임 참가 실패(존재하지않는 모임)")
    public void entranceMeetingFail2()throws Exception{
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/meeting/abcdefghijk/participation")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer "+tokenForUser4)
                ).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(MeetingResource.class))
                .andExpect(handler().methodName("participateMeeting"))
                .andExpect(result -> assertInstanceOf(MeetingNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals("MeetingId abcdefghijk not found", result.getResolvedException().getMessage()));
    }
    @Test
    @Order(7)
    @DisplayName("모임 참가 성공")
    public void entranceMeeting()throws Exception{
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/meeting/"+registeredMeeting.getId()+"/participation")
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer "+tokenForUser4)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(MeetingResource.class))
                .andExpect(handler().methodName("participateMeeting"))
                .andExpect(jsonPath("$.id").value("abcdefghijklm"))
                .andExpect(jsonPath("$.name").value(registeredMeeting.getName()))  // user1
                .andExpect(jsonPath("$.host").value("user1"))
                .andExpect(jsonPath("$.atLeast").value(registeredMeeting.getAtLeast())) // 3
                .andExpect(jsonPath("$.participantsCount").value(4));
    }
}
