package uk.ac.healthcare.view;

import uk.ac.healthcare.controller.PatientController;
import uk.ac.healthcare.repository.PatientRepository;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public class MainFrame extends JFrame {
    public MainFrame(PatientController patientController,
                     PatientRepository patientRepo,
                     Path dataDir) {

        setTitle("Healthcare System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(new PatientPanel(patientController, patientRepo, dataDir));
    }
}
