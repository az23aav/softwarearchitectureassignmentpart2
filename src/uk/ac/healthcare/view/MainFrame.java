package uk.ac.healthcare.view;

import uk.ac.healthcare.controller.PatientController;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(PatientController patientController) {
        super("Healthcare System");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Patients", new PatientPanel(patientController));

        // Next tabs later:
        // tabs.addTab("Clinicians", ...);
        // tabs.addTab("Appointments", ...);
        // tabs.addTab("Prescriptions", ...);
        // tabs.addTab("Referrals", ...);

        add(tabs, BorderLayout.CENTER);
    }
}
