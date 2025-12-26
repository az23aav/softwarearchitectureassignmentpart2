package uk.ac.healthcare.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(JPanel patientPanel, JPanel appointmentPanel, JPanel clinicianPanel) {

        setTitle("Healthcare System");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Patients", patientPanel);
        tabs.addTab("Appointments", appointmentPanel);
        tabs.addTab("Clinicians", clinicianPanel);

        setLayout(new BorderLayout());
        setContentPane(tabs);
    }
}
