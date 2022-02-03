package com.jsjg73.lmun.services;

import javax.transaction.Transactional;

import com.jsjg73.lmun.dto.LocationDto;
import com.jsjg73.lmun.dto.UserRequest;
import com.jsjg73.lmun.model.Location;
import com.jsjg73.lmun.model.Meeting;
import com.jsjg73.lmun.model.manytomany.Departure;
import com.jsjg73.lmun.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

		return UserDto.builder()
				.username(user.getUsername())
				.nick(user.getNick())
				.password(user.getPassword())
				 .grantedAuthorities(user.getAuthorities())
				.isAccountNonLocked(true)
				.isEnabled(true)
				.isCredentialsNonExpired(true)
				.isAccountNonExpired(true)
				 .build();

	}
	
	public void registry(UserRequest userRequest) throws DuplicateKeyException{
		if(isExistUser(userRequest.getUsername()))
			throw new DuplicatedUsernameException("Duplicated user ID");

		User user = User.builder()
				.username(userRequest.getUsername())
				.password(passwordEncoder.encode(userRequest.getPassword()))
				.nick(userRequest.getNick())
				.build();
		List<Departure> departures = userRequest.getDepartures()
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
