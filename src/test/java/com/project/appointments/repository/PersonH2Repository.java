package com.project.appointments.repository;

import com.project.appointments.model.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonH2Repository extends JpaRepository<Person,Long> {

}
