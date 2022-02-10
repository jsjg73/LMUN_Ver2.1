package com.jsjg73.lmun.dto;

import com.jsjg73.lmun.dto.LocationDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantResponse {
    String username;
    LocationDto departure;
}
