package com.project.appointments.repository;

import com.project.appointments.model.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleH2Repository extends JpaRepository<Schedule,Long> {

}
