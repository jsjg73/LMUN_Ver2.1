package com.jsjg73.lmun.dto;

import com.jsjg73.lmun.model.Meeting;
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

    public Meeting toEntity() {
        return new Meeting();
    }
}
