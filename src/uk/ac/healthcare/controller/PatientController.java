package uk.ac.healthcare.controller;

import uk.ac.healthcare.model.Patient;
import uk.ac.healthcare.repository.DataStore;

import java.util.ArrayList;
import java.util.List;

public class PatientController {

    private final DataStore store;

    public PatientController(DataStore store) {
        this.store = store;
    }

    public List<Patient> getAll() {
        return new ArrayList<>(store.patients.values());
    }

    public void add(Patient patient) {
        store.patients.put(patient.getUserId(), patient);
    }

    public void delete(String patientId) {
        store.patients.remove(patientId);
    }

    public Patient getById(String patientId) {
        return store.patients.get(patientId);
    }
}
