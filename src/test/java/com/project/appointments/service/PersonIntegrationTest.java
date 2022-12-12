package com.project.appointments.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.project.appointments.Application;
import com.project.appointments.model.entity.Person;
import com.project.appointments.repository.PersonH2Repository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = Application.class)
@ExtendWith(SpringExtension.class)
@Transactional
public class PersonIntegrationTest {

  @Autowired
  private PersonService personService;
  @Autowired
  private PersonH2Repository personH2Repository;

  @AfterEach
  public void clearH2() {
    personH2Repository.deleteAll();
  }

  @Test
  void createPerson() {
    Person person = new Person().setId(1L).setFirstName("Ivan").setLastName("Baranetskiy")
        .setPhone("+123456");
    assertEquals(0, personH2Repository.findAll().size());
    personService.addPerson(person);
    assertEquals(1, personH2Repository.findAll().size());
  }

  @Test
  void getPerson() {
    Person person = new Person().setId(1L).setFirstName("Ivan").setLastName("Baranetskiy")
        .setPhone("+1234567");
    personService.addPerson(person);
    assertEquals(1, personService.getPeople().size());
    Person foundPerson = personService.getPerson(1L);
    assertEquals(person, foundPerson);
  }

  @Test
  @Sql(statements = "INSERT INTO person (id, first_name, last_name, phone) VALUES (1,'Iven','Baranetskyi','+1234567')")
  @Sql(statements = "INSERT INTO person (id, first_name, last_name, phone) VALUES (2,'Kiril','Andrush','+654879')")
  void getPeople() {
    List<Person> foundPeople = personService.getPeople();
    assertEquals(2, personH2Repository.findAll().size());
    assertEquals(2, foundPeople.size());
    assertEquals("Kiril", foundPeople.get(1).getFirstName());
  }

  @Test
  @Sql(statements = "INSERT INTO person (id, first_name, last_name, phone) VALUES (1,'Iven','Baranetskyi','+1234567')")
  void deletePerson() {
    assertEquals(1, personService.getPeople().size());
    personService.deletePerson(1L);
    assertEquals(0, personService.getPeople().size());
  }

  @Test
  @Sql(statements = "INSERT INTO person (id, first_name, last_name, phone) VALUES (1,'Iven','Baranetskyi','+1234567')")
  void updatePerson() {
    assertEquals("Iven", personService.getPerson(1L).getFirstName());
    personService.updatePerson(new Person().setFirstName("Boris").setLastName("Baranetskyi").setPhone("+1234567"), 1L);
    assertEquals("Boris", personService.getPerson(1L).getFirstName());

  }

}
