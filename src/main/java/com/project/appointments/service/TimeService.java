package com.project.appointments.service;

import com.project.appointments.model.entity.TimeOffsetRequest;
import com.project.appointments.model.entity.UtilityDateSet;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;

@Service
public class TimeService {
  public Long setTimeOffset(TimeOffsetRequest timeOffsetRequest){
    UtilityDateSet.offset = ChronoUnit.MILLIS.between(LocalDateTime.now(),timeOffsetRequest.getLocalDateTimeOffset());
    return UtilityDateSet.offset;
  }
  public Long resetTimeOffset(){
    return UtilityDateSet.offset = 0L;
  }
}
