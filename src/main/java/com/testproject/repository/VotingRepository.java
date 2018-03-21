package com.testproject.repository;

import com.testproject.domain.Voting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingRepository extends CrudRepository<Voting, Integer> {
}
