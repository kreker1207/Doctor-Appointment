package com.project.appointments.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.project.appointments.Application;
import com.project.appointments.model.entity.Person.Fields;
import com.project.appointments.model.entity.Person;
import com.project.appointments.repository.PersonH2Repository;
import jakarta.persistence.EntityNotFoundException;
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
  void addPerson_success() {
    var expectedPerson = new Person().setId(1L).setFirstName("Ivan").setLastName("Baranetskiy")
        .setPhone("+123456");
    assertEquals(0, personH2Repository.findAll().size());
    var personResult = personService.addPerson(createPerson());
    assertEquals(1, personH2Repository.findAll().size());
    assertThat(personResult).usingRecursiveComparison().ignoringFields(Fields.appointments).isEqualTo(expectedPerson);
  }

  @Test
  @Sql(statements = "INSERT INTO person (id, first_name, last_name, phone) VALUES (1,'Ivan','Baranetskiy','+1234567')")
  void getPerson_success() {
    var expectedPerson = new Person().setId(1L).setFirstName("Ivan").setLastName("Baranetskiy")
        .setPhone("+1234567");
    assertEquals(1, personService.getPeople().size());
    var personResult = personService.getPerson(1L);
    assertThat(personResult).usingRecursiveComparison().ignoringFields(Fields.id,Fields.appointments).isEqualTo(expectedPerson);
  }
  @Test
  void getPersonNotFound_fail() {
    EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,()->{
      personService.getPerson(1L);
    },"Person was not found by id");
    assertEquals("Person was not found by id", thrown.getMessage());
  }

  @Test
  @Sql(statements = "INSERT INTO person (id, first_name, last_name, phone) VALUES (1,'Iven','Baranetskyi','+1234567')")
  @Sql(statements = "INSERT INTO person (id, first_name, last_name, phone) VALUES (2,'Kiril','Andrush','+654879')")
  void getPeople_success() {
    List<Person> foundPeople = personService.getPeople();
    assertEquals(2, personH2Repository.findAll().size());
    assertEquals(2, foundPeople.size());
    assertEquals("Kiril", foundPeople.get(1).getFirstName());
  }

  @Test
  @Sql(statements = "INSERT INTO person (id, first_name, last_name, phone) VALUES (1,'Iven','Baranetskyi','+1234567')")
  void deletePerson_success() {
    assertEquals(1, personService.getPeople().size());
    personService.deletePerson(1L);
    assertEquals(0, personService.getPeople().size());
  }
  @Test
  void deletePersonNotFound_fail() {
    EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,()->{
      personService.getPerson(1L);
    },"Person was not found by id");
    assertEquals("Person was not found by id", thrown.getMessage());
  }

  @Test
  @Sql(statements = "INSERT INTO person (id, first_name, last_name, phone) VALUES (1,'Boris','Chegrik','+987654321')")
  void updatePerson_success() {
    assertEquals("Boris", personService.getPerson(1L).getFirstName());
    var personResult = personService.updatePerson(createPerson(), 1L);
    var expectedPerson = new Person().setId(1L).setFirstName("Ivan").setLastName("Baranetskiy")
        .setPhone("+123456");
    assertThat(personResult).usingRecursiveComparison().ignoringFields(Fields.id,Fields.appointments).isEqualTo(expectedPerson);
  }
  @Test
  void updatePersonNotFound_fail() {
    EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,()->{
      personService.getPerson(1L);
    },"Person was not found by id");
    assertEquals("Person was not found by id", thrown.getMessage());
  }
  private Person createPerson(){
    return new Person().setId(1L).setFirstName("Ivan").setLastName("Baranetskiy")
        .setPhone("+123456");
  }

}
