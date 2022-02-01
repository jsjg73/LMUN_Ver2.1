package com.jsjg73.lmun.dto;

import com.jsjg73.lmun.model.Meeting;
import com.jsjg73.lmun.model.Participant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MeetingDto {
    String id;
    String name;
    String host;
    Integer atLeast;
    Integer participantsCount;

    public MeetingDto(Meeting meeting) {
        this.id=meeting.getId();
        this.name = meeting.getName();
        this.host = meeting.getHost().getUsername();
        this.atLeast = meeting.getAtLeast();
        this.participantsCount = meeting.getParticipants().size();
    }

    public Meeting toEntity() {
        return new Meeting();
    }
}
