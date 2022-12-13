package com.project.appointments.controller;

import com.project.appointments.model.entity.Appointment;
import com.project.appointments.service.AppointmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/appointments")
public class AppointmentController {

  private final AppointmentService appointmentService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Appointment> getAppointments() {
    return appointmentService.getAppointments();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Appointment getAppointment(@PathVariable Long id) {
    return appointmentService.getAppointment(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Appointment addAppointment(@RequestBody Appointment appointment) {
    return appointmentService.addAppointment(appointment);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Appointment deleteAppointment(@PathVariable Long id) {
    return appointmentService.deleteAppointment(id);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Appointment updateAppointment(@RequestBody Appointment appointment,
      @PathVariable Long id) {
    return appointmentService.updateAppointment(appointment, id);
  }

  @PutMapping("/reserve/{id}/{personId}")
  @ResponseStatus(HttpStatus.OK)
  public Appointment reserveAppointment(@PathVariable Long id,@PathVariable Long personId) {
    return appointmentService.reserveAppointment(id, personId);
  }
}
