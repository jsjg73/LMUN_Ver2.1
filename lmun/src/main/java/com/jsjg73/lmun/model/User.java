package com.jsjg73.lmun.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class User implements UserDetails {
	@Id
	private String username;
	private String password;
	private String nick;
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "USER_DEPARTURES", joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "USERNAME"), inverseJoinColumns = @JoinColumn(name = "LOCATION_ID", referencedColumnName = "ID"))
	private List<Location> departures = new ArrayList<Location>();

	@Transient
	private boolean isAccountNonExpired;
	@Transient
	private boolean isAccountNonLocked;
	@Transient
	private boolean isCredentialsNonExpired;
	@Transient
	private boolean isEnabled;
	@Transient
	private Set<? extends GrantedAuthority> grantedAuthorities;

	public User() {
	}

	public User(String username, String password, Set<? extends GrantedAuthority> grantedAuthorities,
			boolean isAccountNonExpired, boolean isAccountNonLocked, boolean isCredentialsNonExpired,
			boolean isEnabled) {
		this.grantedAuthorities = grantedAuthorities;
		this.password = password;
		this.username = username;
		this.isAccountNonExpired = isAccountNonExpired;
		this.isAccountNonLocked = isAccountNonLocked;
		this.isCredentialsNonExpired = isCredentialsNonExpired;
		this.isEnabled = isEnabled;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
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
	public boolean isAccountNonExpired() {
		return isAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public List<Location> getDepartures() {
		return departures;
	}

	public void setDepartures(List<Location> departures) {
		this.departures = departures;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void appendDeparture(Location departure) {
		departures.add(departure);
	}

}
