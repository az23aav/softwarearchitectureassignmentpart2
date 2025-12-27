package uk.ac.healthcare.controller;

import uk.ac.healthcare.model.Appointment;
import uk.ac.healthcare.repository.AppointmentRepository;
import uk.ac.healthcare.repository.DataStore;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AppointmentController {

    private final DataStore store;
    private final AppointmentRepository repo;
    private final Path appointmentsCsv;

    public AppointmentController(DataStore store, AppointmentRepository repo, Path dataDir) {
        this.store = store;
        this.repo = repo;
        this.appointmentsCsv = dataDir.resolve("appointments.csv");
    }

    public List<Appointment> getAll() {
        return new ArrayList<>(store.appointments.values());
    }

    public void add(Appointment appointment) {
        store.appointments.put(appointment.getAppointmentId(), appointment);
    }

    public void update(Appointment updated) {
        store.appointments.put(updated.getAppointmentId(), updated);
    }

    public void delete(String appointmentId) {
        store.appointments.remove(appointmentId);
    }

    public void saveNow() {
        try {
            repo.save(appointmentsCsv);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save appointments.csv", e);
        }
    }
}
