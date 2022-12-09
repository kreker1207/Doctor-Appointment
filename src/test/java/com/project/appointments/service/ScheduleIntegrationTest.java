package com.project.appointments.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.project.appointments.Application;

import com.project.appointments.model.entity.Schedule;
import com.project.appointments.model.entity.UtilityDateSet;
import com.project.appointments.repository.ScheduleH2Repository;
import java.time.LocalDate;
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
public class ScheduleIntegrationTest {

  @Autowired
  private ScheduleService scheduleService;
  @Autowired
  private ScheduleH2Repository scheduleH2Repository;

  @AfterEach
  public void clearH2() {
    scheduleH2Repository.deleteAll();
  }

  @Test
  void createSchedule() {
    Schedule schedule = new Schedule().setId(1L).setDoctorId(null)
        .setDate(LocalDate.of(2022, 12, 12));
    assertEquals(0, scheduleH2Repository.findAll().size());
    scheduleService.addSchedule(schedule);
    assertEquals(1, scheduleH2Repository.findAll().size());
  }

  @Test
  @Sql(statements = "INSERT INTO schedule (id, doctor_id, date) VALUES (1,null,'2022-12-13')")
  void getSchedule() {
    assertEquals(1, scheduleService.getSchedule().size());
    Schedule foundSchedule = scheduleService.getSchedule(1L);
    assertEquals(LocalDate.of(2022, 12, 13), foundSchedule.getDate());
  }

  @Test
  @Sql(statements = "INSERT INTO schedule (id, doctor_id, date) VALUES (1,null,'2022-12-13')")
  @Sql(statements = "INSERT INTO schedule (id, doctor_id, date) VALUES (2,null,'2022-12-14')")
  void getSchedules() {
    List<Schedule> foundSchedule = scheduleService.getSchedule();
    assertEquals(2, scheduleH2Repository.findAll().size());
    assertEquals(2, foundSchedule.size());
    assertEquals(LocalDate.of(2022, 12, 14), foundSchedule.get(1).getDate());
  }

  @Test
  @Sql(statements = "INSERT INTO schedule (id, doctor_id, date) VALUES (1,null,'2022-12-13')")
  void deleteSchedule() {
    assertEquals(1, scheduleService.getSchedule().size());
    scheduleService.deleteSchedule(1L);
    assertEquals(0, scheduleService.getSchedule().size());
  }

  @Test
  @Sql(statements = "INSERT INTO schedule (id, doctor_id, date) VALUES (1,null,'2022-12-13')")
  void updateSchedule() {
    assertEquals(LocalDate.of(2022, 12, 13), scheduleService.getSchedule(1L).getDate());
    scheduleService.updateSchedule(new Schedule().setDate(LocalDate.of(2022, 12, 15)), 1L);
    assertEquals(LocalDate.of(2022, 12, 15), scheduleService.getSchedule(1L).getDate());
  }

  @Test
  @Sql(statements = "INSERT INTO doctor (id, name, surname, specialization, phone)  VALUES (1,'Iven','Baranetskyi','Orthopeadic','+1234567')")
  void generateScheduleForThreeDaysMonday() {
    assertEquals(0, scheduleService.getSchedule().size());
    scheduleService.generateScheduleForThreeDaysTest(1L,
        UtilityDateSet.someDate = LocalDate.of(2022, 12, 5));
    List<Schedule> foundSchedule = scheduleService.getSchedule();
    assertEquals(3, foundSchedule.size());
    assertEquals(LocalDate.of(2022, 12, 5), foundSchedule.get(0).getDate());
    assertEquals(LocalDate.of(2022, 12, 6), foundSchedule.get(1).getDate());
    assertEquals(LocalDate.of(2022, 12, 7), foundSchedule.get(2).getDate());
  }

  @Test
  @Sql(statements = "INSERT INTO doctor (id, name, surname, specialization, phone)  VALUES (1,'Iven','Baranetskyi','Orthopeadic','+1234567')")
  void generateScheduleForThreeThursday() {
    assertEquals(0, scheduleService.getSchedule().size());
    scheduleService.generateScheduleForThreeDaysTest(1L,
        UtilityDateSet.someDate = LocalDate.of(2022, 12, 8));
    List<Schedule> foundSchedule = scheduleService.getSchedule();
    assertEquals(3, foundSchedule.size());
    assertEquals(LocalDate.of(2022, 12, 8), foundSchedule.get(0).getDate());
    assertEquals(LocalDate.of(2022, 12, 9), foundSchedule.get(1).getDate());
    assertEquals(LocalDate.of(2022, 12, 12), foundSchedule.get(2).getDate());
  }

  @Test
  @Sql(statements = "INSERT INTO doctor (id, name, surname, specialization, phone)  VALUES (1,'Iven','Baranetskyi','Orthopeadic','+1234567')")
  void generateScheduleForThreeFriday() {
    assertEquals(0, scheduleService.getSchedule().size());
    scheduleService.generateScheduleForThreeDaysTest(1L,
        UtilityDateSet.someDate = LocalDate.of(2022, 12, 9));
    List<Schedule> foundSchedule = scheduleService.getSchedule();
    assertEquals(3, foundSchedule.size());
    assertEquals(LocalDate.of(2022, 12, 9), foundSchedule.get(0).getDate());
    assertEquals(LocalDate.of(2022, 12, 12), foundSchedule.get(1).getDate());
    assertEquals(LocalDate.of(2022, 12, 13), foundSchedule.get(2).getDate());
  }

  @Test
  @Sql(statements = "INSERT INTO doctor (id, name, surname, specialization, phone)  VALUES (1,'Iven','Baranetskyi','Orthopeadic','+1234567')")
  void generateScheduleForThreeSaturday() {
    assertEquals(0, scheduleService.getSchedule().size());
    scheduleService.generateScheduleForThreeDaysTest(1L,
        UtilityDateSet.someDate = LocalDate.of(2022, 12, 10));
    List<Schedule> foundSchedule = scheduleService.getSchedule();
    assertEquals(3, foundSchedule.size());
    assertEquals(LocalDate.of(2022, 12, 12), foundSchedule.get(0).getDate());
    assertEquals(LocalDate.of(2022, 12, 13), foundSchedule.get(1).getDate());
    assertEquals(LocalDate.of(2022, 12, 14), foundSchedule.get(2).getDate());
  }

  @Test
  @Sql(statements = "INSERT INTO doctor (id, name, surname, specialization, phone)  VALUES (1,'Iven','Baranetskyi','Orthopeadic','+1234567')")
  void generateScheduleForThreeSunday() {
    assertEquals(0, scheduleService.getSchedule().size());
    scheduleService.generateScheduleForThreeDaysTest(1L,
        UtilityDateSet.someDate = LocalDate.of(2022, 12, 11));
    List<Schedule> foundSchedule = scheduleService.getSchedule();
    assertEquals(3, foundSchedule.size());
    assertEquals(LocalDate.of(2022, 12, 12), foundSchedule.get(0).getDate());
    assertEquals(LocalDate.of(2022, 12, 13), foundSchedule.get(1).getDate());
    assertEquals(LocalDate.of(2022, 12, 14), foundSchedule.get(2).getDate());
  }
}
