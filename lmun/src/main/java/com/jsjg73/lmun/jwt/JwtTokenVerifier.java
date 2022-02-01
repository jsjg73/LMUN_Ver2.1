package com.jsjg73.lmun.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class JwtTokenVerifier extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final JwtConfig jwtConfig;

    public JwtTokenVerifier(JwtUtil jwtUtil, JwtConfig jwtConfig) {
        this.jwtUtil = jwtUtil;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());

        if (Strings.isNullOrEmpty(authorizationHeader) || ! authorizationHeader.startsWith(jwtConfig.getTokenPrefix())){
            filterChain.doFilter(request,response);
            return;
        }
        String token = jwtUtil.eliminatePrefix(authorizationHeader);
        try{
            String username = jwtUtil.extractUsername(token);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    new HashSet<SimpleGrantedAuthority>()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (JwtException e){
            throw new IllegalStateException(String.format("Token %s cannot be trust", token));
        }

        filterChain.doFilter(request,response);
    }
}
