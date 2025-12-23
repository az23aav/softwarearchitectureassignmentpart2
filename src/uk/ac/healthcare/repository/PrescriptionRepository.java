package uk.ac.healthcare.repository;

import uk.ac.healthcare.model.*;
import uk.ac.healthcare.util.CsvUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PrescriptionRepository {

    private final DataStore store;

    public PrescriptionRepository(DataStore store) {
        this.store = store;
    }

    public void load(Path csv) throws IOException {
        List<Map<String, String>> rows = CsvUtils.read(csv);

        for (Map<String, String> r : rows) {
            String prescriptionId = get(r, "prescription_id");
            String patientId = get(r, "patient_id");
            String clinicianId = get(r, "clinician_id");
            String appointmentId = get(r, "appointment_id");

            LocalDate prescriptionDate = parseDateOrNull(get(r, "prescription_date"));
            String medicationName = get(r, "medication_name"); // your CSV header
            String dosage = get(r, "dosage");
            String frequency = get(r, "frequency");
            String durationDays = get(r, "duration_days");      // keep as STRING (e.g. "28")
            String quantity = extractLeadingIntAsString(get(r, "quantity")); // "28 tablets" -> "28"
            String instructions = get(r, "instructions");
            String pharmacyName = get(r, "pharmacy_name");

            PrescriptionStatus status = parsePrescriptionStatus(get(r, "status"));
            LocalDate issueDate = parseDateOrNull(get(r, "issue_date"));
            LocalDate collectionDate = parseDateOrNull(get(r, "collection_date"));

            String drug = medicationName;
            String condition = "UNKNOWN";

            Prescription prescription = new Prescription(
                    prescriptionId,
                    patientId,
                    clinicianId,
                    appointmentId,
                    prescriptionDate,
                    drug,
                    condition,
                    dosage,
                    frequency,
                    durationDays,
                    quantity,
                    instructions,
                    pharmacyName,
                    status,
                    issueDate,
                    collectionDate
            );

            store.prescriptions.put(prescriptionId, prescription);
        }
    }

    private static String get(Map<String, String> r, String key) {
        String v = r.get(key);
        return v == null ? "" : v.trim();
    }

    private static LocalDate parseDateOrNull(String raw) {
        if (raw == null) return null;
        raw = raw.trim();
        if (raw.isEmpty()) return null;
        return LocalDate.parse(raw); // yyyy-MM-dd
    }

    private static PrescriptionStatus parsePrescriptionStatus(String raw) {
        if (raw == null || raw.isBlank()) return PrescriptionStatus.ISSUED;
        String norm = raw.trim().toUpperCase().replace(' ', '_');
        // CSV uses "Issued" / "Collected"
        return PrescriptionStatus.valueOf(norm);
    }

    // turns "28 tablets" -> "28", "" -> "0", "7" -> "7"
    private static String extractLeadingIntAsString(String raw) {
        if (raw == null) return "0";
        raw = raw.trim();
        if (raw.isEmpty()) return "0";

        StringBuilder digits = new StringBuilder();
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            if (Character.isDigit(c)) digits.append(c);
            else break;
        }
        return digits.length() == 0 ? "0" : digits.toString();
    }
}
