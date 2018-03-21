package com.testproject.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.testproject.domain.Voting;
import com.testproject.repository.VotingRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VotingServiceImplTest {

  @InjectMocks
  private VotingServiceImpl votingService;

  @Mock
  private VotingRepository repository;

  @Mock
  private Voting voting;

  @Test
  public void create() {
    final String expectedTitle = "Title";
    when(voting.getTitle()).thenReturn(expectedTitle);
    when(repository.save(any(Voting.class))).thenReturn(voting);

    final Voting actualVoting = votingService.create(expectedTitle);

    assertNotNull(actualVoting);
    assertEquals(expectedTitle, actualVoting.getTitle());
    assertEquals(Integer.valueOf(0), actualVoting.getId());
    assertEquals(false, actualVoting.isActive());
    assertEquals(true, actualVoting.getVotes().isEmpty());

    verify(repository).save(any(Voting.class));
    verifyNoMoreInteractions(repository);
  }

  @Test
  public void getVotingWhenExists() {
    final Integer expectedId = 1;
    final String expectedTitle = "Title";
    final boolean expectedIsActive = true;

    final Voting expectedVoting = new Voting();
    expectedVoting.setId(expectedId);
    expectedVoting.setTitle(expectedTitle);
    expectedVoting.setActive(expectedIsActive);

    when(repository.findById(any(Integer.class))).thenReturn(Optional.of(expectedVoting));

    final Voting actualVoting = votingService.getVoting(expectedId);

    assertNotNull(actualVoting);
    assertEquals(expectedVoting.getTitle(), actualVoting.getTitle());
    assertEquals(expectedVoting.getId(), actualVoting.getId());
    assertEquals(expectedVoting.isActive(), actualVoting.isActive());
    assertEquals(expectedVoting.getVotes(), actualVoting.getVotes());

    verify(repository).findById(expectedId);
    verifyNoMoreInteractions(repository);
  }

  @Test(expected = NoSuchElementException.class)
  public void getVotingWhenDoesNotExist() {
    final Integer expectedId = 1;
    when(repository.findById(any(Integer.class))).thenReturn(Optional.empty());
    votingService.getVoting(expectedId);
  }

  @Test
  public void updateVoting() {
    final Integer expectedId = 1;
    final String expectedTitle = "Title";
    final boolean expectedIsActive = true;

    final Voting expectedVoting = new Voting();
    expectedVoting.setId(expectedId);
    expectedVoting.setTitle(expectedTitle);
    expectedVoting.setActive(expectedIsActive);

    when(repository.save(any(Voting.class))).thenReturn(expectedVoting);

    final Voting actualVoting = votingService.updateVoting(expectedVoting);

    assertNotNull(actualVoting);
    assertEquals(expectedVoting.getTitle(), actualVoting.getTitle());
    assertEquals(expectedVoting.getId(), actualVoting.getId());
    assertEquals(expectedVoting.isActive(), actualVoting.isActive());
    assertEquals(expectedVoting.getVotes(), actualVoting.getVotes());

    verify(repository).save(any(Voting.class));
    verifyNoMoreInteractions(repository);
  }
}