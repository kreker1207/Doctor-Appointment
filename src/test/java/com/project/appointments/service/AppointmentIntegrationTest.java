package com.project.appointments.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.project.appointments.Application;
import com.project.appointments.model.entity.Appointment;
import com.project.appointments.model.entity.AppointmentStatus;
import com.project.appointments.repository.AppointmentH2Repository;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = Application.class)
@ExtendWith(SpringExtension.class)
@Transactional
public class AppointmentIntegrationTest {

  @Autowired
  private AppointmentService appointmentService;
  @Autowired
  private AppointmentH2Repository appointmentH2Repository;

  @AfterEach
  public void clearH2() {
    appointmentH2Repository.deleteAll();
  }

  @Test
  @Sql(statements = "INSERT INTO schedule (id, doctor_id, date) VALUES (1,null,'2020-12-13')")
  void createAppointment() {
    Appointment appointment = new Appointment().setId(1L).setStartTime(LocalTime.of(8, 0))
        .setEndTime(LocalTime.of(8, 30)).setStatus(AppointmentStatus.AVAILABLE).setScheduleId(1L);
    assertEquals(0, appointmentH2Repository.findAll().size());
    appointmentService.addAppointment(appointment);
    assertEquals(1, appointmentH2Repository.findAll().size());
  }

  @Test
  @Sql(statements = "INSERT INTO schedule (id, doctor_id, date) VALUES (1,null,'2020-12-13')")
  void getAppointment() {
    Appointment appointment = new Appointment().setId(1L).setStartTime(LocalTime.of(8, 0))
        .setEndTime(LocalTime.of(8, 30)).setStatus(AppointmentStatus.AVAILABLE).setScheduleId(1L);
    appointmentService.addAppointment(appointment);
    assertEquals(1, appointmentService.getAppointments().size());
    Appointment foundAppointment = appointmentService.getAppointment(1L);
    assertEquals(appointment, foundAppointment);
  }

  @Test
  @Sql(statements = "INSERT INTO schedule (id, doctor_id, date) VALUES (1,null,'2020-12-13')")
  @Sql(statements = "INSERT INTO appointment (id, schedule_id, start_time, end_time, status, person_id)  VALUES (1,1,'08:00','08:30','AVAILABLE',null)")
  @Sql(statements = "INSERT INTO appointment (id, schedule_id, start_time, end_time, status, person_id)  VALUES (2,1,'12:00','12:30','AVAILABLE',null)")
  void getAppointments() {
    List<Appointment> foundAppointment = appointmentService.getAppointments();
    assertEquals(2, appointmentH2Repository.findAll().size());
    assertEquals(2, foundAppointment.size());
    assertEquals(LocalTime.of(12, 0), foundAppointment.get(1).getStartTime());
  }

  @Test
  @Sql(statements = "INSERT INTO schedule (id, doctor_id, date) VALUES (1,null,'2020-12-13')")
  @Sql(statements = "INSERT INTO appointment (id, schedule_id, start_time, end_time, status, person_id)  VALUES (1,1,'08:00','08:30','AVAILABLE',null)")
  void deleteAppointment() {
    assertEquals(1, appointmentService.getAppointments().size());
    appointmentService.deleteAppointment(1L);
    assertEquals(0, appointmentService.getAppointments().size());
  }

  @Test
  @Sql(statements = "INSERT INTO schedule (id, doctor_id, date) VALUES (1,null,'2020-12-13')")
  @Sql(statements = "INSERT INTO appointment (id, schedule_id, start_time, end_time, status, person_id)  VALUES (1,1,'08:00','08:30','AVAILABLE',null)")
  void updateAppointment() {
    assertEquals(LocalTime.of(8, 30), appointmentService.getAppointment(1L).getEndTime());
    appointmentService.updateAppointment(new Appointment().setStartTime(LocalTime.of(8,0)).setEndTime(LocalTime.of(8,40)).setStatus(AppointmentStatus.AVAILABLE).setScheduleId(1L), 1L);
    assertEquals(LocalTime.of(8, 40), appointmentService.getAppointment(1L).getEndTime());
  }

  @Test
  @Sql(statements = "INSERT INTO person (id, first_name, last_name, phone) VALUES (1,'Ivan','Kerzhov','+289456')")
  @Sql(statements = "INSERT INTO schedule (id, doctor_id, date) VALUES (1,null,'2020-12-13')")
  @Sql(statements = "INSERT INTO appointment (id, schedule_id, start_time, end_time, status, person_id)  VALUES (1,1,'08:00','08:30','AVAILABLE',null)")
  void reserveAppointment() {
    assertEquals(AppointmentStatus.AVAILABLE, appointmentService.getAppointment(1L).getStatus());
    appointmentService.reserveAppointment(1L, "+289456");
    assertEquals(1, appointmentService.getAppointment(1L).getPersonId());
    assertEquals(AppointmentStatus.RESERVED, appointmentService.getAppointment(1L).getStatus());

  }

  @Test
  @Sql(statements = "INSERT INTO schedule (id, doctor_id, date) VALUES (1,null,'2020-12-13')")
  void generateAppointmentsForDayTest() {
    assertEquals(0, appointmentService.getAppointments().size());
    appointmentService.generateAppointmentsForDay(1L);
    List<Appointment> foundAppointments = appointmentService.getAppointments();
    assertEquals(16, foundAppointments.size());
    assertEquals(LocalTime.of(8, 0), foundAppointments.get(0).getStartTime());
    assertEquals(LocalTime.of(8, 30), foundAppointments.get(0).getEndTime());
  }
}
