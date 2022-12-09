package com.project.appointments.service;

import com.project.appointments.model.entity.Doctor;
import com.project.appointments.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorService {

  private final DoctorRepository doctorRepository;

  public List<Doctor> getDoctors() {
    return doctorRepository.findAll();
  }

  public Doctor getDoctor(Long id) {
    return findDoctorById(id);
  }

  public Doctor addDoctor(Doctor doctor) {
    return doctorRepository.save(doctor);
  }

  public Doctor updateDoctor(Doctor doctor, Long id) {
    Doctor saveDoctor = findDoctorById(id);
    return doctorRepository.save(
        saveDoctor.setName(doctor.getName()).setSurname(doctor.getSurname())
            .setPhone(doctor.getPhone()).setSpecialization(doctor.getSpecialization()));
  }

  public Doctor deleteDoctor(Long id) {
    Doctor doctor = findDoctorById(id);
    doctorRepository.deleteById(id);
    return doctor;
  }

  private Doctor findDoctorById(Long id) {
    return doctorRepository.findById(id).orElseThrow(() -> {
      throw new EntityNotFoundException("Doctor was not found by id");
    });
  }
}
