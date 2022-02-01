package com.jsjg73.lmun.services.impl;

import com.jsjg73.lmun.dto.MeetingDto;
import com.jsjg73.lmun.model.Meeting;
import com.jsjg73.lmun.model.Participant;
import com.jsjg73.lmun.model.User;
import com.jsjg73.lmun.repositories.MeetingRepository;
import com.jsjg73.lmun.repositories.ParticipatingRepository;
import com.jsjg73.lmun.repositories.UserRepository;
import com.jsjg73.lmun.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MeetingServiceImpl implements MeetingService {
    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ParticipatingRepository participatingRepository;

    @Override
    public MeetingDto registerMeeting(MeetingDto meetingDto, String username) {
        User host = userRepository.getById(username);
        Meeting meeting = meetingRepository.save(
                new Meeting(meetingDto.getName(), host, meetingDto.getAtLeast())
        );
        participatingRepository.save(new Participant(meeting, host));

        meetingDto.setId(meeting.getId());
        meetingDto.setParticipantsCount(1);
        meetingDto.setHost(host.getUsername());
        return meetingDto;
    }
}