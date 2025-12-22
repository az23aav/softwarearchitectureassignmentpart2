package uk.ac.healthcare.repository;

import uk.ac.healthcare.model.Patient;
import uk.ac.healthcare.util.CsvUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class PatientRepository {

    private final DataStore store;

    public PatientRepository(DataStore store) {
        this.store = store;
    }

    public void load(Path csv) throws IOException {
        List<Map<String, String>> rows = CsvUtils.read(csv);

        for (Map<String, String> r : rows) {
            String id = r.get("patient_id");
            String first = r.get("first_name");
            String last = r.get("last_name");
            String email = r.get("email");
            String nhs = r.get("nhs_number");
            String facilityId = r.get("facility_id");

            Patient p = new Patient(id, first, last, email, nhs, facilityId);
            store.patients.put(id, p);
        }
    }
}

