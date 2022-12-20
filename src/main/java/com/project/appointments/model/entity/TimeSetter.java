package com.project.appointments.model.entity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeSetter {
  public static Long offset = 0L;
  public static Long setTimeOffset(TimeOffsetRequest timeOffsetRequest){
    TimeSetter.offset = ChronoUnit.MILLIS.between(LocalDateTime.now(),timeOffsetRequest.getLocalDateTimeOffset());
    return TimeSetter.offset;
  }
}
