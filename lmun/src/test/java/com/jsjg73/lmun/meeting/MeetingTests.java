package com.jsjg73.lmun.meeting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jsjg73.lmun.dto.AuthenticationRequest;
import com.jsjg73.lmun.dto.LocationDto;
import com.jsjg73.lmun.dto.UserDto;
import com.jsjg73.lmun.dto.meeting.MeetingRequest;
import com.jsjg73.lmun.exceptions.MeetingNotFoundException;
import com.jsjg73.lmun.jwt.JwtUtil;
import com.jsjg73.lmun.model.Category;
import com.jsjg73.lmun.resources.MeetingResource;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql("classpath:/test/meeting/data.sql")
@Transactional
public class MeetingTests {
    @Autowired
    MockMvc mockMvc;
    static LocationDto departure;
    static MeetingRequest meetingRequest;
    static AuthenticationRequest authenticationRequest;
    private static String token ;

    private String meetingId;
    @Autowired
    private JwtUtil jwtUtil;

    @BeforeAll

    public static void beforeAll() {

        departure = new LocationDto(131213L,"장소 이름",1.1, 3.3,"지번 주소", "도로명 주소", Category.PM9);
        List<LocationDto> list = List.of(departure);

        authenticationRequest =new AuthenticationRequest("user1", "password");

        meetingRequest = new MeetingRequest("Math Study", 3);
    }
    @Test
    @Order(1)
    @DisplayName("계정 생성 성공")
    @Disabled
    public void createUserSuccess() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String requestContent = null;
        try {
            requestContent = mapper.writeValueAsString(authenticationRequest);
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

//        assertEquals(user.getUsername(), jwtUtil.extractUsername(token));
    }

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
                    token = jwtUtil.eliminatePrefix(localToken);
                    assertNotNull(token);
                    assertEquals(authenticationRequest.getUsername(), jwtUtil.extractUsername(token));
                });
    }
    @Test
    @Order(2)
    @DisplayName("모임 생성 성공")
    public void createMeeting() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/meeting")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+token)
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
                    meetingId = JsonPath.read(responseBody, "$.id");
                });
        assertNotNull(meetingId);
    }

    // 모임 단일 조회 실패(잘못된 모임 id)
    @Test
    @Order(3)
    @DisplayName("모임 단일 조회 실패(잘못된 모임 ID)")
    @Disabled
    public void getMeetingByIdFail() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/meeting/abcdefghijk")
                        .accept(MediaType.APPLICATION_JSON)
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
    @Disabled
    public void getMeetingByIdSuccess() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/meeting/abcdefghijklm")
                                .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(handler().handlerType(MeetingResource.class))
                .andExpect(handler().methodName("getMeetingById"))
                .andExpect(jsonPath("$.id").value("abcdefghijklm"))
                .andExpect(jsonPath("$.name").value("모임 이름"))
                .andExpect(jsonPath("$.host").value("user1"))
                .andExpect(jsonPath("$.atLeast").value(3))
                .andExpect(jsonPath("$.participantsCount").value(3))
                .andExpect(jsonPath("$.participants[0]").value("user1"))
                .andExpect(jsonPath("$.participants[1]").value("user2"))
                .andExpect(jsonPath("$.participants[2]").value("user3"));

    }
}
