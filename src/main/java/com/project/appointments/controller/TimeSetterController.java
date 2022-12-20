package com.project.appointments.controller;

import com.project.appointments.model.entity.TimeOffsetRequest;
import com.project.appointments.model.entity.TimeSetter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/time")
public class TimeSetterController {
  @PutMapping("/set")
  @ResponseStatus(HttpStatus.OK)
  public Long setOffset(@RequestBody TimeOffsetRequest timeOffsetRequest) {
    return TimeSetter.setTimeOffset(timeOffsetRequest);
  }

}
