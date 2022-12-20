package com.project.appointments.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalTime;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "appointment")
@FieldNameConstants
public class Appointment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "schedule_id")
  private Long scheduleId;
  @JsonFormat(pattern = "HH:mm")
  @Column(name = "start_time")
  private LocalTime startTime;
  @JsonFormat(pattern = "HH:mm")
  @Column(name = "end_time")
  private LocalTime endTime;
  @Enumerated(value = EnumType.STRING)
  @Column(name = "status")
  private AppointmentStatus status;
  @Column(name = "person_id")
  private Long personId;
}
