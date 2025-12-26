package uk.ac.healthcare.repository;

import uk.ac.healthcare.model.Prescription;
import uk.ac.healthcare.model.PrescriptionStatus;
import uk.ac.healthcare.util.CsvUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
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

            String drug = get(r, "medication_name");     // column name in CSV
            String condition = "UNKNOWN";                // you don’t have it in CSV

            String dosage = get(r, "dosage");
            String frequency = get(r, "frequency");
            String durationDays = get(r, "duration_days");
            String quantity = get(r, "quantity");

            String instructions = get(r, "instructions");
            String pharmacyName = get(r, "pharmacy_name");

            PrescriptionStatus status = parseStatusOrNull(get(r, "status"));

            LocalDate issueDate = parseDateOrNull(get(r, "issue_date"));
            LocalDate collectionDate = parseDateOrNull(get(r, "collection_date"));

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

    public void save(Path csv) throws IOException {
        List<String> lines = new ArrayList<>();

        lines.add(String.join(",",
                "prescription_id",
                "patient_id",
                "clinician_id",
                "appointment_id",
                "prescription_date",
                "medication_name",
                "dosage",
                "frequency",
                "duration_days",
                "quantity",
                "instructions",
                "pharmacy_name",
                "status",
                "issue_date",
                "collection_date"
        ));

        for (Prescription p : store.prescriptions.values()) {
            lines.add(String.join(",",
                    esc(p.getPrescriptionId()),
                    esc(p.getPatientId()),
                    esc(p.getClinicianId()),
                    esc(p.getAppointmentId()),
                    esc(dateToStr(p.getPrescriptionDate())),
                    esc(p.getDrug()),
                    esc(p.getDosage()),
                    esc(p.getFrequency()),
                    esc(p.getDurationDays()),
                    esc(p.getQuantity()),
                    esc(p.getInstructions()),
                    esc(p.getPharmacyName()),
                    esc(p.getStatus() == null ? "" : p.getStatus().name()),
                    esc(dateToStr(p.getIssueDate())),
                    esc(dateToStr(p.getCollectionDate()))
            ));
        }

        Files.write(csv, lines);
    }

    // ---------- helpers ----------
    private static String get(Map<String, String> r, String key) {
        String v = r.get(key);
        return v == null ? "" : v.trim();
    }

    private static LocalDate parseDateOrNull(String raw) {
        if (raw == null) return null;
        raw = raw.trim();
        if (raw.isEmpty()) return null;
        return LocalDate.parse(raw);
    }

    private static PrescriptionStatus parseStatusOrNull(String raw) {
        if (raw == null) return null;
        raw = raw.trim();
        if (raw.isEmpty()) return null;
        return PrescriptionStatus.valueOf(raw.toUpperCase());
    }

    private static String dateToStr(LocalDate d) {
        return d == null ? "" : d.toString();
    }

    // CSV escaping (commas/quotes/newlines)
    private static String esc(String s) {
        if (s == null) return "";
        boolean needs = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        if (!needs) return s;
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }
}
