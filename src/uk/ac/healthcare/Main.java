package uk.ac.healthcare;

import uk.ac.healthcare.repository.*;

import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {

        try {
            //create datastore
            DataStore store = new DataStore();

            //create repositories
            FacilityRepository facilityRepo = new FacilityRepository(store);
            PatientRepository patientRepo = new PatientRepository(store);
            ClinicianRepository clinicianRepo = new ClinicianRepository(store);
            StaffRepository staffRepo = new StaffRepository(store);
            AppointmentRepository appointmentRepo = new AppointmentRepository(store);
            PrescriptionRepository prescriptionRepo = new PrescriptionRepository(store);
            ReferralRepository referralRepo = new ReferralRepository(store);

            //Create data loader
            DataLoader loader = new DataLoader(
                    store,
                    facilityRepo,
                    patientRepo,
                    clinicianRepo,
                    staffRepo,
                    appointmentRepo,
                    prescriptionRepo,
                    referralRepo
            );

            // Load all CSV files
            Path dataDir = Path.of("data");
            loader.loadAll(dataDir);

            //Confirm everything loaded
            System.out.println("Data loaded successfully:");
            System.out.println("Facilities: " + store.facilities.size());
            System.out.println("Patients: " + store.patients.size());
            System.out.println("Clinicians: " + store.clinicians.size());
            System.out.println("Staff: " + store.staff.size());
            System.out.println("Appointments: " + store.appointments.size());
            System.out.println("Prescriptions: " + store.prescriptions.size());
            System.out.println("Referrals: " + store.referrals.size());

        } catch (Exception e) {
            System.err.println("Failed to load data");
            e.printStackTrace();
        }
    }
}

