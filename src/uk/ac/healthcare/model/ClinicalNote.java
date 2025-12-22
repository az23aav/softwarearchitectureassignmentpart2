package uk.ac.healthcare.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class ClinicalNote {
    private final String noteId;
    private final LocalDateTime timestamp;
    private String text;
    private final String authorClinicianId;

    public ClinicalNote(String noteId, String authorClinicianId, LocalDateTime timestamp, String text) {
        if (noteId == null || noteId.isBlank()) throw new IllegalArgumentException("noteId required");
        if (authorClinicianId == null || authorClinicianId.isBlank()) throw new IllegalArgumentException("authorClinicianId required");
        if (timestamp == null) throw new IllegalArgumentException("timestamp required");
        if (text == null || text.isBlank()) throw new IllegalArgumentException("text required");

        this.noteId = noteId.trim();
        this.authorClinicianId = authorClinicianId.trim();
        this.timestamp = timestamp;
        this.text = text.trim();
    }

    public String getNoteId() {
        return noteId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getText() {
        return text;
    }

    public String getAuthorClinicianId() {
        return authorClinicianId;
    }

    public void updateText(String newText) {
        if (newText == null || newText.isBlank()) throw new IllegalArgumentException("newText required");
        this.text = newText.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClinicalNote)) return false;
        ClinicalNote that = (ClinicalNote) o;
        return Objects.equals(noteId, that.noteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteId);
    }
}
