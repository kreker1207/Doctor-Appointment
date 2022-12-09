package com.project.appointments.repository;

import com.project.appointments.model.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentH2Repository extends JpaRepository<Appointment,Long> {

}
