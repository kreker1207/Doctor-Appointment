package com.project.appointments.repository;

import com.project.appointments.model.entity.Schedule;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
  Optional<Schedule> findByDoctorIdAndDate(Long doctorId, LocalDate date);
}
