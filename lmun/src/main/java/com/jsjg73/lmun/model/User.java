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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	private String username;
	private String password;
	private String nick;
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "USER_DEPARTURES", joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "USERNAME"), inverseJoinColumns = @JoinColumn(name = "LOCATION_ID", referencedColumnName = "ID"))
	private List<Location> departures = new ArrayList<Location>();
	
	public User(String username, String password, String nick) {
		super();
		this.username = username;
		this.password = password;
		this.nick = nick;
	}

}
