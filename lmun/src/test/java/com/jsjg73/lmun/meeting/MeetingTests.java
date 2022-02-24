package com.jsjg73.lmun.meeting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jsjg73.lmun.config.UTF8MockMvc;
import com.jsjg73.lmun.dto.*;
import com.jsjg73.lmun.dto.meeting.MeetingRequest;
import com.jsjg73.lmun.exceptions.AlreadyParticipationException;
import com.jsjg73.lmun.exceptions.DuplicatedProposalException;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@UTF8MockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(value = "classpath:/test/meeting/insert-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/test/meeting/delete-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MeetingTests {
    @Autowired
    MockMvc mockMvc;
    static LocationDto departure= new LocationDto(131213L,"장소 이름",1.1, 3.3,"지번 주소", "도로명 주소", Category.PM9);
    static MeetingRequest meetingRequest= new MeetingRequest("Math Study", 3);
    static MeetingDto registeredMeeting =  MeetingDto.builder()
            .id("abcdefghijklm")
            .name("모임 이름")
            .host("user1")
            .atLeast(3)
            .participantsCount(1)
            .build();
    static AuthenticationRequest authenticationRequest=new AuthenticationRequest("user1", "password");;
    private static String tokenForUser1 ="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJhYmNkZWZnaGlqa2xtOkhPU1QifV0sImlhdCI6MTY0MzgxMjM5OCwiZXhwIjo5OTk5OTk5OTk5fQ.P_k2dRNAdRSs1oYNTX-6hiQuSrPWcGkOX7vjoAfOEJyDpoNTVoLbT6H7OXAp3GQY-pf9ZeubMGApAvyUmz3cMQ";
    private static String tokenForUser2 ="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMiIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJhYmNkZWZnaGlqa2xtOkdVRVNUIn0seyJhdXRob3JpdHkiOiJhbGdvcml0aG1zU3R1ZHk6SE9TVCJ9XSwiaWF0IjoxNjQ1NzA0NDk1LCJleHAiOjk5OTk5OTk5OTl9.GR_CF_nuSUYYFMkIt6-YMPkNymFldCUF2KgQTSPAY_2L9fz8r7306-vM9d6-6fEuI9V_r1XnfnVWDLFQa8Ho6w";
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
                .andExpect(jsonPath("$.participants" ).value(containsInAnyOrder("user1", "user2", "user3")))
                .andExpect(jsonPath("$.createAt").isNotEmpty())
                .andExpect(jsonPath("$.updateAt").isNotEmpty());

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
                .andExpect(jsonPath("$.participantsCount").value(3))
                .andExpect(jsonPath("$.createAt").isNotEmpty())
                .andExpect(jsonPath("$.updateAt").isNotEmpty());
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

    @Test
    @Order(8)
    @DisplayName("참석자들 출발지 목록 조회 실패(존재하지않는 모임)")
    public void getParticipantsFail() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
//                        .get("/meeting/"+registeredMeeting.getId()+"/departures")
                        .get("/meeting/abcdefghijk/departures")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+tokenForUser1)
        ).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(MeetingResource.class))
                .andExpect(handler().methodName("getParticipants"))
                .andExpect(result -> assertInstanceOf(MeetingNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals("MeetingId abcdefghijk not found", result.getResolvedException().getMessage()));
    }
    @Test
    @Order(8)
    @DisplayName("참석자들 출발지 목록 조회 성공")
    public void getParticipants() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/meeting/"+registeredMeeting.getId()+"/departures")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+tokenForUser1)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(MeetingResource.class))
                .andExpect(handler().methodName("getParticipants"))
                .andExpect(jsonPath("$.participants").value(hasSize(3)))
                .andExpect(jsonPath("$.participants[0].departure.id").value(131213))
                .andExpect(jsonPath("$.participants[0].departure.placeName").value("장소 이름"))
                .andExpect(jsonPath("$.participants[0].departure.lon").value(3.3))
                .andExpect(jsonPath("$.participants[0].departure.lat").value(1.1))
                .andExpect(jsonPath("$.participants[0].departure.addressName").value("지번 주소"))
                .andExpect(jsonPath("$.participants[0].departure.roadAddressName").value("도로명 주소"))
                .andExpect(jsonPath("$.participants[0].departure.categoryGroupCode").value("PM9"));

    }

    @Test
    @Order(9)
    @DisplayName("제안 생성 실패(이미 제안된 장소)")
    public void registryProposalFail() throws Exception {
        ProposalRequest proposalRequest = proposalDataForTest();

        // register proposal
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/meeting/"+registeredMeeting.getId()+"/proposal")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(proposalRequest))
                                .header("Authorization", "Bearer "+tokenForUser1)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(MeetingResource.class))
                .andExpect(handler().methodName("registerProposal"))
                .andExpect(jsonPath("$.meetingId").value(registeredMeeting.getId()))
                .andExpect(jsonPath("$.locationId").value(26338954L));

        // again
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/meeting/"+registeredMeeting.getId()+"/proposal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(proposalRequest))
                        .header("Authorization", "Bearer "+tokenForUser1)
                ).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(MeetingResource.class))
                .andExpect(handler().methodName("registerProposal"))
                .andExpect(
                        result -> {
                            assertInstanceOf(DuplicatedProposalException.class, result.getResolvedException());
                            assertEquals("this place is already proposed", result.getResolvedException().getMessage());
                });
    }
    @Test
    @Order(10)
    @DisplayName("제안 생성 실패(unauthorized)")
    public void registryProposalFailUnauthorized() throws Exception {
        ProposalRequest proposalRequest = proposalDataForTest();

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/meeting/"+registeredMeeting.getId()+"/proposal")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(proposalRequest))

                                // user4 is not going to participate in this meeting.
                                .header("Authorization", "Bearer "+tokenForUser4)
                ).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(result -> {
                    assertEquals("Forbidden", result.getResponse().getErrorMessage());
                });
    }
    @Test
    @Order(11)
    @DisplayName("제안 생성 성공(HOST)")
    public void registryProposalHost() throws Exception {
        ProposalRequest proposalRequest = proposalDataForTest();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/meeting/"+registeredMeeting.getId()+"/proposal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(proposalRequest))
                        .header("Authorization", "Bearer "+tokenForUser1)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(MeetingResource.class))
                .andExpect(handler().methodName("registerProposal"))
                .andExpect(jsonPath("$.meetingId").value(registeredMeeting.getId()))
                .andExpect(jsonPath("$.locationId").value(26338954L));
    }
    @Test
    @Order(12)
    @DisplayName("제안 생성 성공(GUEST)")
    public void registryProposalGuest() throws Exception {
        ProposalRequest proposalRequest = proposalDataForTest();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/meeting/"+registeredMeeting.getId()+"/proposal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(proposalRequest))
                        .header("Authorization", "Bearer "+tokenForUser2)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(MeetingResource.class))
                .andExpect(handler().methodName("registerProposal"))
                .andExpect(jsonPath("$.meetingId").value(registeredMeeting.getId()))
                .andExpect(jsonPath("$.locationId").value(26338954L));
    }

    private ProposalRequest proposalDataForTest() {
        LocationDto destination
                = LocationDto.builder()
                .id(26338954L)
                .placeName("카카오프렌즈 코엑스점")
                .addressName("서울 강남구 삼성동 159")
                .roadAddressName("서울 강남구 영동대로 513")
                .lat(37.51207412593136)
                .lon(127.05902969025047)
                .categoryGroupCode(Category.NULL)
                .build();
        OriginRequest origin1
                = OriginRequest.builder()
                .username("user1")
                .lon(127.13243772760565)
                .lat(37.44148514309502)
                .build();
        OriginRequest origin2
                = OriginRequest.builder()
                .username("user2")
                .lon(127.1331694942593)
                .lat(37.4463137562622)
                .build();
        return ProposalRequest.builder()
                .destination(destination)
                .origins(List.of(origin1, origin2))
                .build();
    }
}
