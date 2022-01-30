package com.jsjg73.lmun.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jsjg73.lmun.dto.UserDto;
import com.jsjg73.lmun.model.User;
import com.jsjg73.lmun.repositories.UserRepository;

@Service
@Transactional
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findById(username).orElseThrow(()-> new UsernameNotFoundException("Invalid Username"));
		return new UserDto(user);
	}
	
	public void registry(UserDto userDto) throws DuplicateKeyException{
		if(isExistUser(userDto.getUsername()))
			throw new DuplicateKeyException("Duplicated user ID");
		
		userRepository.save(userDto.toEntity());
	}
	private boolean isExistUser(String username) {
		return userRepository.findById(username).isPresent();
	}
}
