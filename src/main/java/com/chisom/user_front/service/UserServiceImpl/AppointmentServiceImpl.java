package com.chisom.user_front.service.UserServiceImpl;

import com.chisom.user_front.domain.Appointment;
import com.chisom.user_front.repository.AppointmentRepository;
import com.chisom.user_front.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public Appointment createAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public Appointment findAppointment(Long id) {
        return appointmentRepository.getById(id);
    }

    public void confirmAppointment(Long id) {
        Appointment appointment = findAppointment(id);
        appointment.setConfirmed(true);
        appointmentRepository.save(appointment);
    }

}
