package com.jsjg73.lmun.services;

import com.jsjg73.lmun.dto.*;

public interface MeetingService {
    public String registerMeeting(MeetingDto meetingDto, String username);
    public MeetingParticipantsDto getMeetingById(String meetingId);
    public Boolean update(String meetingId, MeetingDto meetingDto);
    public void participate(String meetingId, String username);
    public ParticipantsResponse getParticipants(String meetingId);

    public void registerProposal(String meetingId, String name, ProposalRequest proposalRequest);

    public ProposalSuccessResponse getProposal(String meetingId, ProposalRequest proposalRequest);
}
