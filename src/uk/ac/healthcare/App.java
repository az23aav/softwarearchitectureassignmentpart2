package uk.ac.healthcare;

import uk.ac.healthcare.controller.*;
import uk.ac.healthcare.repository.*;
import uk.ac.healthcare.view.*;

import javax.swing.*;
import java.nio.file.Path;

public class App {

    public static void start() {

        DataStore store = new DataStore();
        Path dataDir = Path.of("data");

        // Repositories
        FacilityRepository facilityRepo = new FacilityRepository(store);
        PatientRepository patientRepo = new PatientRepository(store);
        ClinicianRepository clinicianRepo = new ClinicianRepository(store);
        StaffRepository staffRepo = new StaffRepository(store);
        AppointmentRepository appointmentRepo = new AppointmentRepository(store);
        PrescriptionRepository prescriptionRepo = new PrescriptionRepository(store);

        // If you later convert this to Singleton, change it to ReferralRepository.getInstance(store)
        ReferralRepository referralRepo = new ReferralRepository(store);

        // Loader
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

        try {
            loader.loadAll(dataDir);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Failed to load data:\n" + e.getMessage(),
                    "Data Load Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        // Controllers
        PatientController patientController = new PatientController(store, patientRepo, dataDir);
        AppointmentController appointmentController = new AppointmentController(store, appointmentRepo, dataDir);
        ClinicianController clinicianController = new ClinicianController(store, clinicianRepo, dataDir);
        PrescriptionController prescriptionController = new PrescriptionController(store, prescriptionRepo, dataDir);

        SwingUtilities.invokeLater(() -> {
            PatientPanel patientPanel = new PatientPanel(patientController, patientRepo, dataDir);
            AppointmentPanel appointmentPanel = new AppointmentPanel(appointmentController);
            ClinicianPanel clinicianPanel = new ClinicianPanel(clinicianController);
            PrescriptionPanel prescriptionPanel = new PrescriptionPanel(prescriptionController);

            MainFrame frame = new MainFrame(patientPanel, appointmentPanel, clinicianPanel, prescriptionPanel);
            frame.setVisible(true);
        });
    }
}
