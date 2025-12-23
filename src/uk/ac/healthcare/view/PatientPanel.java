package uk.ac.healthcare.view;

import uk.ac.healthcare.controller.PatientController;
import uk.ac.healthcare.model.Patient;
import uk.ac.healthcare.repository.PatientRepository;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.nio.file.Path;
import java.util.List;

public class PatientPanel extends JPanel {

    private final PatientController controller;
    private final PatientRepository patientRepo;
    private final Path dataDir;

    private final PatientTableModel tableModel = new PatientTableModel();
    private final JTable table = new JTable(tableModel);

    public PatientPanel(PatientController controller, PatientRepository patientRepo, Path dataDir) {
        this.controller = controller;
        this.patientRepo = patientRepo;
        this.dataDir = dataDir;

        setLayout(new BorderLayout(10, 10));

        // TOP BUTTONS
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Refresh");
        JButton addBtn = new JButton("Add");
        JButton deleteBtn = new JButton("Delete");

        top.add(refreshBtn);
        top.add(addBtn);
        top.add(deleteBtn);
        add(top, BorderLayout.NORTH);

        // TABLE
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // BUTTON ACTIONS

        refreshBtn.addActionListener(e -> refreshTable());

        addBtn.addActionListener(e -> addPatient());

        deleteBtn.addActionListener(e -> deletePatient());

        // Load at startup
        refreshTable();
    }

    // ADD NEW PATIENT
    private void addPatient() {
        String id = JOptionPane.showInputDialog(this, "Enter Patient ID:");
        if (id == null || id.isBlank()) return;

        String first = JOptionPane.showInputDialog(this, "Enter First Name:");
        String last = JOptionPane.showInputDialog(this, "Enter Last Name:");
        String email = JOptionPane.showInputDialog(this, "Enter Email:");
        String nhs = JOptionPane.showInputDialog(this, "Enter NHS Number:");
        String facility = JOptionPane.showInputDialog(this, "Enter GP Surgery ID:");

        Patient p = new Patient(id, first, last, email, nhs, facility);
        controller.add(p);

        saveCSV();
        refreshTable();
    }


    // DELETE PATIENT
    private void deletePatient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a patient first.");
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        controller.delete(id);

        saveCSV();
        refreshTable();
    }


    // SAVE TO CSV
    private void saveCSV() {
        try {
            patientRepo.save(dataDir.resolve("patients.csv"));
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Failed to save CSV:\n" + ex.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // REFRESH TABLE DATA
    private void refreshTable() {
        List<Patient> patients = controller.getAll();
        tableModel.setPatients(patients);
    }

    // TABLE MODEL
    private static class PatientTableModel extends AbstractTableModel {

        private List<Patient> patients = List.of();

        private final String[] columns = {
                "Patient ID",
                "First Name",
                "Last Name",
                "Email",
                "NHS Number",
                "GP Surgery ID"
        };

        public void setPatients(List<Patient> patients) {
            this.patients = patients;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return patients.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int row, int col) {
            Patient p = patients.get(row);
            return switch (col) {
                case 0 -> p.getUserId();
                case 1 -> p.getFirstName();
                case 2 -> p.getLastName();
                case 3 -> p.getEmail();
                case 4 -> p.getNhsNumber();
                case 5 -> p.getRegisteredSurgeryId();
                default -> "";
            };
        }
    }
}
