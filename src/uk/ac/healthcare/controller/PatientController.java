package uk.ac.healthcare.controller;

import uk.ac.healthcare.model.Patient;
import uk.ac.healthcare.repository.DataStore;
import uk.ac.healthcare.repository.PatientRepository;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PatientController {

    private final DataStore store;
    private final PatientRepository repo;
    private final Path patientsCsv;

    public PatientController(DataStore store, PatientRepository repo, Path patientsCsv) {
        this.store = store;
        this.repo = repo;
        this.patientsCsv = patientsCsv;
    }

    public List<Patient> getAll() {
        return new ArrayList<>(store.patients.values());
    }

    public void add(Patient patient) {
        store.patients.put(patient.getUserId(), patient);
        saveNow();
    }

    public void delete(String patientId) {
        store.patients.remove(patientId);
        saveNow();
    }

    public Patient getById(String patientId) {
        return store.patients.get(patientId);
    }

    private void saveNow() {
        try {
            repo.save(patientsCsv);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save patients.csv", e);
        }
    }
}

