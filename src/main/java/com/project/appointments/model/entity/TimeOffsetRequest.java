package com.project.appointments.model.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TimeOffsetRequest {
LocalDateTime localDateTimeOffset;
}
