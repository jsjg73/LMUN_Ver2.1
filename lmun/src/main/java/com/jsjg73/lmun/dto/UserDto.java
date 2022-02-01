package com.jsjg73.lmun.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import com.jsjg73.lmun.model.Meeting;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jsjg73.lmun.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements UserDetails{
	private String username;
	private String password;
	private String nick;
	private List<LocationDto> departures;
	private List<MeetingDto> meetings;

	public UserDto(String username, String password, String nick, List<LocationDto> departures) {
		this.username = username;
		this.password = password;
		this.nick = nick;
		this.departures = departures;
	}
	
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

	public UserDto(User user) {
		this.username = user.getUsername();
		this.password= user.getPassword();
		this.nick=user.getNick();
		this.departures =user.getDepartures().stream().map(each->new LocationDto(each)).collect(Collectors.toList());
	}
}
