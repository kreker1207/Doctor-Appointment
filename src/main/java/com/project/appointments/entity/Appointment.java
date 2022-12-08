package com.project.appointments.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalTime;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "appointment")
public class Appointment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "schedule_id")
  private Long scheduleId;
  @Column(name = "start_time")
  private LocalTime startTime;
  @Column(name = "end_time")
  private LocalTime endTime;
  @Column(name = "ticket_status")
  private AppointmentStatus status;
  @Column(name = "user_id")
  private Long userId;
}
