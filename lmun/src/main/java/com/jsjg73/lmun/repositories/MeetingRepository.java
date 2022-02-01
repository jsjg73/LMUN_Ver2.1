package com.jsjg73.lmun.repositories;

import com.jsjg73.lmun.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, String> {
}
