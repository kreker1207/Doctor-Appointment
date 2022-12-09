package com.project.appointments.service;

import com.project.appointments.model.entity.Person;
import com.project.appointments.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonService {
  private final PersonRepository personRepository;
  public List<Person> getPeople() {
    return personRepository.findAll();
  }

  public Person getPerson(Long id) {
    return findPersonById(id);
  }

  public Person addPerson(Person person) {
    return personRepository.save(person);
  }

  public Person updatePerson(Person person, Long id) {
    Person savePerson = findPersonById(id);
    return personRepository.save(
        savePerson.setName(person.getName()).setSurname(person.getSurname())
            .setPhone(person.getPhone()));
  }

  public Person deletePerson(Long id) {
    Person person = findPersonById(id);
    personRepository.deleteById(id);
    return person;
  }

  private Person findPersonById(Long id) {
    return personRepository.findById(id).orElseThrow(() -> {
      throw new EntityNotFoundException("Person was not found by id");
    });
  }
}
