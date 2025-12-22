package uk.ac.healthcare.repository;

import uk.ac.healthcare.model.Facility;
import uk.ac.healthcare.util.CsvUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class FacilityRepository {

    private final DataStore store;

    public FacilityRepository(DataStore store) {
        this.store = store;
    }

    public void load(Path csv) throws IOException {
        List<Map<String, String>> rows = CsvUtils.read(csv);

        for (Map<String, String> r : rows) {
            String id = r.get("facility_id");
            String name = r.get("facility_name");
            String type = r.get("facility_type");
            String address = r.get("address");
            String postcode = r.get("postcode");
            String phone = r.get("phone_number");
            String email = r.get("email");
            String opening = r.get("opening_hours");
            String manager = r.get("manager_name");

            int capacity = r.get("capacity").isBlank()
                    ? 0
                    : Integer.parseInt(r.get("capacity"));

            String specialties = r.get("specialties_offered");

            Facility facility = new Facility(
                    id, name, type, address, postcode,
                    phone, email, opening, manager, capacity, specialties
            );

            store.facilities.put(id, facility);
        }
    }
}
