package uk.ac.healthcare.repository;

import uk.ac.healthcare.model.Patient;
import uk.ac.healthcare.util.CsvUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class PatientRepository {

    private final DataStore store;

    public PatientRepository(DataStore store) {
        this.store = store;
    }

    public void load(Path csv) throws IOException {
        List<Map<String, String>> rows = CsvUtils.read(csv);

        for (Map<String, String> r : rows) {
            String id = get(r, "patient_id");
            if (id.isBlank()) continue;

            String first = get(r, "first_name");
            String last = get(r, "last_name");
            String email = get(r, "email");
            String nhs = get(r, "nhs_number");
            String surgeryId = get(r, "gp_surgery_id"); // IMPORTANT: correct column

            Patient p = new Patient(id, first, last, email, nhs, surgeryId);
            store.patients.put(id, p);
        }
    }

    // Writes the whole file again (simplest + safest)
    public void save(Path csv) throws IOException {
        List<String> lines = new ArrayList<>();

        // Keep EXACT header order from the provided file
        lines.add(String.join(",",
                "patient_id",
                "first_name",
                "last_name",
                "date_of_birth",
                "nhs_number",
                "gender",
                "phone_number",
                "email",
                "address",
                "postcode",
                "emergency_contact_name",
                "emergency_contact_phone",
                "registration_date",
                "gp_surgery_id"
        ));

        for (Patient p : store.patients.values()) {
            String patientId = escape(p.getUserId());
            String firstName = escape(p.getFirstName());
            String lastName = escape(p.getLastName());
            String email = escape(p.getEmail());
            String nhs = escape(p.getNhsNumber());
            String surgeryId = escape(p.getRegisteredSurgeryId());

            // Fields you don't store yet -> write empty values
            lines.add(String.join(",",
                    patientId,
                    firstName,
                    lastName,
                    "",         // date_of_birth
                    nhs,
                    "",         // gender
                    "",         // phone_number
                    email,
                    "",         // address
                    "",         // postcode
                    "",         // emergency_contact_name
                    "",         // emergency_contact_phone
                    "",         // registration_date
                    surgeryId
            ));
        }

        Files.write(csv, lines);
    }

    private static String get(Map<String, String> r, String key) {
        String v = r.get(key);
        return v == null ? "" : v.trim();
    }

    // basic CSV escaping (handles commas + quotes)
    private static String escape(String s) {
        if (s == null) return "";
        String v = s.trim();
        if (v.contains(",") || v.contains("\"") || v.contains("\n")) {
            v = v.replace("\"", "\"\"");
            return "\"" + v + "\"";
        }
        return v;
    }
}


