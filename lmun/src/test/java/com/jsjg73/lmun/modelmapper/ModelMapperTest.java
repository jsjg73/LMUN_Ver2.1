package com.jsjg73.lmun.modelmapper;

import com.jsjg73.lmun.dto.LocationDto;
import com.jsjg73.lmun.dto.MeetingDto;
import com.jsjg73.lmun.dto.ParticipantsResponse;
import com.jsjg73.lmun.model.Category;
import com.jsjg73.lmun.model.Location;
import com.jsjg73.lmun.model.Meeting;
import com.jsjg73.lmun.model.User;
import com.jsjg73.lmun.model.manytomany.Participant;
import com.jsjg73.lmun.model.manytomany.ParticipantKey;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

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
        participant.setId(new ParticipantKey());
        participant.setUser(user);

        Set<Participant> participants = new HashSet<>();
        participants.add(participant);
        meeting = Meeting.builder()
                    .id("모임 id")
                    .name("모임이름")
                    .host(user)
                    .atLeast(3)
                    .participants(participants)
                    .build();
        participant.setMeeting(meeting);

        Location location = new Location(111L, "hello", 1.1, 2.2, "addressname", "road", Category.AC5, null);
        participant.setDeparture(location);

        Participant p2 = new Participant();
        p2.setId(new ParticipantKey("ddd","sss"));
        p2.setUser(user);
        p2.setDeparture(location);
        participants.add(p2);
    }

    @Test
    public void context(){
        MeetingDto meetingDto = modelMapper.map(meeting, MeetingDto.class);
        System.out.println(meetingDto.toString());

        Location location = new Location(111L, "hello", 1.1, 2.2, "addressname", "road", Category.AC5, null);
        LocationDto map = modelMapper.map(location, LocationDto.class);

        ParticipantsResponse participantsResponse = modelMapper.map(meeting, ParticipantsResponse.class);
        System.out.println();
//        TypeMap<Location, LocationDto> locationLocationDtoTypeMap = modelMapper.createTypeMap(Location.class, LocationDto.class);
    }
}
