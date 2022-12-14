package com.project.appointments.controller;

import com.project.appointments.model.entity.TimeOffsetRequest;
import com.project.appointments.service.TimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/time")
public class TimeSetterController {
  private final TimeService timeService;
  @PutMapping("/set")
  @ResponseStatus(HttpStatus.OK)
  public Long setOffset(@RequestBody TimeOffsetRequest timeOffsetRequest){
    return timeService.setTimeOffset(timeOffsetRequest);
  }
  @PutMapping("/reset")
  @ResponseStatus(HttpStatus.OK)
  public Long resetOffset(){
    return timeService.resetTimeOffset();
  }
}
