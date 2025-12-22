package uk.ac.healthcare.repository;

import uk.ac.healthcare.model.Condition;
import uk.ac.healthcare.model.Drug;
import uk.ac.healthcare.model.Prescription;
import uk.ac.healthcare.model.PrescriptionStatus;
import uk.ac.healthcare.util.CsvUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
            String patientId      = get(r, "patient_id");
            String clinicianId    = get(r, "clinician_id");
            String appointmentId  = get(r, "appointment_id");

            LocalDate prescriptionDate = parseDateOrNull(get(r, "prescription_date"));

            String medicationName = get(r, "medication_name");
            Drug drug = new Drug(medicationName);

            Condition condition = new Condition("UNKNOWN");

            String dosage       = get(r, "dosage");
            String frequency    = get(r, "frequency");
            int durationDays    = intOrZero(get(r, "duration_days"));
            int quantity        = intOrZero(get(r, "quantity"));
            String instructions = get(r, "instructions");
            String pharmacyName = get(r, "pharmacy_name");

            PrescriptionStatus status = parseStatus(get(r, "status"));
            LocalDate issueDate       = parseDateOrNull(get(r, "issue_date"));
            LocalDate collectionDate  = parseDateOrNull(get(r, "collection_date"));

            Prescription p = new Prescription(
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

            store.prescriptions.put(prescriptionId, p);
        }
    }

    private static String get(Map<String, String> r, String key) {
        String v = r.get(key);
        return v == null ? "" : v.trim();
    }

    private static int intOrZero(String raw) {
        if (raw == null) return 0;
        raw = raw.trim();
        if (raw.isEmpty()) return 0;
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static LocalDate parseDateOrNull(String raw) {
        if (raw == null) return null;
        raw = raw.trim();
        if (raw.isEmpty()) return null;
        try {
            return LocalDate.parse(raw);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static PrescriptionStatus parseStatus(String raw) {
        if (raw == null) return PrescriptionStatus.ISSUED;
        raw = raw.trim().toUpperCase();

        // CSV uses "Issued" / "Collected"
        if (raw.startsWith("ISS")) return PrescriptionStatus.ISSUED;
        if (raw.startsWith("COL")) return PrescriptionStatus.COLLECTED;

        //try enum direct
        try {
            return PrescriptionStatus.valueOf(raw);
        } catch (Exception e) {
            return PrescriptionStatus.ISSUED;
        }
    }
}
