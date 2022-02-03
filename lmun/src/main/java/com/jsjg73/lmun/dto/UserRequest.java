package com.jsjg73.lmun.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserRequest {
    private String username;
    private String password;
    private String nick;
    private List<LocationDto> departures;
}
