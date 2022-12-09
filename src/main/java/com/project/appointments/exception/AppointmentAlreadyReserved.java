package com.project.appointments.exception;

import jakarta.persistence.EntityExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AppointmentAlreadyReserved extends EntityExistsException {
  public AppointmentAlreadyReserved(String message){
    super(message);
  }

}
