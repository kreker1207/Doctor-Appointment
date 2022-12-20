package com.project.appointments.service;

import static com.project.appointments.model.entity.TimeSetter.defineLocalDayTimeWithOffset;

import com.project.appointments.model.entity.Schedule;
import com.project.appointments.repository.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

  private final ScheduleRepository scheduleRepository;
  private final AppointmentService appointmentService;

  public List<Schedule> getSchedule() {
    return scheduleRepository.findAll();
  }

  public Schedule getSchedule(Long id) {
    return findScheduleById(id);
  }

  public Schedule addSchedule(Schedule schedule) {
    return scheduleRepository.save(schedule);
  }

  public Schedule updateSchedule(Schedule schedule, Long id) {
    Schedule saveSchedule = findScheduleById(id);
    return scheduleRepository.save(
        saveSchedule.setDate(schedule.getDate()).setDoctorId(schedule.getDoctorId()));
  }

  public Schedule deleteSchedule(Long id) {
    Schedule schedule = findScheduleById(id);
    scheduleRepository.deleteById(id);
    return schedule;
  }

  public void generateScheduleForThreeDays(Long doctorId) {
    scheduleSaveCycle(defineLocalDayTimeWithOffset(), doctorId);
  }

  private void scheduleSaveCycle(LocalDateTime date, Long doctorId) {
    for (int i = 0; i < 3; i++) {
      date = weekendValidation(date);
      if (scheduleRepository.findByDoctorIdAndDate(doctorId, date.toLocalDate()).isEmpty()) {
        Schedule schedule = scheduleRepository.save(new Schedule().setDate(date.toLocalDate()))
            .setDoctorId(doctorId);
        appointmentService.generateAppointmentsForDay(schedule.getId());
      }
      date = date.plusDays(1);
    }
  }

  private Schedule findScheduleById(Long id) {
    return scheduleRepository.findById(id).orElseThrow(() -> {
      throw new EntityNotFoundException("Schedule was not found by id");
    });
  }

  private LocalDateTime weekendValidation(LocalDateTime date) {
    if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
      return date.plusDays(1);
    } else if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
      return date.plusDays(2);
    }
    return date;
  }
}
