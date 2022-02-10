package com.jsjg73.lmun.dto;

import com.jsjg73.lmun.model.Category;
import com.jsjg73.lmun.model.Location;

import com.jsjg73.lmun.model.manytomany.Departure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
	
	public LocationDto(Departure departure) {
		Location location = departure.getLocation();
		this.id=location.getId();
		this.placeName=location.getPlaceName();
		this.lon=location.getLon();
		this.lat=location.getLat();
		this.addressName=location.getAddressName();
		this.roadAddressName=location.getRoadAddressName();
		this.categoryGroupCode=location.getCategoryGroupCode();
	}
	public Location toEntity() {
		return Location.builder()
				.id(id)
				.placeName(placeName)
				.lon(lon).lat(lat)
				.addressName(addressName).roadAddressName(roadAddressName)
				.categoryGroupCode(categoryGroupCode)
				.build();
	}
}
