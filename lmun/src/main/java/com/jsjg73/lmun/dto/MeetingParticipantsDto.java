package com.jsjg73.lmun.dto;

import com.jsjg73.lmun.model.Meeting;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MeetingParticipantsDto extends MeetingDto{
    private List<String> participants;

    public MeetingParticipantsDto(Meeting meeting){
        super(meeting);
        participants = meeting
                .getParticipants()
                .stream()
                .map(
                    participant -> participant.getUser().getNick()).collect(Collectors.toList()
                );
    }
}
