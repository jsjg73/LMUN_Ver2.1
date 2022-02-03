package com.jsjg73.lmun.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;

import com.jsjg73.lmun.model.manytomany.Departure;
import com.jsjg73.lmun.model.manytomany.Participant;

import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
	@Id
	private String username;
	private String password;
	private String nick;
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
	private List<Departure> departures = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private Set<Participant> meetings;

	public Location getDefaultDeparture(){
		return departures.get(0).getLocation();
	}



	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return username.equals(user.username);
	}

	@Override
	public int hashCode() {
		return Objects.hash(username);
	}

	public Set<SimpleGrantedAuthority> getAuthorities() {
		return meetings.stream().map(participant -> {
					Meeting meeting = participant.getMeeting();
					String authority = null;
					if (meeting.getHost().equals(username)) {
						authority = String.format("%s:HOST", meeting.getId());
					} else {
						authority = String.format("%s:GUEST", meeting.getId());
					}
					return new SimpleGrantedAuthority(authority);
				}).collect(Collectors.toSet());
	}
}
