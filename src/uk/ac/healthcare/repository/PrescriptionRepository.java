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

            LocalDate prescriptionDate = parseDate(get(r, "prescription_date"));
            LocalDate issueDate = parseDate(get(r, "issue_date"));
            LocalDate collectionDate = parseDate(get(r, "collection_date"));

            Drug drug = new Drug(get(r, "medication_name"));
            Condition condition = new Condition("UNKNOWN"); // 👈 as requested

            String dosage = get(r, "dosage");
            String frequency = get(r, "frequency");
            int durationDays = intOrZero(get(r, "duration_days"));
            int quantity = intOrZero(get(r, "quantity"));
            String instructions = get(r, "instructions");
            String pharmacyName = get(r, "pharmacy_name");

            PrescriptionStatus status =
                    PrescriptionStatus.valueOf(get(r, "status").toUpperCase());

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

    private static int intOrZero(String raw) {
        if (raw == null || raw.isBlank()) return 0;
        return Integer.parseInt(raw.trim());
    }

    private static LocalDate parseDate(String raw) {
        if (raw == null || raw.isBlank()) return null;
        return LocalDate.parse(raw.trim()); // yyyy-MM-dd
    }
}
