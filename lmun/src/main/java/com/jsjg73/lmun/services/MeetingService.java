package com.jsjg73.lmun.services;

import com.jsjg73.lmun.dto.MeetingDto;

public interface MeetingService {
    public MeetingDto registerMeeting(MeetingDto meetingDto, String username);
}
