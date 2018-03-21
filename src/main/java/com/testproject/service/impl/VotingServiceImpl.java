package com.testproject.service.impl;

import com.testproject.domain.Voting;
import com.testproject.repository.VotingRepository;
import com.testproject.service.VotingService;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotingServiceImpl implements VotingService {

  private VotingRepository votingRepository;

  @Autowired
  public VotingServiceImpl(VotingRepository votingRepository) {
    this.votingRepository = votingRepository;
  }

  @Override
  public Voting create(String title) {
    Voting voting = new Voting(title);
    return votingRepository.save(voting);
  }

  @Override
  public Voting getVoting(Integer id) {
    Optional<Voting> votingOptional = votingRepository.findById(id);
    return votingOptional.orElseThrow(
        () -> {
          String message = String.format("Voting with id=%d doesn't exist", id);
          return new NoSuchElementException(message);
        });
  }

  @Override
  public Voting updateVoting(Voting voting) {
    return votingRepository.save(voting);
  }
}
