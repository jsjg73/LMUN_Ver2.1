package com.jsjg73.lmun.dto;

import com.jsjg73.lmun.model.Meeting;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MeetingDto {
    String id;
    String name;
    String host;
    Integer atLeast;
    Integer participantsCount;
    LocalDateTime createAt;
    LocalDateTime updateAt;

    public MeetingDto(String id, String name, String host, Integer atLeast) {
        this.id = id;
        this.name = name;
        this.host = host;
        this.atLeast = atLeast;
    }

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
