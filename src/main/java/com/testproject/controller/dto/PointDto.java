package com.testproject.controller.dto;

import com.testproject.domain.PointName;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PointDto {

  @NotNull
  private PointName pointName;

  @Min(0)
  private Integer count;

  public PointName getPointName() {
    return pointName;
  }

  public void setPointName(PointName pointName) {
    this.pointName = pointName;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }
}
