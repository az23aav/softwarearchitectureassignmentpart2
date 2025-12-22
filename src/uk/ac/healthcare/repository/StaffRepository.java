package uk.ac.healthcare.repository;

import uk.ac.healthcare.model.AdminStaff;
import uk.ac.healthcare.util.CsvUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class StaffRepository {

    private final DataStore store;

    public StaffRepository(DataStore store) {
        this.store = store;
    }

    public void load(Path csv) throws IOException {
        List<Map<String, String>> rows = CsvUtils.read(csv);

        for (Map<String, String> r : rows) {

            String staffId = get(r, "staff_id");
            if (staffId.isBlank()) continue;

            String firstName = get(r, "first_name");
            String lastName = get(r, "last_name");
            String role = get(r, "role");
            String department = get(r, "department");
            String facilityId = get(r, "facility_id");
            String phone = get(r, "phone_number");
            String email = get(r, "email");
            String employmentStatus = get(r, "employment_status");
            String startDateRaw = get(r, "start_date");
            String lineManager = get(r, "line_manager");
            String accessLevel = get(r, "access_level");

            LocalDate startDate = parseDateOrNull(startDateRaw);

            AdminStaff staff = new AdminStaff(
                    staffId,
                    firstName,
                    lastName,
                    email,
                    phone,
                    role,
                    department,
                    facilityId,
                    employmentStatus,
                    startDate,
                    lineManager,
                    accessLevel
            );

            store.staff.put(staffId, staff);
        }
    }

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
}
