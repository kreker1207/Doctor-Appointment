package com.project.appointments.controller;

import com.project.appointments.model.entity.Schedule;
import com.project.appointments.service.ScheduleService;
import java.time.LocalDate;
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
@RequestMapping("/v1/schedule")
public class ScheduleController {

  private final ScheduleService scheduleService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Schedule> getSchedules() {
    return scheduleService.getSchedule();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Schedule getSchedule(@PathVariable Long id) {
    return scheduleService.getSchedule(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Schedule addSchedule(Schedule schedule) {
    return scheduleService.addSchedule(schedule);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Schedule deleteSchedule(@PathVariable Long id) {
    return scheduleService.deleteSchedule(id);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Schedule updateSchedule(@RequestBody Schedule schedule, @PathVariable Long id) {
    return scheduleService.updateSchedule(schedule, id);
  }

  @PostMapping("/generate/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void generateScheduleForDoctor(@PathVariable Long id) {
    scheduleService.generateScheduleForThreeDays(id);
  }
  @PostMapping("/generate/test/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void generateScheduleForDoctorTest(@PathVariable Long id,@RequestBody LocalDate someDate) {
    scheduleService.generateScheduleForThreeDaysTest(id,someDate);
  }
}
