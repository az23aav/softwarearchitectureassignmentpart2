package uk.ac.healthcare.model;

import java.time.LocalDateTime;
import java.util.*;

public class MedicalRecord {
    private final String recordId;
    private String allergies;
    private String chronicConditions;
    private LocalDateTime lastUpdated;

    // Composition: MedicalRecord owns notes
    private final List<ClinicalNote> notes = new ArrayList<>();

    public MedicalRecord(String recordId, String allergies, String chronicConditions) {
        if (recordId == null || recordId.isBlank()) throw new IllegalArgumentException("recordId required");
        this.recordId = recordId.trim();
        this.allergies = (allergies == null) ? "" : allergies.trim();
        this.chronicConditions = (chronicConditions == null) ? "" : chronicConditions.trim();
        this.lastUpdated = LocalDateTime.now();
    }

    public String getRecordId() { return recordId; }
    public String getAllergies() { return allergies; }
    public String getChronicConditions() { return chronicConditions; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }

    public void updateAllergies(String allergies) {
        this.allergies = (allergies == null) ? "" : allergies.trim();
        touch();
    }

    public void updateChronicConditions(String conditions) {
        this.chronicConditions = (conditions == null) ? "" : conditions.trim();
        touch();
    }

    // UML: addNote(text: String, author: Clinician)
    // For persistence simplicity we take clinicianId instead of object reference
    public ClinicalNote addNote(String noteId, String text, String authorClinicianId) {
        ClinicalNote note = new ClinicalNote(noteId, authorClinicianId, LocalDateTime.now(), text);
        notes.add(note);
        touch();
        return note;
    }

    // UML: getHistory(): List<ClinicalNote>
    public List<ClinicalNote> getHistory() {
        return Collections.unmodifiableList(notes);
    }

    private void touch() {
        this.lastUpdated = LocalDateTime.now();
    }
}

