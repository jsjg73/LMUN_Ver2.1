package com.jsjg73.lmun.dto;

import com.jsjg73.lmun.model.Meeting;

public class MeetingDto {

    public Meeting toEntity() {
        return new Meeting();
    }
}
