package com.jsjg73.lmun.resources;

import com.jsjg73.lmun.dto.MeetingDto;
import com.jsjg73.lmun.dto.UserDto;
import com.jsjg73.lmun.model.Meeting;
import com.jsjg73.lmun.services.MeetingService;
import com.jsjg73.lmun.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/meeting")
public class MeetingResource {
    @Autowired
    MeetingService meetingService;
    @PostMapping
    public ResponseEntity<MeetingDto> registerMeeting(@RequestBody MeetingDto meetingDto, Authentication auth){
        meetingDto=meetingService.registerMeeting(meetingDto, auth.getName());
        return new ResponseEntity<>( meetingDto, HttpStatus.CREATED);
    }

}
