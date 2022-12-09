package com.project.appointments.controller;

import com.project.appointments.model.entity.Person;
import com.project.appointments.service.PersonService;
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
@RequestMapping("/v1/person")
@RequiredArgsConstructor
public class PersonController {

  private final PersonService personService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Person> getPerson() {
    return personService.getPeople();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Person getPerson(@PathVariable Long id) {
    return personService.getPerson(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Person addPerson(Person person) {
    return personService.addPerson(person);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Person deletePerson(@PathVariable Long id) {
    return personService.deletePerson(id);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Person updatePerson(@RequestBody Person person, @PathVariable Long id) {
    return personService.updatePerson(person, id);
  }
}
