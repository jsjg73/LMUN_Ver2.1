package com.jsjg73.lmun.dto;

import com.jsjg73.lmun.model.Category;
import com.jsjg73.lmun.model.Location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
	private Long id;
	private String placeName;
	private Double lon;
	private Double lat;
	private String addressName;
	private String roadAddressName;
	private Category categoryGroupCode;
	
	public LocationDto(Location location) {
		this.id=location.getId();
		this.placeName=location.getPlaceName();
		this.lon=location.getLon();
		this.lat=location.getLat();
		this.addressName=location.getAddressName();
		this.roadAddressName=location.getRoadAddressName();
		this.categoryGroupCode=location.getCategoryGroupCode();
	}
	
	public Location toEntity() {
		return new Location(id, placeName, lon, lat, addressName, roadAddressName, categoryGroupCode);
	}
}
