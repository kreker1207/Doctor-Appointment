package com.project.appointments.repository;

import com.project.appointments.model.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorH2Repository extends JpaRepository<Doctor,Long> {

}
