package com.jsjg73.lmun.repositories;

import com.jsjg73.lmun.model.manytomany.Proposal;
import com.jsjg73.lmun.model.manytomany.ProposalKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<Proposal, ProposalKey> {
}
