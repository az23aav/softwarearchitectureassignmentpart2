package uk.ac.healthcare.repository;

import uk.ac.healthcare.model.*;
import uk.ac.healthcare.util.CsvUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class ClinicianRepository {

    private final DataStore store;

    public ClinicianRepository(DataStore store) {
        this.store = store;
    }

    public void load(Path csv) throws IOException {
        List<Map<String, String>> rows = CsvUtils.read(csv);

        for (Map<String, String> r : rows) {

            String clinicianId = get(r, "clinician_id");
            String firstName = get(r, "first_name");
            String lastName = get(r, "last_name");
            String title = get(r, "title");                 // GP / Consultant / Nurse
            String speciality = get(r, "speciality");
            String registrationNo = get(r, "gmc_number");   // GMC / NMC
            String email = get(r, "email");
            String workplaceId = get(r, "workplace_id");

            Clinician clinician;

            switch (title.toLowerCase()) {
                case "gp":
                    clinician = new GeneralPractitioner(
                            clinicianId,
                            firstName,
                            lastName,
                            email,
                            speciality,
                            registrationNo,
                            workplaceId
                    );
                    break;

                case "consultant":
                    clinician = new SpecialistDoctor(
                            clinicianId,
                            firstName,
                            lastName,
                            email,
                            speciality,
                            registrationNo,
                            workplaceId
                    );
                    break;

                default:
                    clinician = new Nurse(
                            clinicianId,
                            firstName,
                            lastName,
                            email,
                            speciality,
                            registrationNo,
                            workplaceId
                    );
                    break;
            }

            store.clinicians.put(clinicianId, clinician);
        }
    }

    private static String get(Map<String, String> r, String key) {
        String v = r.get(key);
        return v == null ? "" : v.trim();
    }
}
