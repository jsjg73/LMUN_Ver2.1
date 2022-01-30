package com.jsjg73.lmun.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Location {
	@Id
	private Long id;
	private String placeName;
	private Double lon;
	private Double lat;
	private String addressName;
	private String roadAddressName;
	private Category categoryGroupCode;
	
	@ManyToMany(mappedBy = "departures", fetch = FetchType.LAZY)
	private Set<User> users = new HashSet<>();

	public Location(Long id, String placeName, Double lon, Double lat, String addressName, String roadAddressName,
			Category categoryGroupCode) {
		super();
		this.id = id;
		this.placeName = placeName;
		this.lon = lon;
		this.lat = lat;
		this.addressName = addressName;
		this.roadAddressName = roadAddressName;
		this.categoryGroupCode = categoryGroupCode;
	}
	
	
}
