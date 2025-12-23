package uk.ac.healthcare.view;

import uk.ac.healthcare.controller.PatientController;
import uk.ac.healthcare.model.Patient;

import javax.swing.*;
import java.awt.*;

public class PatientPanel extends JPanel {

    private final PatientController controller;
    private final PatientTableModel tableModel = new PatientTableModel();
    private final JTable table = new JTable(tableModel);

    public PatientPanel(PatientController controller) {
        this.controller = controller;

        setLayout(new BorderLayout(10, 10));

        // Top buttons
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Refresh");
        JButton addBtn = new JButton("Add");
        JButton deleteBtn = new JButton("Delete");

        top.add(refreshBtn);
        top.add(addBtn);
        top.add(deleteBtn);

        add(top, BorderLayout.NORTH);

        // Table
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Wire actions
        refreshBtn.addActionListener(e -> refresh());
        addBtn.addActionListener(e -> addPatientDialog());
        deleteBtn.addActionListener(e -> deleteSelected());

        // Initial load
        refresh();
    }

    private void refresh() {
        tableModel.setPatients(controller.getAll());
    }

    private void addPatientDialog() {
        JTextField id = new JTextField();
        JTextField first = new JTextField();
        JTextField last = new JTextField();
        JTextField email = new JTextField();
        JTextField nhs = new JTextField();
        JTextField surgery = new JTextField();

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Patient ID:")); form.add(id);
        form.add(new JLabel("First name:")); form.add(first);
        form.add(new JLabel("Last name:")); form.add(last);
        form.add(new JLabel("Email:")); form.add(email);
        form.add(new JLabel("NHS number:")); form.add(nhs);
        form.add(new JLabel("Registered surgery ID:")); form.add(surgery);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Add Patient",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        // basic validation
        if (id.getText().isBlank() || first.getText().isBlank() || last.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Patient ID, first name, last name are required.");
            return;
        }

        Patient p = new Patient(
                id.getText().trim(),
                first.getText().trim(),
                last.getText().trim(),
                email.getText().trim(),
                nhs.getText().trim(),
                surgery.getText().trim()
        );

        controller.add(p);
        refresh();
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a patient row first.");
            return;
        }

        Patient p = tableModel.getPatientAt(row);
        if (p == null) return;

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete patient " + p.getUserId() + "?",
                "Confirm delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        controller.delete(p.getUserId());
        refresh();
    }
}

