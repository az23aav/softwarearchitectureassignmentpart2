package uk.ac.healthcare.repository;

import uk.ac.healthcare.model.Appointment;
import uk.ac.healthcare.model.AppointmentStatus;
import uk.ac.healthcare.util.CsvUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class AppointmentRepository {

    private final DataStore store;

    public AppointmentRepository(DataStore store) {
        this.store = store;
    }

    public void load(Path csv) throws IOException {
        List<Map<String, String>> rows = CsvUtils.read(csv);

        for (Map<String, String> r : rows) {
            String id = get(r, "appointment_id");
            String patientId = get(r, "patient_id");
            String clinicianId = get(r, "clinician_id");
            String facilityId = get(r, "facility_id");

            LocalDate date = parseDate(get(r, "appointment_date"));
            LocalTime time = parseTime(get(r, "appointment_time"));

            int duration = intOrZero(get(r, "duration_minutes"));
            String type = get(r, "appointment_type");

            AppointmentStatus status = parseStatus(get(r, "status"));
            String reason = get(r, "reason_for_visit");
            String notes = get(r, "notes");

            Appointment a = new Appointment(
                    id, patientId, clinicianId, facilityId,
                    date, time, duration, type, status, reason, notes
            );

            store.appointments.put(id, a);
        }
    }

    public void save(Path csv) throws IOException {
        List<String> lines = new ArrayList<>();

        // header must match your appointments.csv columns
        lines.add(String.join(",",
                "appointment_id",
                "patient_id",
                "clinician_id",
                "facility_id",
                "appointment_date",
                "appointment_time",
                "duration_minutes",
                "appointment_type",
                "status",
                "reason_for_visit",
                "notes",
                "created_date",
                "last_modified"
        ));

        for (Appointment a : store.appointments.values()) {
            String appointmentId = safe(a.getAppointmentId());
            String patientId = safe(a.getPatientId());
            String clinicianId = safe(a.getClinicianId());
            String facilityId = safe(a.getFacilityId());

            String date = (a.getDate() == null) ? "" : a.getDate().toString();
            String time = (a.getTime() == null) ? "" : a.getTime().toString();

            String duration = String.valueOf(a.getDurationMinutes());
            String type = safe(a.getType());
            String status = (a.getStatus() == null) ? "" : a.getStatus().name();
            String reason = safe(a.getReason());
            String notes = safe(a.getNotes());

            // you don't store created/modified in model yet -> write empty
            String created = "";
            String modified = "";

            lines.add(String.join(",",
                    csvEscape(appointmentId),
                    csvEscape(patientId),
                    csvEscape(clinicianId),
                    csvEscape(facilityId),
                    csvEscape(date),
                    csvEscape(time),
                    csvEscape(duration),
                    csvEscape(type),
                    csvEscape(status),
                    csvEscape(reason),
                    csvEscape(notes),
                    csvEscape(created),
                    csvEscape(modified)
            ));
        }

        Files.write(csv, lines);
    }

    private static String get(Map<String, String> r, String key) {
        String v = r.get(key);
        return v == null ? "" : v.trim();
    }

    private static int intOrZero(String s) {
        if (s == null) return 0;
        s = s.trim();
        if (s.isEmpty()) return 0;
        try { return Integer.parseInt(s); }
        catch (Exception e) { return 0; }
    }

    private static LocalDate parseDate(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty()) return null;
        return LocalDate.parse(s);
    }

    private static LocalTime parseTime(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty()) return null;
        return LocalTime.parse(s);
    }

    private static AppointmentStatus parseStatus(String s) {
        if (s == null) return AppointmentStatus.SCHEDULED;
        s = s.trim();
        if (s.isEmpty()) return AppointmentStatus.SCHEDULED;
        return AppointmentStatus.valueOf(s.toUpperCase());
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }

    // quotes CSV values that contain commas/quotes/newlines
    private static String csvEscape(String s) {
        if (s == null) return "";
        boolean needsQuotes = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        if (!needsQuotes) return s;
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }
}
