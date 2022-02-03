package com.jsjg73.lmun.model;

import java.util.HashSet;
import java.util.List;
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
	
	@OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
	private Set<Departure> departures = new HashSet<>();
	
}
