package uk.ac.healthcare.controller;

import uk.ac.healthcare.model.Prescription;
import uk.ac.healthcare.repository.DataStore;
import uk.ac.healthcare.repository.PrescriptionRepository;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionController {

    private final DataStore store;
    private final PrescriptionRepository repo;
    private final Path dataDir;

    public PrescriptionController(DataStore store, PrescriptionRepository repo, Path dataDir) {
        this.store = store;
        this.repo = repo;
        this.dataDir = dataDir;
    }

    public List<Prescription> getAll() {
        return new ArrayList<>(store.prescriptions.values());
    }

    public Prescription getById(String prescriptionId) {
        return store.prescriptions.get(prescriptionId);
    }

    public void add(Prescription p) {
        store.prescriptions.put(p.getPrescriptionId(), p);
    }

    public void delete(String prescriptionId) {
        store.prescriptions.remove(prescriptionId);
    }

    public void saveNow() {
        try {
            repo.save(dataDir.resolve("prescriptions.csv"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to save prescriptions.csv", e);
        }
    }
}
