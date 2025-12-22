package uk.ac.healthcare.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MedicalRecord {

    private final String recordId;
    private String allergies;
    private String chronicConditions;
    private LocalDateTime lastUpdated;
    private final List<ClinicalNote> notes = new ArrayList<>();

    public MedicalRecord(String recordId) {
        this.recordId = recordId;
        this.lastUpdated = LocalDateTime.now();
    }

    public void addNote(String text, Clinician author) {
        String noteId = UUID.randomUUID().toString();
        String authorId = author.getClinicianId(); // or author.getClinicianId() depending on your class
        LocalDateTime now = LocalDateTime.now();

        notes.add(new ClinicalNote(noteId, authorId, now, text));
        lastUpdated = now;
    }

    public List<ClinicalNote> getHistory() {
        return notes;
    }

    public String getRecordId() {
        return recordId;
    }
}
