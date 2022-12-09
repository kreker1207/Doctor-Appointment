package com.project.appointments.repository;

import com.project.appointments.model.entity.Person;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person,Long> {
  Optional<Person> findByPhone(String phone);
}
