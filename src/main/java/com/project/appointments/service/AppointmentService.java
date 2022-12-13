package com.project.appointments.service;

import com.project.appointments.exception.AppointmentAlreadyReserved;
import com.project.appointments.model.entity.Appointment;
import com.project.appointments.model.entity.AppointmentStatus;
import com.project.appointments.model.entity.Person;
import com.project.appointments.model.entity.Schedule;
import com.project.appointments.repository.AppointmentRepository;
import com.project.appointments.repository.PersonRepository;
import com.project.appointments.repository.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {

  private final AppointmentRepository appointmentRepository;
  private final PersonRepository personRepository;
  private final ScheduleRepository scheduleRepository;

  public List<Appointment> getAppointments() {
    return appointmentRepository.findAll();
  }

  public Appointment getAppointment(Long id) {
    return findAppointmentById(id);
  }

  public Appointment addAppointment(Appointment appointment) {
    return appointmentRepository.save(appointment);
  }

  public Appointment updateAppointment(Appointment appointment, Long id) {
    Appointment saveAppointment = findAppointmentById(id);
    return appointmentRepository.save(
        saveAppointment.setStatus(appointment.getStatus())
            .setPersonId(appointment.getPersonId())
            .setStartTime(appointment.getStartTime())
            .setEndTime(appointment.getEndTime())
            .setScheduleId(appointment.getScheduleId()));
  }

  public Appointment deleteAppointment(Long id) {
    Appointment appointment = findAppointmentById(id);
    appointmentRepository.deleteById(id);
    return appointment;
  }

  public Appointment reserveAppointment(Long appointmentId, Long personId) {
    Appointment appointment = findAppointmentById(appointmentId);
    reservationValidation(appointment);
    Person person = personRepository.findById(personId).orElseThrow(() -> {
      throw new EntityNotFoundException("Person was not found by phone");
    });
    appointmentRepository.save(appointment.setStatus(AppointmentStatus.RESERVED).setPersonId(
        person.getId()));
    return appointment;
  }

  private Appointment findAppointmentById(Long id) {
    return appointmentRepository.findById(id).orElseThrow(() -> {
      throw new EntityNotFoundException("Appointment was not found by id");
    });
  }

  public void generateAppointmentsForDay(Long scheduleId) {
    for (LocalTime startTime = LocalTime.of(8, 0); startTime.isBefore(LocalTime.of(16, 0));
        startTime = startTime.plusMinutes(30)) {
      appointmentRepository.save(new Appointment().setStatus(AppointmentStatus.AVAILABLE)
          .setStartTime(startTime).setEndTime(startTime.plusMinutes(30)).setScheduleId(scheduleId));
    }
  }

  private void reservationValidation(Appointment appointment) {
    Schedule schedule = scheduleRepository.findById(appointment.getScheduleId()).orElseThrow(() -> {
      throw new EntityNotFoundException("Connected schedule was not found by id");
    });
    if (appointment.getStatus().equals(AppointmentStatus.RESERVED)) {
      throw new AppointmentAlreadyReserved("This Appointment already reserved");
    } else if (LocalDate.now().isAfter(schedule.getDate()) || (
        LocalDate.now().isEqual(schedule.getDate()) && LocalTime.now()
            .isAfter(appointment.getStartTime()))) {
      throw new AppointmentAlreadyReserved("You are late to reserve this appointment");
    }
  }
}
