package com.jsjg73.lmun.model;

import java.util.ArrayList;
import java.util.List;

public class User {
	private String username;
	private String password;
	private String nick;
	private List<Location> departures=new ArrayList<>();
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public List<Location> getDepartures() {
		return departures;
	}
	public void setDepartures(List<Location> departures) {
		this.departures = departures;
	}
	
	public void appendDeparture(Location departure) {
		departures.add(departure);
	}
}
