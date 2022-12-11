package com.project.appointments.controller;

import com.project.appointments.model.entity.Doctor;
import com.project.appointments.service.DoctorService;
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
@RequestMapping("/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

  private final DoctorService doctorService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Doctor> getDoctor() {
    return doctorService.getDoctors();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Doctor getDoctor(@PathVariable Long id) {
    return doctorService.getDoctor(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Doctor addDoctor(@RequestBody Doctor doctor) {
    return doctorService.addDoctor(doctor);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Doctor deleteDoctor(@PathVariable Long id) {
    return doctorService.deleteDoctor(id);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Doctor updateDoctor(@RequestBody Doctor doctor, @PathVariable Long id) {
    return doctorService.updateDoctor(doctor, id);
  }
}
