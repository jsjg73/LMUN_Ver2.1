package com.jsjg73.lmun.modelmapper;

import com.jsjg73.lmun.dto.MeetingDto;
import com.jsjg73.lmun.model.Meeting;
import com.jsjg73.lmun.model.User;
import com.jsjg73.lmun.model.manytomany.Participant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
public class ModelMapperTest {
    static Meeting meeting;

    @Autowired
    ModelMapper modelMapper;
    @BeforeAll
    public static void beforeAll(){

        User user = User.builder()
                .username("user1")
                .password("password")
                .nick("nick1")
                .build();
        Participant participant = new Participant();
        participant.setUser(user);

        Set<Participant> participants = new HashSet<>();
        participants.add(participant);
        meeting = new Meeting("모임 id","모임이름", user, 3, participants);
        participant.setMeeting(meeting);
    }

    @Test
    public void context(){
        MeetingDto meetingDto = modelMapper.map(meeting, MeetingDto.class);
        System.out.println(meetingDto.toString());
    }
}
