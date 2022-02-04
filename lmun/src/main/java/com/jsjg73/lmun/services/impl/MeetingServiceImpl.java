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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class MeetingServiceImpl implements MeetingService {
    private MeetingRepository meetingRepository;
    private UserRepository userRepository;
    private ParticipatingRepository participatingRepository;
    private ModelMapper modelMapper;
    @Autowired
    public MeetingServiceImpl(MeetingRepository meetingRepository, UserRepository userRepository, ParticipatingRepository participatingRepository, ModelMapper modelMapper) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.participatingRepository = participatingRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public String registerMeeting(MeetingDto meetingDto, String username) {
        User host = userRepository.findById(username).get();
        Meeting meeting =  new Meeting(meetingDto.getName(), host, meetingDto.getAtLeast());
        meetingRepository.save(meeting);
        participate(meeting, host);

        return meeting.getId();
    }

    @Override
    @Transactional
    public MeetingParticipantsDto getMeetingById(String meetingId) {
        Meeting meeting = findById(meetingId);
        return modelMapper.map(meeting, MeetingParticipantsDto.class);
    }

    @Override
    @Transactional
    public Boolean update(String meetingId, MeetingDto meetingDto) {
        Meeting meeting = findById(meetingId);
        meeting.setName(meetingDto.getName());
        meeting.setAtLeast(meetingDto.getAtLeast());

        meetingRepository.save(meeting);
        return true;
    }

    @Override
    @Transactional
    public void participate(String meetingId, String username) {
        User user = userRepository.findById(username).orElseThrow(()->
                new UsernameNotFoundException(String.format("Username %s not found", username)));
        Meeting meeting = findById(meetingId);
        if( meeting.containsUser(user) ){
            throw new AlreadyParticipationException(meetingId+" already in attendance.");
        }
        participate(meeting,user);
    }

    private Meeting findById(String id){
        return meetingRepository
                .findById(id)
                .orElseThrow(()->
                        new MeetingNotFoundException(String.format("MeetingId %s not found", id)));
    }

    private void participate(Meeting meeting, User host) {
        Participant participant = new Participant(meeting, host, host.getDefaultDeparture());
        meeting.addParticipant(participant);
    }
}