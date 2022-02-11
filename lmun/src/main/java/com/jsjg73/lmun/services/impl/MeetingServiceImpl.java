package com.jsjg73.lmun.services.impl;

import com.jsjg73.lmun.dto.*;
import com.jsjg73.lmun.exceptions.AlreadyParticipationException;
import com.jsjg73.lmun.exceptions.MeetingNotFoundException;
import com.jsjg73.lmun.model.Location;
import com.jsjg73.lmun.model.Meeting;
import com.jsjg73.lmun.model.manytomany.*;
import com.jsjg73.lmun.model.User;
import com.jsjg73.lmun.repositories.LocationRepository;
import com.jsjg73.lmun.repositories.MeetingRepository;
import com.jsjg73.lmun.repositories.ProposalRepository;
import com.jsjg73.lmun.repositories.UserRepository;
import com.jsjg73.lmun.services.MeetingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MeetingServiceImpl implements MeetingService {
    final private MeetingRepository meetingRepository;
    final private UserRepository userRepository;
    final private LocationRepository locationRepository;
    final private ModelMapper modelMapper;
    final private ProposalRepository proposalRepository;
    @Autowired
    public MeetingServiceImpl(MeetingRepository meetingRepository, UserRepository userRepository, LocationRepository locationRepository, ModelMapper modelMapper, ProposalRepository proposalRepository) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.modelMapper = modelMapper;
        this.proposalRepository = proposalRepository;
    }

    @Override
    @Transactional
    public String registerMeeting(MeetingDto meetingDto, String username) {
        User host = findUserByUsername(username);
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
        User user = findUserByUsername(username);
        Meeting meeting = findById(meetingId);
        if( meeting.containsUser(user) ){
            throw new AlreadyParticipationException(meetingId+" already in attendance.");
        }
        participate(meeting,user);
    }

    @Override
    @Transactional
    public ParticipantsResponse getParticipants(String meetingId) {
        Meeting meeting = findById(meetingId);
        return modelMapper.map(meeting, ParticipantsResponse.class);
    }

    @Override
    @Transactional
    public void registerProposal(String meetingId, String username, ProposalRequest proposalRequest) {
        // TODO 제안 생성 서비스 로직 구현
        Meeting meeting = findById(meetingId);
        User proposer = findUserByUsername(username);
        //destination 저장
        Location destination = proposalRequest.getDestination().toEntity();

        ProposalKey proposalKey = new ProposalKey(meetingId, destination.getId());
        Proposal proposal = new Proposal();
        proposal.setId(proposalKey);
        proposal.setMeeting(meeting);
        proposal.setLocation(destination);
        proposal.setProposer(proposer);

        Set<Origin> origins = proposalRequest.getOrigins().stream().map(originRequest -> {
            User member = findUserByUsername(originRequest.getUsername());
            Origin origin = new Origin();
            origin.setUser(member);
            origin.setId(new OriginKey(proposalKey, member.getUsername()));
            origin.setLat(originRequest.getLat());
            origin.setLon(originRequest.getLon());
            origin.setAgree(Boolean.FALSE);
            origin.setProposal(proposal);
            return origin;
        }).collect(Collectors.toSet());

        proposal.setOrigins(origins);
        meeting.addProposal(proposal);
        meeting.setAtLeast(5);
//        meetingRepository.save(meeting);
//        return null;
    }

    @Override
    public ProposalSuccessResponse getProposal(String meetingId, ProposalRequest proposalRequest) {
        Long locId = proposalRequest.getDestination().getId();
        Proposal proposal = proposalRepository.findById(new ProposalKey(meetingId, locId)).orElseThrow();
        return modelMapper.map(proposal, ProposalSuccessResponse.class);
    }

    private Meeting findById(String id){
        return meetingRepository
                .findById(id)
                .orElseThrow(()->
                        new MeetingNotFoundException(String.format("MeetingId %s not found", id)));
    }
    private User findUserByUsername(String username){
        return userRepository.findById(username).orElseThrow(()->
                new UsernameNotFoundException(String.format("Username %s not found", username)));
    }
    private void participate(Meeting meeting, User host) {
        Participant participant = new Participant(meeting, host, host.getDefaultDeparture());
        meeting.addParticipant(participant);
    }
}