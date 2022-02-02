package com.jsjg73.lmun.repositories;

import com.jsjg73.lmun.model.manytomany.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipatingRepository extends JpaRepository<Participant, Long> {
}
