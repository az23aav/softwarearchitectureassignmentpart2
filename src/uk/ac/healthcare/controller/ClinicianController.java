package uk.ac.healthcare.controller;

import uk.ac.healthcare.model.Clinician;
import uk.ac.healthcare.repository.ClinicianRepository;
import uk.ac.healthcare.repository.DataStore;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ClinicianController {

    private final DataStore store;
    private final ClinicianRepository repo;
    private final Path dataDir;

    public ClinicianController(DataStore store, ClinicianRepository repo, Path dataDir) {
        this.store = store;
        this.repo = repo;
        this.dataDir = dataDir;
    }

    public List<Clinician> getAll() {
        return new ArrayList<>(store.clinicians.values());
    }

    public Clinician getById(String id) {
        return store.clinicians.get(id);
    }

    public void add(Clinician c) {
        store.clinicians.put(c.getClinicianId(), c);
    }

    public void update(Clinician c) {
        store.clinicians.put(c.getClinicianId(), c);
    }

    public void delete(String clinicianId) {
        store.clinicians.remove(clinicianId);
    }

    public void saveNow() {
        try {
            repo.save(dataDir.resolve("clinicians.csv"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to save clinicians.csv", e);
        }
    }
}
