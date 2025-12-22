package uk.ac.healthcare.repository;

import java.io.IOException;
import java.nio.file.Path;

public class DataLoader {

    private final DataStore store;

    private final FacilityRepository facilityRepo;
    private final PatientRepository patientRepo;
    private final ClinicianRepository clinicianRepo;
    private final StaffRepository staffRepo;
    private final AppointmentRepository appointmentRepo;
    private final PrescriptionRepository prescriptionRepo;
    private final ReferralRepository referralRepo;

    public DataLoader(DataStore store,
                      FacilityRepository facilityRepo,
                      PatientRepository patientRepo,
                      ClinicianRepository clinicianRepo,
                      StaffRepository staffRepo,
                      AppointmentRepository appointmentRepo,
                      PrescriptionRepository prescriptionRepo,
                      ReferralRepository referralRepo) {
        this.store = store;
        this.facilityRepo = facilityRepo;
        this.patientRepo = patientRepo;
        this.clinicianRepo = clinicianRepo;
        this.staffRepo = staffRepo;
        this.appointmentRepo = appointmentRepo;
        this.prescriptionRepo = prescriptionRepo;
        this.referralRepo = referralRepo;
    }

    public void loadAll(Path dataDir) throws IOException {
        facilityRepo.load(dataDir.resolve("facilities.csv"));
        patientRepo.load(dataDir.resolve("patients.csv"));
        clinicianRepo.load(dataDir.resolve("clinicians.csv"));
        staffRepo.load(dataDir.resolve("staff.csv"));
        appointmentRepo.load(dataDir.resolve("appointments.csv"));
        prescriptionRepo.load(dataDir.resolve("prescriptions.csv"));
        referralRepo.load(dataDir.resolve("referrals.csv"));
    }
}

