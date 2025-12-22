package uk.ac.healthcare.repository;

import uk.ac.healthcare.model.Appointment;
import uk.ac.healthcare.model.AppointmentStatus;
import uk.ac.healthcare.util.CsvUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

public class AppointmentRepository {

    private final DataStore store;

    public AppointmentRepository(DataStore store) {
        this.store = store;
    }

    public void load(Path csv) throws IOException {
        List<Map<String, String>> rows = CsvUtils.read(csv);

        for (Map<String, String> r : rows) {

            String appointmentId = get(r, "appointment_id");
            String patientId     = get(r, "patient_id");
            String clinicianId   = get(r, "clinician_id");
            String facilityId    = get(r, "facility_id");

            LocalDate date = parseDateOrNull(get(r, "appointment_date"));
            LocalTime time = parseTimeOrNull(get(r, "appointment_time"));

            int durationMinutes = intOrZero(get(r, "duration_minutes"));
            String type   = get(r, "appointment_type");
            AppointmentStatus status = parseStatus(get(r, "status"));

            String reason = get(r, "reason_for_visit");
            String notes  = get(r, "notes");

            // created_date + last_modified exist in CSV but your model doesn't store them -> ignore for now
            Appointment appt = new Appointment(
                    appointmentId,
                    patientId,
                    clinicianId,
                    facilityId,
                    date,
                    time,
                    durationMinutes,
                    type,
                    status,
                    reason,
                    notes
            );

            store.appointments.put(appointmentId, appt);
        }
    }

    // ---------- helpers ----------

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
            return LocalDate.parse(raw); // yyyy-MM-dd
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static LocalTime parseTimeOrNull(String raw) {
        if (raw == null) return null;
        raw = raw.trim();
        if (raw.isEmpty()) return null;
        try {
            return LocalTime.parse(raw); // HH:mm
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static AppointmentStatus parseStatus(String raw) {
        if (raw == null) return AppointmentStatus.SCHEDULED;
        raw = raw.trim().toUpperCase();

        // Handle CSV values like "Scheduled", "Cancelled"
        if (raw.startsWith("SCHED")) return AppointmentStatus.SCHEDULED;
        if (raw.startsWith("CANC"))  return AppointmentStatus.CANCELLED;
        if (raw.startsWith("COMP"))  return AppointmentStatus.COMPLETED;

        // Fallback: try enum direct
        try {
            return AppointmentStatus.valueOf(raw);
        } catch (Exception e) {
            return AppointmentStatus.SCHEDULED;
        }
    }
}
