package com.jsjg73.lmun.services;

import com.jsjg73.lmun.dto.MeetingDto;
import com.jsjg73.lmun.dto.MeetingParticipantsDto;

public interface MeetingService {
    public MeetingDto registerMeeting(MeetingDto meetingDto, String username);
    public MeetingParticipantsDto getMeetingById(String meetingId);
}
