package com.testproject.controller;

import com.testproject.controller.dto.PointDto;
import com.testproject.controller.dto.VotingDto;
import com.testproject.domain.PointName;
import com.testproject.domain.Voting;
import com.testproject.service.VotingService;
import com.testproject.util.Converter;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "votings")
public class VotingController {

  private VotingService votingService;
  private Converter converter;

  @Autowired
  public VotingController(VotingService votingService, Converter converter) {
    this.votingService = votingService;
    this.converter = converter;
  }


  @GetMapping(path = "/{votingId}")
  public VotingDto getVotingById(@PathVariable(name = "votingId")
      Integer votingId) {
    Voting voting = votingService.getVoting(votingId);
    return converter.convertToDto(voting);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public VotingDto createVoting(@RequestBody String title) {
    Voting voting = votingService.create(title);
    return converter.convertToDto(voting);
  }

  /**
   * Add vote for a specific voting point and return updated Voting
   */
  @PatchMapping(path = "/{votingId}/vote")
  public VotingDto vote(
      @PathVariable(name = "votingId")
          Integer votingId,
      @RequestBody
      @Validated PointDto pointDto) {

    PointName pointName = pointDto.getPointName();
    Voting voting = votingService.getVoting(votingId);

    if (voting.isActive()) {
      Map<PointName, Integer> votes = voting.getVotes();
      votes.put(pointName, votes.get(pointName) + 1);
      voting = votingService.updateVoting(voting);
    }
    return converter.convertToDto(voting);
  }

  @PatchMapping(path = "/{votingId}/stop")
  public VotingDto stopVoting(
      @PathVariable(name = "votingId")
          Integer votingId) {
    Voting voting = votingService.getVoting(votingId);
    if (voting.isActive()) {
      voting.setActive(false);
      voting = votingService.updateVoting(voting);
    }
    return converter.convertToDto(voting);
  }

  @GetMapping(path = "/{votingId}/vote/{pointName}")
  public PointDto votingPointScore(
      @PathVariable(name = "votingId")
          Integer votingId,
      @PathVariable(name = "pointName")
          PointName pointName) {

    Voting voting = votingService.getVoting(votingId);
    Map<PointName, Integer> votes = voting.getVotes();

    return converter.convertToDto(pointName, votes.get(pointName));
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoSuchElementException.class)
  private String return404(NoSuchElementException e) {
    return e.getMessage();
  }

  @ExceptionHandler
  private String defaultExceptionHandler(Throwable e) {
    return e.getMessage();
  }
}
