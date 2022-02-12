package com.jsjg73.lmun.repositories;

import com.jsjg73.lmun.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTestRepository extends JpaRepository<User, String> {
    public User findByNick(String nick);
}
