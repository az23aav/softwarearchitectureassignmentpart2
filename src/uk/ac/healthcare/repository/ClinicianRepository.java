package uk.ac.healthcare.repository;

import uk.ac.healthcare.model.*;
import uk.ac.healthcare.util.CsvUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

public class ClinicianRepository {

    private final DataStore store;

    public ClinicianRepository(DataStore store) {
        this.store = store;
    }

    public void load(Path csv) throws IOException {
        List<Map<String, String>> rows = CsvUtils.read(csv);

        for (Map<String, String> r : rows) {
            String id = get(r, "clinician_id");
            String first = get(r, "first_name");
            String last = get(r, "last_name");
            String title = get(r, "title");              // GP / Consultant / Senior Nurse / etc
            String speciality = get(r, "speciality");
            String regNo = get(r, "gmc_number");         // for nurses your CSV might contain NMC values in same column
            String email = get(r, "email");
            String workplaceId = get(r, "workplace_id");

            Clinician clinician = createClinicianFromTitle(
                    title, id, first, last, email, speciality, regNo, workplaceId
            );

            store.clinicians.put(id, clinician);
        }
    }

    public void save(Path csv) throws IOException {
        List<String> lines = new ArrayList<>();

        // Keep same header order as your clinicians.csv
        lines.add(String.join(",",
                "clinician_id","first_name","last_name","title","speciality",
                "gmc_number","phone_number","email","workplace_id",
                "workplace_type","employment_status","start_date"
        ));

        for (Clinician c : store.clinicians.values()) {
            String title = clinicianTitle(c);

            // You may not store these fields in your model, so keep empty strings for now
            String phone = "";
            String workplaceType = "";
            String employmentStatus = "";
            String startDate = "";

            lines.add(String.join(",",
                    esc(c.getClinicianId()),
                    esc(c.getFirstName()),
                    esc(c.getLastName()),
                    esc(title),
                    esc(c.getSpeciality()),
                    esc(c.getRegistrationNo()),
                    esc(phone),
                    esc(c.getEmail()),
                    esc(c.getWorkplaceId()),
                    esc(workplaceType),
                    esc(employmentStatus),
                    esc(startDate)
            ));
        }

        Files.write(csv, lines);
    }

    // ---------- helpers ----------

    private static Clinician createClinicianFromTitle(
            String title,
            String clinicianId,
            String firstName,
            String lastName,
            String email,
            String speciality,
            String registrationNo,
            String workplaceId
    ) {
        String t = title == null ? "" : title.toLowerCase();

        // GP
        if (t.contains("gp")) {
            return new GeneralPractitioner(clinicianId, firstName, lastName, email, speciality, registrationNo, workplaceId);
        }

        // Nurse
        if (t.contains("nurse")) {
            return new Nurse(clinicianId, firstName, lastName, email, speciality, registrationNo, workplaceId);
        }

        // Consultant / specialist
        return new SpecialistDoctor(clinicianId, firstName, lastName, email, speciality, registrationNo, workplaceId);
    }

    private static String clinicianTitle(Clinician c) {
        if (c instanceof GeneralPractitioner) return "GP";
        if (c instanceof Nurse) return "Nurse";
        if (c instanceof SpecialistDoctor) return "Consultant";
        return "Clinician";
    }

    private static String get(Map<String, String> r, String key) {
        String v = r.get(key);
        return v == null ? "" : v.trim();
    }

    // minimal CSV escaping (handles commas/quotes)
    private static String esc(String s) {
        if (s == null) return "";
        boolean needQuotes = s.contains(",") || s.contains("\"") || s.contains("\n");
        if (!needQuotes) return s;
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }
}
