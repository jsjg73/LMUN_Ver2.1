package com.jsjg73.lmun.resources;

import com.jsjg73.lmun.dto.*;
import com.jsjg73.lmun.exceptions.DuplicatedProposalException;
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
    private final MeetingService meetingService;
    private final JwtUtil jwtUtil;

    @Autowired
    public MeetingResource(MeetingService meetingService, JwtUtil jwtUtil) {
        this.meetingService = meetingService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<MeetingDto> registerMeeting(@RequestBody MeetingDto meetingDto, Authentication auth, HttpServletResponse httpServletResponse){
        String meetingId = meetingService.registerMeeting(meetingDto, auth.getName());

        String token = jwtUtil.reGenerateToken(auth.getName());
        httpServletResponse.addHeader("Token" , token);
        return new ResponseEntity<>( meetingService.getMeetingById(meetingId), HttpStatus.CREATED);
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
    public ResponseEntity<MeetingDto> participateMeeting( @PathVariable("meetingId") String meetingId,
                                                          Authentication authentication){
        meetingService.participate(meetingId, authentication.getName());
        return new ResponseEntity<>(meetingService.getMeetingById(meetingId), HttpStatus.OK);
    }

    @GetMapping("/{meetingId}/departures")
    public ResponseEntity<ParticipantsResponse> getParticipants(@PathVariable("meetingId") String meetingId){
        ParticipantsResponse participantsResponse = meetingService.getParticipants(meetingId);
        return new ResponseEntity<>(participantsResponse, HttpStatus.OK);
    }

    @PostMapping("/{meetingId}/proposal")
    @PreAuthorize("hasAnyAuthority(#meetingId+':HOST', #meetingId+':GUEST')")
    public ResponseEntity<ProposalSuccessResponse> registerProposal(@PathVariable("meetingId") String meetingId,
                                                                    @RequestBody ProposalRequest proposalRequest,
                                                                    Authentication authentication){
        if(meetingService.alreadyProposedPlace(meetingId, proposalRequest)){
            throw new DuplicatedProposalException("this place is already proposed");
        }
        meetingService.registerProposal(meetingId, authentication.getName(),proposalRequest);
        ProposalSuccessResponse success = meetingService.getProposal(meetingId, proposalRequest);
        return new ResponseEntity<>(success, HttpStatus.OK);
    }
}
