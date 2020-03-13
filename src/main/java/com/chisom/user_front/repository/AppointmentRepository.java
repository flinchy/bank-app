package com.chisom.user_front.repository;

import com.chisom.user_front.domain.Appointment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {

    List<Appointment> findAll();

    Appointment getById(Long id);
}
