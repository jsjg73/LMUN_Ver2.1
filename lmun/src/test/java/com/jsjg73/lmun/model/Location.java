package com.jsjg73.lmun.model;

public class Location {
	private Long id;
	private String placeName;
	private Double lon;
	private Double lat;
	private String addressName;
	private String roadAddressName;
	private Category categoryGroupCode;
	public Location() {
	}
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	public Double getLon() {
		return lon;
	}
	public void setLon(Double lon) {
		this.lon = lon;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public String getAddressName() {
		return addressName;
	}
	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}
	public String getRoadAddressName() {
		return roadAddressName;
	}
	public void setRoadAddressName(String roadAddressName) {
		this.roadAddressName = roadAddressName;
	}
	public Category getCategoryGroupCode() {
		return categoryGroupCode;
	}
	public void setCategoryGroupCode(Category categoryGroupCode) {
		this.categoryGroupCode = categoryGroupCode;
	}
	
}
