package uk.ac.healthcare.repository;

import uk.ac.healthcare.model.Referral;
import uk.ac.healthcare.model.ReferralStatus;
import uk.ac.healthcare.util.CsvUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ReferralRepository {

    private final DataStore store;

    public ReferralRepository(DataStore store) {
        this.store = store;
    }

    public void load(Path csv) throws IOException {
        List<Map<String, String>> rows = CsvUtils.read(csv);

        for (Map<String, String> r : rows) {
            String referralId = get(r, "referral_id");
            String patientId = get(r, "patient_id");

            String referringClinicianId = get(r, "referring_clinician_id");
            String referredToClinicianId = get(r, "referred_to_clinician_id");

            String referringFacilityId = get(r, "referring_facility_id");
            String referredToFacilityId = get(r, "referred_to_facility_id");

            LocalDate referralDate = parseDate(get(r, "referral_date"));

            String urgencyLevel = get(r, "urgency_level");
            String reason = get(r, "referral_reason");
            String clinicalSummary = get(r, "clinical_summary");
            String requestedInvestigations = get(r, "requested_investigations");

            ReferralStatus status = parseReferralStatus(get(r, "status"));

            String appointmentId = get(r, "appointment_id");
            String notes = get(r, "notes");

            Referral referral = new Referral(
                    referralId,
                    patientId,
                    referringClinicianId,
                    referredToClinicianId,
                    referringFacilityId,
                    referredToFacilityId,
                    referralDate,
                    urgencyLevel,
                    reason,
                    clinicalSummary,
                    requestedInvestigations,
                    status,
                    appointmentId,
                    notes
            );

            store.referrals.put(referralId, referral);
        }
    }

    private static String get(Map<String, String> r, String key) {
        String v = r.get(key);
        return v == null ? "" : v.trim();
    }

    private static LocalDate parseDate(String raw) {
        if (raw == null || raw.isBlank()) return null;
        return LocalDate.parse(raw.trim()); // yyyy-MM-dd
    }

    private static ReferralStatus parseReferralStatus(String raw) {
        if (raw == null || raw.isBlank()) return ReferralStatus.PENDING;
        String normalized = raw.trim().toUpperCase().replace(" ", "_");
        try {
            return ReferralStatus.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            return ReferralStatus.PENDING;
        }
    }
}
