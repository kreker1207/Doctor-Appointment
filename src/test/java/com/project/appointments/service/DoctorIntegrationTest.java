package com.project.appointments.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.project.appointments.Application;
import com.project.appointments.model.entity.Doctor.Fields;
import com.project.appointments.model.entity.Doctor;
import com.project.appointments.repository.DoctorH2Repository;
import jakarta.persistence.EntityNotFoundException;
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
public class DoctorIntegrationTest {

  @Autowired
  private DoctorService doctorService;
  @Autowired
  private DoctorH2Repository doctorH2Repository;

  @AfterEach
  public void clearH2() {
    doctorH2Repository.deleteAll();
  }

  @Test
  void createDoctor_success() {
    var expectedDoctor = new Doctor().setId(1L).setFirstName("Ivan").setLastName("Baranetskiy")
        .setPhone("+123456").setSpecialization("Orthopeadic");
    assertEquals(0, doctorH2Repository.findAll().size());
    var doctorResult = doctorService.addDoctor(createDoctor());
    assertEquals(1, doctorH2Repository.findAll().size());
    assertThat(doctorResult).usingRecursiveComparison().isEqualTo(expectedDoctor);
  }

  @Test
  @Sql(statements = "INSERT INTO doctor (id, first_name, last_name, specialization, phone)  VALUES (1,'Ivan','Baranetskiy','Orthopeadic','+1234567')")
  void getDoctor_success() {
    var expectedDoctor = new Doctor().setId(1L).setFirstName("Ivan").setLastName("Baranetskiy")
        .setPhone("+1234567").setSpecialization("Orthopeadic");
    assertEquals(1, doctorService.getDoctors().size());
    var foundDoctor = doctorService.getDoctor(1L);
    assertThat(foundDoctor).usingRecursiveComparison().ignoringFields(Fields.id, Fields.schedules)
        .isEqualTo(expectedDoctor);
  }
  @Test
  void getDoctorNotFound_fail() {
    EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,()->{
      doctorService.getDoctor(1L);
    },"Doctor was not found by id");
    assertEquals("Doctor was not found by id", thrown.getMessage());
  }

  @Test
  @Sql(statements = "INSERT INTO doctor (id, first_name, last_name, specialization, phone)  VALUES (1,'Iven','Baranetskyi','Orthopeadic','+1234567')")
  @Sql(statements = "INSERT INTO doctor (id, first_name, last_name, specialization, phone)  VALUES (2,'Vitaliy','Yorkshir','Orthopeadic','+487916')")
  void getDoctors_success() {
    var foundDoctor = doctorService.getDoctors();
    assertEquals(2, doctorH2Repository.findAll().size());
    assertEquals(2, foundDoctor.size());
    assertEquals("+487916", foundDoctor.get(1).getPhone());
  }

  @Test
  @Sql(statements = "INSERT INTO doctor (id, first_name, last_name, specialization, phone)  VALUES (1,'Vitaliy','Yorkshir','Orthopeadic','+487916')")
  void deleteDoctor_success() {
    assertEquals(1, doctorService.getDoctors().size());
    doctorService.deleteDoctor(1L);
    assertEquals(0, doctorService.getDoctors().size());
  }
  @Test
  void deleteDoctorNotFound_fail() {
    EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,()->{
      doctorService.getDoctor(1L);
    },"Doctor was not found by id");
    assertEquals("Doctor was not found by id", thrown.getMessage());
  }

  @Test
  @Sql(statements = "INSERT INTO doctor (id, first_name, last_name, specialization, phone)  VALUES (1,'Vitaliy','Yorkshir','Orthopeadic','+487916')")
  void updateDoctor_success() {
    assertEquals("Vitaliy", doctorService.getDoctor(1L).getFirstName());
    var expectedDoctor = new Doctor().setId(1L).setFirstName("Ivan").setLastName("Baranetskiy")
        .setPhone("+123456").setSpecialization("Orthopeadic");
    var doctorResult = doctorService.updateDoctor(createDoctor(), 1L);
    assertThat(doctorResult).usingRecursiveComparison().ignoringFields(Fields.id, Fields.schedules)
        .isEqualTo(expectedDoctor);
  }
  @Test
  void updateDoctorNotFound_fail() {
    EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,()->{
      doctorService.getDoctor(1L);
    },"Doctor was not found by id");
    assertEquals("Doctor was not found by id", thrown.getMessage());
  }

  private Doctor createDoctor() {
    return new Doctor().setId(1L).setFirstName("Ivan").setLastName("Baranetskiy")
        .setPhone("+123456").setSpecialization("Orthopeadic");
  }

}
