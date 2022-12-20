package com.project.appointments.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.project.appointments.exception.AppointmentAlreadyReserved;
import com.project.appointments.model.entity.Appointment.Fields;
import com.project.appointments.Application;
import com.project.appointments.model.entity.Appointment;
import com.project.appointments.model.entity.AppointmentStatus;
import com.project.appointments.repository.AppointmentH2Repository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalTime;
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
  void addAppointment_success() {
    assertEquals(0, appointmentH2Repository.findAll().size());
    var expectedAppointment = new Appointment().setId(1L).setStartTime(LocalTime.of(8, 0))
        .setEndTime(LocalTime.of(8, 30)).setStatus(AppointmentStatus.AVAILABLE).setScheduleId(1L);
    var appointmentResult = appointmentService.addAppointment(createAppointment());
    assertEquals(1, appointmentH2Repository.findAll().size());
    assertThat(appointmentResult).usingRecursiveComparison().ignoringFields(Fields.id)
        .isEqualTo(expectedAppointment);
  }

  @Test
  @Sql(statements = "INSERT INTO schedule (id, doctor_id, date) VALUES (1,null,'2020-12-13')")
  @Sql(statements = "INSERT INTO appointment (id, schedule_id, start_time, end_time, status, person_id)  VALUES (1,1,'08:00','08:30','AVAILABLE',null)")
  void getAppointment_success() {
    var expectedAppointment = new Appointment().setId(1L).setStartTime(LocalTime.of(8, 0))
        .setEndTime(LocalTime.of(8, 30)).setStatus(AppointmentStatus.AVAILABLE).setScheduleId(1L);
    assertEquals(1, appointmentService.getAppointments().size());
    var foundAppointment = appointmentService.getAppointment(1L);
    assertEquals(expectedAppointment, foundAppointment);
  }
  @Test
  void getAppointmentNotFound_fail() {
    EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,()->{
          appointmentService.getAppointment(1L);
        },"Appointment was not found by id");
    assertEquals("Appointment was not found by id", thrown.getMessage());
  }

  @Test
  @Sql(statements = "INSERT INTO schedule (id, doctor_id, date) VALUES (1,null,'2020-12-13')")
  @Sql(statements = "INSERT INTO appointment (id, schedule_id, start_time, end_time, status, person_id)  VALUES (1,1,'08:00','08:30','AVAILABLE',null)")
  @Sql(statements = "INSERT INTO appointment (id, schedule_id, start_time, end_time, status, person_id)  VALUES (2,1,'12:00','12:30','AVAILABLE',null)")
  void getAppointments_success() {
    var foundAppointment = appointmentService.getAppointments();
    assertEquals(2, appointmentH2Repository.findAll().size());
    assertEquals(2, foundAppointment.size());
    assertEquals(LocalTime.of(12, 0), foundAppointment.get(1).getStartTime());
  }

  @Test
  @Sql(statements = "INSERT INTO schedule (id, doctor_id, date) VALUES (1,null,'2020-12-13')")
  @Sql(statements = "INSERT INTO appointment (id, schedule_id, start_time, end_time, status, person_id)  VALUES (1,1,'08:00','08:30','AVAILABLE',null)")
  void deleteAppointment_success() {
    assertEquals(1, appointmentService.getAppointments().size());
    appointmentService.deleteAppointment(1L);
    assertEquals(0, appointmentService.getAppointments().size());
  }
  @Test
  void deleteAppointmentNotFound_fail() {
    EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,()->{
      appointmentService.deleteAppointment(1L);
    },"Appointment was not found by id");
    assertEquals("Appointment was not found by id", thrown.getMessage());
  }

  @Test
  @Sql(statements = "INSERT INTO schedule (id, doctor_id, date) VALUES (1,null,'2020-12-13')")
  @Sql(statements = "INSERT INTO appointment (id, schedule_id, start_time, end_time, status, person_id)  VALUES (1,1,'09:00','09:30','AVAILABLE',null)")
  void updateAppointment_success() {
    assertEquals(LocalTime.of(9, 30), appointmentService.getAppointment(1L).getEndTime());
    appointmentService.updateAppointment(createAppointment(), 1L);
    assertEquals(LocalTime.of(8, 30), appointmentService.getAppointment(1L).getEndTime());
  }
  @Test
  void updateAppointmentNotFound_fail() {
    EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,()->{
      appointmentService.updateAppointment(createAppointment(),1L);
    },"Appointment was not found by id");
    assertEquals("Appointment was not found by id", thrown.getMessage());
  }

  @Test
  @Sql(statements = "INSERT INTO person (id, first_name, last_name, phone) VALUES (1,'Ivan','Kerzhov','+289456')")
  @Sql(statements = "INSERT INTO schedule (id, doctor_id, date) VALUES (1,null,'2088-12-13')")
  @Sql(statements = "INSERT INTO appointment (id, schedule_id, start_time, end_time, status, person_id)  VALUES (1,1,'23:00','23:30','AVAILABLE',null)")
  void reserveAppointment_success() {
    assertEquals(AppointmentStatus.AVAILABLE, appointmentService.getAppointment(1L).getStatus());
    appointmentService.reserveAppointment(1L, 1L);
    assertEquals(1, appointmentService.getAppointment(1L).getPersonId());
    assertEquals(AppointmentStatus.RESERVED, appointmentService.getAppointment(1L).getStatus());
  }
  @Test
  @Sql(statements = "INSERT INTO person (id, first_name, last_name, phone) VALUES (1,'Ivan','Kerzhov','+289456')")
  @Sql(statements = "INSERT INTO schedule (id, doctor_id, date) VALUES (1,null,'2088-12-13')")
  @Sql(statements = "INSERT INTO appointment (id, schedule_id, start_time, end_time, status, person_id)  VALUES (1,1,'23:00','23:30','RESERVED',1)")
  void reserveAppointmentAlreadyReserved_fail() {
    assertEquals(AppointmentStatus.RESERVED, appointmentService.getAppointment(1L).getStatus());
    AppointmentAlreadyReserved thrown = assertThrows(AppointmentAlreadyReserved.class,()->{
      appointmentService.reserveAppointment(1L,1L);
    },"This Appointment already reserved");
    assertEquals("This Appointment already reserved",thrown.getMessage());
  }
  @Test
  @Sql(statements = "INSERT INTO person (id, first_name, last_name, phone) VALUES (1,'Ivan','Kerzhov','+289456')")
  @Sql(statements = "INSERT INTO schedule (id, doctor_id, date) VALUES (1,null,'2088-12-13')")
  @Sql(statements = "INSERT INTO appointment (id, schedule_id, start_time, end_time, status, person_id)  VALUES (1,1,'23:00','23:30','AVAILABLE',null)")
  void reserveAppointmentNotFoundPerson_fail() {
    assertEquals(AppointmentStatus.AVAILABLE, appointmentService.getAppointment(1L).getStatus());
    EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,()->{
      appointmentService.reserveAppointment(1L,2L);
    },"Person was not found by id");
    assertEquals("Person was not found by id",thrown.getMessage());
  }
  @Test
  @Sql(statements = "INSERT INTO person (id, first_name, last_name, phone) VALUES (1,'Ivan','Kerzhov','+289456')")
  @Sql(statements = "INSERT INTO schedule (id, doctor_id, date) VALUES (1,null,'2020-12-13')")
  @Sql(statements = "INSERT INTO appointment (id, schedule_id, start_time, end_time, status, person_id)  VALUES (1,1,'23:00','23:30','AVAILABLE',1)")
  void reserveAppointmentLate_fail() {
    assertEquals(AppointmentStatus.AVAILABLE, appointmentService.getAppointment(1L).getStatus());
    AppointmentAlreadyReserved thrown = assertThrows(AppointmentAlreadyReserved.class,()->{
      appointmentService.reserveAppointment(1L,2L);
    },"You are late to reserve this appointment");
    assertEquals("You are late to reserve this appointment",thrown.getMessage());
  }


  @Test
  @Sql(statements = "INSERT INTO schedule (id, doctor_id, date) VALUES (1,null,'2020-12-13')")
  void generateAppointmentsForDayTest_success() {
    assertEquals(0, appointmentService.getAppointments().size());
    appointmentService.generateAppointmentsForDay(1L);
    var foundAppointments = appointmentService.getAppointments();
    assertEquals(16, foundAppointments.size());
    assertEquals(LocalTime.of(8, 0), foundAppointments.get(0).getStartTime());
    assertEquals(LocalTime.of(8, 30), foundAppointments.get(0).getEndTime());
  }

  private Appointment createAppointment() {
    return new Appointment().setStartTime(LocalTime.of(8, 0))
        .setEndTime(LocalTime.of(8, 30)).setStatus(AppointmentStatus.AVAILABLE).setScheduleId(1L);
  }
}
