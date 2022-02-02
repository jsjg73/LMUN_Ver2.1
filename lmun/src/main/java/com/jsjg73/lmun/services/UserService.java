package com.jsjg73.lmun.services;

import javax.transaction.Transactional;

import com.jsjg73.lmun.dto.LocationDto;
import com.jsjg73.lmun.model.Location;
import com.jsjg73.lmun.model.manytomany.Departure;
import com.jsjg73.lmun.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jsjg73.lmun.dto.UserDto;
import com.jsjg73.lmun.exceptions.DuplicatedUsernameException;
import com.jsjg73.lmun.model.User;
import com.jsjg73.lmun.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository
				.findById(username)
				.orElseThrow(()->
						new UsernameNotFoundException(String.format("Username %s not found", username)));
		return new UserDto(user);
	}
	
	public void registry(UserDto userDto) throws DuplicateKeyException{
		if(isExistUser(userDto.getUsername()))
			throw new DuplicatedUsernameException("Duplicated user ID");
		User user = new User();
		user.setUsername(userDto.getUsername());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setNick(userDto.getNick());

		List<Departure> departures = userDto.getDepartures()
				.stream()
				.map(
					locationDto-> {
						Location location = locationDto.toEntity();
						locationRepository.save(location);
						return new Departure(user, location);
					}
				).collect(Collectors.toList());
		user.setDepartures(	departures );

		userRepository.save(user);
	}
	private boolean isExistUser(String username) {
		return userRepository.findById(username).isPresent();
	}
}
