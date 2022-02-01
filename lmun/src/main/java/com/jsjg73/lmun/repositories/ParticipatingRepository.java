package com.jsjg73.lmun.repositories;

import com.jsjg73.lmun.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipatingRepository extends JpaRepository<Participant, Long> {
}
