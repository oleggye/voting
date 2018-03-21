package com.testproject.domain;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Voting {

  @Id
  @GeneratedValue
  private Integer id;

  @Column
  private String title;

  @ElementCollection
  private Map<PointName, Integer> votes;

  @Column
  private boolean isActive;

  public Voting() {
    this.votes = ImmutableMap.of(
        PointName.YES, 0,
        PointName.NO, 0,
        PointName.PROBABLY, 0
    );
    isActive = true;
  }

  public Voting(String title) {
    this.title = title;
    this.votes = ImmutableMap.of(
        PointName.YES, 0,
        PointName.NO, 0,
        PointName.PROBABLY, 0
    );
    isActive = true;
  }

  public Voting(String title, Map<PointName, Integer> votes, boolean isActive) {
    this.title = title;
    this.votes = votes;
    this.isActive = isActive;
  }

  public Integer getId() {
    return this.id;
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

  @Override
  public String toString() {
    return "Voting{" +
        "id='" + id + '\'' +
        ", title='" + title + '\'' +
        ", votes=" + votes +
        ", isActive=" + isActive +
        '}';
  }
}
