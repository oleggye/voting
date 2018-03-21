package com.testproject.service;

import com.testproject.domain.Voting;

public interface VotingService {

    Voting create(final String title);

    Voting getVoting(final Integer id);

    Voting updateVoting(final Voting voting);
}
