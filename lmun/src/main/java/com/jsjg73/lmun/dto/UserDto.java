package com.jsjg73.lmun.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import com.jsjg73.lmun.model.Meeting;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jsjg73.lmun.model.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto implements UserDetails{
	private String username;
	private String password;
	private String nick;

	private boolean isAccountNonExpired=true;
	private boolean isAccountNonLocked=true;
	private boolean isCredentialsNonExpired=true;
	private boolean isEnabled=true;
	private Set<? extends GrantedAuthority> grantedAuthorities=new HashSet<>();
	
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}
	
	@JsonIgnore
	public Set<? extends GrantedAuthority> getGrantedAuthorities(){
		return grantedAuthorities;
	}
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return isAccountNonExpired;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return isEnabled;
	}

}
