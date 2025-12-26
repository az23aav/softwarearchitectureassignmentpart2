package uk.ac.healthcare.view;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame(PatientPanel patientPanel, AppointmentPanel appointmentPanel) {
        setTitle("Healthcare System");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Patients", patientPanel);
        tabs.addTab("Appointments", appointmentPanel);

        setContentPane(tabs);
    }
}

