package com.jsjg73.lmun.services.impl;

import com.jsjg73.lmun.dto.MeetingDto;
import com.jsjg73.lmun.dto.MeetingParticipantsDto;
import com.jsjg73.lmun.exceptions.AlreadyParticipationException;
import com.jsjg73.lmun.exceptions.MeetingNotFoundException;
import com.jsjg73.lmun.model.Meeting;
import com.jsjg73.lmun.model.manytomany.Participant;
import com.jsjg73.lmun.model.User;
import com.jsjg73.lmun.repositories.MeetingRepository;
import com.jsjg73.lmun.repositories.ParticipatingRepository;
import com.jsjg73.lmun.repositories.UserRepository;
import com.jsjg73.lmun.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        participatingRepository.save(new Participant(meeting, host, host.getDefaultDeparture()));

        meetingDto.setId(meeting.getId());
        meetingDto.setParticipantsCount(1);
        meetingDto.setHost(host.getUsername());
        return meetingDto;
    }

    @Override
    public MeetingParticipantsDto getMeetingById(String meetingId) {
        Meeting meeting = findById(meetingId);
        return new MeetingParticipantsDto(meeting);
    }

    @Override
    public Boolean update(String meetingId, MeetingDto meetingDto) {
        Meeting meeting = findById(meetingId);
        meeting.setName(meetingDto.getName());
        meeting.setAtLeast(meetingDto.getAtLeast());

        meetingRepository.save(meeting);
        return true;
    }

    @Override
    public void participate(String meetingId, String username) {
        User user = userRepository.findById(username).orElseThrow(()->
                new UsernameNotFoundException(String.format("Username %s not found", username)));
        Meeting meeting = findById(meetingId);
        if( meeting.containsUser(user) ){
            throw new AlreadyParticipationException(meetingId+" already in attendance.");
        }
        Participant participant = new Participant(meeting, user, user.getDefaultDeparture());
        user.getMeetings().add(participant);
        meeting.getParticipants().add(participant);
        participatingRepository.save(participant);
    }

    private Meeting findById(String id){
        return meetingRepository
                .findById(id)
                .orElseThrow(()->
                        new MeetingNotFoundException(String.format("MeetingId %s not found", id)));
    }
}