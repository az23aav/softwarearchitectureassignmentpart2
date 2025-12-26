package uk.ac.healthcare.controller;

import uk.ac.healthcare.model.Patient;
import uk.ac.healthcare.repository.DataStore;
import uk.ac.healthcare.repository.PatientRepository;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PatientController {

    private final DataStore store;
    private final PatientRepository patientRepo;
    private final Path dataDir;

    public PatientController(DataStore store, PatientRepository patientRepo, Path dataDir) {
        this.store = store;
        this.patientRepo = patientRepo;
        this.dataDir = dataDir;
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

    public void save() {
        try {
            patientRepo.save(dataDir.resolve("patients.csv"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to save patients.csv", e);
        }
    }
}


