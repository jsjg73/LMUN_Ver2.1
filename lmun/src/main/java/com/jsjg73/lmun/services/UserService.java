package com.jsjg73.lmun.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jsjg73.lmun.model.User;
import com.jsjg73.lmun.repositories.UserRepository;

@Service
@Transactional
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findById(username).orElseThrow(()-> new UsernameNotFoundException("Invalid Username"));
	}
	
	public void registry(User user) throws DuplicateKeyException{
		
		Optional<User> optional =userRepository.findById(user.getUsername());
		
		if(optional.isPresent())
			throw new DuplicateKeyException("Duplicated user ID");
		
		userRepository.save(user);
	}
}
