package com.project.appointments.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.project.appointments.Application;
import com.project.appointments.model.entity.Doctor;
import com.project.appointments.repository.DoctorH2Repository;
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
  void createDoctor() {
    Doctor doctor = new Doctor().setId(1L).setFirstName("Ivan").setLastName("Baranetskiy")
        .setPhone("+123456").setSpecialization("Orthopeadic");
    assertEquals(0, doctorH2Repository.findAll().size());
    doctorService.addDoctor(doctor);
    assertEquals(1, doctorH2Repository.findAll().size());
  }

  @Test
  void getDoctor() {
    Doctor doctor = new Doctor().setId(1L).setFirstName("Ivan").setLastName("Baranetskiy")
        .setPhone("+1234567").setSpecialization("Orthopeadic");
    doctorService.addDoctor(doctor);
    assertEquals(1, doctorService.getDoctors().size());
    Doctor foundDoctor = doctorService.getDoctor(1L);
    assertEquals(doctor, foundDoctor);
  }

  @Test
  @Sql(statements = "INSERT INTO doctor (id, first_name, last_name, specialization, phone)  VALUES (1,'Iven','Baranetskyi','Orthopeadic','+1234567')")
  @Sql(statements = "INSERT INTO doctor (id, first_name, last_name, specialization, phone)  VALUES (2,'Vitaliy','Yorkshir','Orthopeadic','+487916')")
  void getDoctors() {
    List<Doctor> foundDoctor = doctorService.getDoctors();
    assertEquals(2, doctorH2Repository.findAll().size());
    assertEquals(2, foundDoctor.size());
    assertEquals("+487916", foundDoctor.get(1).getPhone());
  }

  @Test
  @Sql(statements = "INSERT INTO doctor (id, first_name, last_name, specialization, phone)  VALUES (1,'Vitaliy','Yorkshir','Orthopeadic','+487916')")
  void deleteDoctor() {
    assertEquals(1, doctorService.getDoctors().size());
    doctorService.deleteDoctor(1L);
    assertEquals(0, doctorService.getDoctors().size());
  }

  @Test
  @Sql(statements = "INSERT INTO doctor (id, first_name, last_name, specialization, phone)  VALUES (1,'Vitaliy','Yorkshir','Orthopeadic','+487916')")
  void updateDoctor() {
    assertEquals("Vitaliy", doctorService.getDoctor(1L).getFirstName());
    doctorService.updateDoctor(new Doctor().setFirstName("Boris").setLastName("Yorkshir").setSpecialization("Orthopeadic").setPhone("+487916"), 1L);
    assertEquals("Boris", doctorService.getDoctor(1L).getFirstName());

  }

}
