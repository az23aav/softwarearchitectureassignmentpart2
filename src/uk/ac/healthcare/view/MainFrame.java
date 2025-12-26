package uk.ac.healthcare.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(JPanel patientPanel,
                     JPanel appointmentPanel,
                     JPanel clinicianPanel,
                     JPanel prescriptionPanel) {

        setTitle("Healthcare System");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Patients", patientPanel);
        tabs.addTab("Appointments", appointmentPanel);
        tabs.addTab("Clinicians", clinicianPanel);
        tabs.addTab("Prescriptions", prescriptionPanel);

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
    }
}

