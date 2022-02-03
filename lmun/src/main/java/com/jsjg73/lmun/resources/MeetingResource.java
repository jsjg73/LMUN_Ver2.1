package com.jsjg73.lmun.resources;

import com.jsjg73.lmun.dto.MeetingDto;
import com.jsjg73.lmun.dto.MeetingParticipantsDto;
import com.jsjg73.lmun.jwt.JwtUtil;
import com.jsjg73.lmun.services.MeetingService;
import com.jsjg73.lmun.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/meeting")
public class MeetingResource {
    @Autowired
    MeetingService meetingService;
    @Autowired
    UserService userService;
    @Autowired
    JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<MeetingDto> registerMeeting(@RequestBody MeetingDto meetingDto, Authentication auth, HttpServletResponse httpServletResponse){
        meetingDto=meetingService.registerMeeting(meetingDto, auth.getName());

        String token = jwtUtil.generateToken(auth.getName());
        httpServletResponse.addHeader("Token" , token);
        return new ResponseEntity<>( meetingDto, HttpStatus.CREATED);
    }

    @GetMapping("/{meetingId}")
    public ResponseEntity<MeetingParticipantsDto> getMeetingById(@PathVariable("meetingId") String meetingId){
        return new ResponseEntity<>(meetingService.getMeetingById(meetingId), HttpStatus.OK);
    }

    @PutMapping("/{meetingId}")
    @PreAuthorize("hasAnyAuthority(#meetingId+':HOST')")
    public ResponseEntity<MeetingDto> updateMeeting(@PathVariable("meetingId") String meetingId, @RequestBody MeetingDto meetingDto){
        meetingService.update(meetingId, meetingDto);

        return new ResponseEntity<>(meetingService.getMeetingById(meetingId), HttpStatus.OK);
    }

    @PutMapping("/{meetingId}/participation")
    public ResponseEntity<MeetingDto> participateMeeting( @PathVariable("meetingId") String meetingId,Authentication authentication){
        meetingService.participate(meetingId, authentication.getName());
        return new ResponseEntity<>(meetingService.getMeetingById(meetingId), HttpStatus.OK);
    }
}
