package com.jsjg73.lmun.model;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import com.jsjg73.lmun.model.manytomany.Departure;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
	@Id
	private Long id;
	private String placeName;
	private Double lon;
	private Double lat;
	private String addressName;
	private String roadAddressName;
	private Category categoryGroupCode;
	private Integer proposalCount;
	
	@OneToMany(mappedBy = "location")
	private Set<Departure> departures = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Location location = (Location) o;
		return Objects.equals(id, location.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
