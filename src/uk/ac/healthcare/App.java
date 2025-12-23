package uk.ac.healthcare;

import uk.ac.healthcare.controller.PatientController;
import uk.ac.healthcare.repository.*;
import uk.ac.healthcare.view.MainFrame;

import javax.swing.*;
import java.nio.file.Path;

public class App {

    public static void start() {
        // Model store
        DataStore store = new DataStore();
        Path dataDir = Path.of("data");

        // Repositories
        FacilityRepository facilityRepo = new FacilityRepository(store);
        PatientRepository patientRepo = new PatientRepository(store);
        ClinicianRepository clinicianRepo = new ClinicianRepository(store);
        StaffRepository staffRepo = new StaffRepository(store);
        AppointmentRepository appointmentRepo = new AppointmentRepository(store);
        PrescriptionRepository prescriptionRepo = new PrescriptionRepository(store);
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

        // Load CSVs from /data folder (project root)
        try {
            loader.loadAll(Path.of("data"));
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

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(patientController, patientRepo, dataDir);
            frame.setVisible(true);
        });
    }
}
