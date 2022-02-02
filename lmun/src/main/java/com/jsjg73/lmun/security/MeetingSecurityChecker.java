package com.jsjg73.lmun.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class MeetingSecurityChecker {
    public boolean checkerHost(Authentication authentication, String meetingId){
        String expected = meetingId+":HOST";
        Long cnt = authentication.getAuthorities().stream()
                .filter(
                        grantedAuthority ->grantedAuthority.getAuthority().equals(expected)
                ).map(
                        grantedAuthority -> grantedAuthority.getAuthority()
                ).count();
        return cnt == 1;
    }
}
