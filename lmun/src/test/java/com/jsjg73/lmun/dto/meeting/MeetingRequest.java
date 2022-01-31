package com.jsjg73.lmun.dto.meeting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MeetingRequest {
    String name;
    Integer atLeast;
}
