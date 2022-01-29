package com.jsjg73.lmun.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsjg73.lmun.model.User;

public interface UserRepository extends JpaRepository<User, String> {

}
