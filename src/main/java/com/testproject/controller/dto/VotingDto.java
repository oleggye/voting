package com.testproject.controller.dto;

import com.testproject.domain.PointName;
import java.util.Map;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class VotingDto {

  @Min(0)
  private Integer id;

  @Size(max = 255)
  @NotNull
  private String title;

  private Map<PointName, Integer> votes;

  private boolean isActive;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Map<PointName, Integer> getVotes() {
    return votes;
  }

  public void setVotes(Map<PointName, Integer> votes) {
    this.votes = votes;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }
}
