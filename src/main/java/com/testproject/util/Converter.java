package com.testproject.util;

import com.testproject.controller.dto.PointDto;
import com.testproject.controller.dto.VotingDto;
import com.testproject.domain.PointName;
import com.testproject.domain.Voting;
import org.springframework.stereotype.Component;

@Component
public class Converter {

  public PointDto convertToDto(PointName pointName, Integer count) {
    PointDto pointDto = new PointDto();
    pointDto.setPointName(pointName);
    pointDto.setCount(count);
    return pointDto;
  }

  public VotingDto convertToDto(Voting voting) {
    VotingDto votingDto = new VotingDto();
    votingDto.setId(voting.getId());
    votingDto.setTitle(voting.getTitle());
    votingDto.setVotes(voting.getVotes());
    votingDto.setActive(voting.isActive());
    return votingDto;
  }
}
