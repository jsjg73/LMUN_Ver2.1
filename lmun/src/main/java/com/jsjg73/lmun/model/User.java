package com.jsjg73.lmun.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import com.jsjg73.lmun.model.manytomany.Departure;
import com.jsjg73.lmun.model.manytomany.Participant;

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
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
	private List<Departure> departures = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Participant> meetings=new ArrayList<>();

	public User(String username, String password, String nick) {
		super();
		this.username = username;
		this.password = password;
		this.nick = nick;
	}

	public Location getDefaultDeparture(){
		return departures.get(0).getLocation();
	}

	@Override
	public int hashCode() {
		return username.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || !(o instanceof User))
			return false;
		User other = (User) o;
		return Objects.equals(username, other.username);
	}
}
