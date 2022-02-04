package com.jsjg73.lmun.model;

import java.util.*;
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

	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
	private Set<Participant> participants=new HashSet<>();

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
		return participants.stream().map(participant -> {
					Meeting meeting = participant.getMeeting();
					String authority = null;
					String host = meeting.getHost().getUsername();
					if (host.equals(username)) {
						authority = String.format("%s:HOST", meeting.getId());
					} else {
						authority = String.format("%s:GUEST", meeting.getId());
					}
					return new SimpleGrantedAuthority(authority);
				}).collect(Collectors.toSet());
	}

	public void addParticipant(Participant participant) {
		participants.add(participant);
	}
}
