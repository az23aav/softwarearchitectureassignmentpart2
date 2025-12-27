package uk.ac.healthcare.view;

import uk.ac.healthcare.controller.PrescriptionController;
import uk.ac.healthcare.model.Prescription;
import uk.ac.healthcare.model.PrescriptionStatus;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class PrescriptionPanel extends JPanel {

    private final PrescriptionController controller;

    private final PrescriptionTableModel tableModel = new PrescriptionTableModel();
    private final JTable table = new JTable(tableModel);

    public PrescriptionPanel(PrescriptionController controller) {
        this.controller = controller;

        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Refresh");
        JButton addBtn = new JButton("Add");
        JButton deleteBtn = new JButton("Delete");
        JButton saveBtn = new JButton("Save");
        JButton exportBtn = new JButton("Export Text");
        JButton editBtn = new JButton("Edit");

        top.add(refreshBtn);
        top.add(addBtn);
        top.add(editBtn);
        top.add(deleteBtn);
        top.add(saveBtn);
        top.add(exportBtn);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> refresh());
        addBtn.addActionListener(e -> addPrescriptionDialog());
        editBtn.addActionListener(e -> editSelectedPrescription());
        deleteBtn.addActionListener(e -> deleteSelected());
        saveBtn.addActionListener(e -> {
            try {
                controller.saveNow();
                JOptionPane.showMessageDialog(this, "Saved prescriptions.csv");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Save error", JOptionPane.ERROR_MESSAGE);
            }
        });
        exportBtn.addActionListener(e -> exportSelected());

        refresh();
    }

    private void editSelectedPrescription() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a prescription first.");
            return;
        }

        Prescription existing = tableModel.getAt(row);
        if (existing == null) return;

        JTextField patientId = new JTextField(existing.getPatientId());
        JTextField clinicianId = new JTextField(existing.getClinicianId());
        JTextField appointmentId = new JTextField(existing.getAppointmentId());

        JTextField prescriptionDate = new JTextField(
                existing.getPrescriptionDate() == null ? "" : existing.getPrescriptionDate().toString()
        );

        JTextField drug = new JTextField(existing.getDrug());
        JTextField condition = new JTextField(existing.getCondition());

        JTextField dosage = new JTextField(existing.getDosage());
        JTextField frequency = new JTextField(existing.getFrequency());
        JTextField durationDays = new JTextField(existing.getDurationDays());
        JTextField quantity = new JTextField(existing.getQuantity());

        JTextField instructions = new JTextField(existing.getInstructions());
        JTextField pharmacyName = new JTextField(existing.getPharmacyName());

        JComboBox<PrescriptionStatus> status = new JComboBox<>(PrescriptionStatus.values());
        status.setSelectedItem(existing.getStatus());

        JTextField issueDate = new JTextField(
                existing.getIssueDate() == null ? "" : existing.getIssueDate().toString()
        );

        JTextField collectionDate = new JTextField(
                existing.getCollectionDate() == null ? "" : existing.getCollectionDate().toString()
        );

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));

        form.add(new JLabel("Prescription ID:"));
        JTextField idField = new JTextField(existing.getPrescriptionId());
        idField.setEnabled(false);
        form.add(idField);

        form.add(new JLabel("Patient ID:")); form.add(patientId);
        form.add(new JLabel("Clinician ID:")); form.add(clinicianId);
        form.add(new JLabel("Appointment ID:")); form.add(appointmentId);

        form.add(new JLabel("Prescription Date (yyyy-MM-dd):")); form.add(prescriptionDate);
        form.add(new JLabel("Drug:")); form.add(drug);
        form.add(new JLabel("Condition:")); form.add(condition);

        form.add(new JLabel("Dosage:")); form.add(dosage);
        form.add(new JLabel("Frequency:")); form.add(frequency);
        form.add(new JLabel("Duration Days:")); form.add(durationDays);
        form.add(new JLabel("Quantity:")); form.add(quantity);

        form.add(new JLabel("Instructions:")); form.add(instructions);
        form.add(new JLabel("Pharmacy:")); form.add(pharmacyName);

        form.add(new JLabel("Status:")); form.add(status);
        form.add(new JLabel("Issue Date (yyyy-MM-dd):")); form.add(issueDate);
        form.add(new JLabel("Collection Date (yyyy-MM-dd):")); form.add(collectionDate);

        int result = JOptionPane.showConfirmDialog(
                this, form, "Edit Prescription " + existing.getPrescriptionId(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        try {
            LocalDate presDate = parseDateOrNull(prescriptionDate.getText());
            LocalDate issDate = parseDateOrNull(issueDate.getText());
            LocalDate colDate = parseDateOrNull(collectionDate.getText());

            Prescription updated = new Prescription(
                    existing.getPrescriptionId(),
                    patientId.getText().trim(),
                    clinicianId.getText().trim(),
                    appointmentId.getText().trim(),
                    presDate,
                    drug.getText().trim(),
                    condition.getText().trim(),
                    dosage.getText().trim(),
                    frequency.getText().trim(),
                    durationDays.getText().trim(),
                    quantity.getText().trim(),
                    instructions.getText().trim(),
                    pharmacyName.getText().trim(),
                    (PrescriptionStatus) status.getSelectedItem(),
                    issDate,
                    colDate
            );

            controller.update(updated);
            refresh(); // whatever you already use
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
        }
    }

    private LocalDate parseDateOrNull(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty()) return null;
        return LocalDate.parse(s);
    }

    private void exportSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a prescription first.");
            return;
        }

        int modelRow = table.convertRowIndexToModel(row);

        String prescriptionId = (String) tableModel.getValueAt(modelRow, 0);

        try {
            java.nio.file.Path file = controller.exportToText(prescriptionId);
            JOptionPane.showMessageDialog(this, "Saved:\n" + file.toAbsolutePath());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Export failed:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void refresh() {
        tableModel.setData(controller.getAll());
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a prescription first.");
            return;
        }

        Prescription p = tableModel.getAt(row);

        int ok = JOptionPane.showConfirmDialog(
                this,
                "Delete prescription " + p.getPrescriptionId() + "?",
                "Confirm delete",
                JOptionPane.YES_NO_OPTION
        );

        if (ok == JOptionPane.YES_OPTION) {
            controller.delete(p.getPrescriptionId());
            refresh();
        }
    }

    private void addPrescriptionDialog() {
        JTextField prescriptionId = new JTextField();
        JTextField patientId = new JTextField();
        JTextField clinicianId = new JTextField();
        JTextField appointmentId = new JTextField();

        JTextField prescriptionDate = new JTextField(LocalDate.now().toString()); // yyyy-MM-dd
        JTextField medicationName = new JTextField();
        JTextField condition = new JTextField("UNKNOWN");

        JTextField dosage = new JTextField();
        JTextField frequency = new JTextField();
        JTextField durationDays = new JTextField();  // string, can be "28"
        JTextField quantity = new JTextField();      // string, can be "28 tablets"
        JTextField instructions = new JTextField();
        JTextField pharmacyName = new JTextField();

        JComboBox<PrescriptionStatus> status = new JComboBox<>(PrescriptionStatus.values());
        JTextField issueDate = new JTextField("");        // yyyy-MM-dd or blank
        JTextField collectionDate = new JTextField("");   // yyyy-MM-dd or blank

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("prescription_id")); form.add(prescriptionId);
        form.add(new JLabel("patient_id")); form.add(patientId);
        form.add(new JLabel("clinician_id")); form.add(clinicianId);
        form.add(new JLabel("appointment_id")); form.add(appointmentId);

        form.add(new JLabel("prescription_date (yyyy-MM-dd)")); form.add(prescriptionDate);
        form.add(new JLabel("medication_name")); form.add(medicationName);
        form.add(new JLabel("condition")); form.add(condition);

        form.add(new JLabel("dosage")); form.add(dosage);
        form.add(new JLabel("frequency")); form.add(frequency);
        form.add(new JLabel("duration_days")); form.add(durationDays);
        form.add(new JLabel("quantity")); form.add(quantity);
        form.add(new JLabel("instructions")); form.add(instructions);
        form.add(new JLabel("pharmacy_name")); form.add(pharmacyName);

        form.add(new JLabel("status")); form.add(status);
        form.add(new JLabel("issue_date (yyyy-MM-dd, optional)")); form.add(issueDate);
        form.add(new JLabel("collection_date (yyyy-MM-dd, optional)")); form.add(collectionDate);

        int ok = JOptionPane.showConfirmDialog(
                this, form, "Add Prescription", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );
        if (ok != JOptionPane.OK_OPTION) return;

        // Basic validation
        if (prescriptionId.getText().isBlank() || patientId.getText().isBlank() || clinicianId.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "prescription_id, patient_id, clinician_id are required.");
            return;
        }

        try {
            LocalDate prescDate = LocalDate.parse(prescriptionDate.getText().trim());
            LocalDate issDate = parseOptionalDate(issueDate.getText());
            LocalDate collDate = parseOptionalDate(collectionDate.getText());

            Prescription p = new Prescription(
                    prescriptionId.getText().trim(),
                    patientId.getText().trim(),
                    clinicianId.getText().trim(),
                    appointmentId.getText().trim(),
                    prescDate,
                    medicationName.getText().trim(),
                    condition.getText().trim(),
                    dosage.getText().trim(),
                    frequency.getText().trim(),
                    durationDays.getText().trim(),
                    quantity.getText().trim(),
                    instructions.getText().trim(),
                    pharmacyName.getText().trim(),
                    (PrescriptionStatus) status.getSelectedItem(),
                    issDate,
                    collDate
            );

            controller.add(p);
            refresh();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid input: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static LocalDate parseOptionalDate(String text) {
        if (text == null) return null;
        String t = text.trim();
        if (t.isEmpty()) return null;
        return LocalDate.parse(t);
    }
}
