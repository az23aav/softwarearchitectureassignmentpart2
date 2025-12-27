package uk.ac.healthcare.view;

import uk.ac.healthcare.controller.ReferralController;
import uk.ac.healthcare.model.Referral;
import uk.ac.healthcare.model.ReferralStatus;
import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.time.LocalDate;

public class ReferralPanel extends JPanel {

    private final ReferralController controller;
    private final ReferralTableModel tableModel = new ReferralTableModel();
    private final JTable table = new JTable(tableModel);

    public ReferralPanel(ReferralController controller) {
        this.controller = controller;

        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Refresh");
        JButton addBtn = new JButton("Add");
        JButton deleteBtn = new JButton("Delete");
        JButton saveBtn = new JButton("Save");
        JButton exportBtn = new JButton("Export Email Text");
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
        addBtn.addActionListener(e -> addReferral());
        editBtn.addActionListener(e -> editSelectedReferral());
        deleteBtn.addActionListener(e -> deleteSelected());
        saveBtn.addActionListener(e -> {
            controller.saveNow();
            JOptionPane.showMessageDialog(this, "Saved to referrals.csv");
        });
        exportBtn.addActionListener(e -> exportSelected());

        refresh();
    }

    private void refresh() {
        tableModel.setData(controller.getAll());
    }

    private void editSelectedReferral() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a referral first.");
            return;
        }

        int modelRow = table.convertRowIndexToModel(row);
        Referral existing = tableModel.getAt(modelRow);
        if (existing == null) return;

        // Read-only IDs
        JTextField referralIdField = new JTextField(existing.getReferralId());
        referralIdField.setEnabled(false);

        JTextField patientIdField = new JTextField(existing.getPatientId());
        patientIdField.setEnabled(false);

        JTextField fromClinicianField = new JTextField(existing.getReferringClinicianId());
        fromClinicianField.setEnabled(false);

        JTextField toClinicianField = new JTextField(existing.getReferredToClinicianId());
        toClinicianField.setEnabled(false);

        JTextField fromFacilityField = new JTextField(existing.getReferringFacilityId());
        fromFacilityField.setEnabled(false);

        JTextField toFacilityField = new JTextField(existing.getReferredToFacilityId());
        toFacilityField.setEnabled(false);

        JTextField dateField = new JTextField(existing.getReferralDate() == null ? "" : existing.getReferralDate().toString());
        JTextField urgencyField = new JTextField(existing.getUrgencyLevel());

        JTextField reasonField = new JTextField(existing.getReason());
        JTextArea summaryArea = new JTextArea(existing.getClinicalSummary(), 3, 20);
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);

        JTextField investigationsField = new JTextField(existing.getRequestedInvestigations());
        JTextField appointmentIdField = new JTextField(existing.getAppointmentId());
        JTextArea notesArea = new JTextArea(existing.getNotes(), 2, 20);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);

        JComboBox<ReferralStatus> statusBox = new JComboBox<>(ReferralStatus.values());
        statusBox.setSelectedItem(existing.getStatus());

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));

        form.add(new JLabel("Referral ID:")); form.add(referralIdField);
        form.add(new JLabel("Patient ID:")); form.add(patientIdField);
        form.add(new JLabel("From Clinician:")); form.add(fromClinicianField);
        form.add(new JLabel("To Clinician:")); form.add(toClinicianField);
        form.add(new JLabel("From Facility:")); form.add(fromFacilityField);
        form.add(new JLabel("To Facility:")); form.add(toFacilityField);

        form.add(new JLabel("Referral Date (yyyy-MM-dd):")); form.add(dateField);
        form.add(new JLabel("Urgency Level:")); form.add(urgencyField);
        form.add(new JLabel("Reason:")); form.add(reasonField);

        form.add(new JLabel("Clinical Summary:")); form.add(new JScrollPane(summaryArea));
        form.add(new JLabel("Requested Investigations:")); form.add(investigationsField);

        form.add(new JLabel("Status:")); form.add(statusBox);
        form.add(new JLabel("Appointment ID:")); form.add(appointmentIdField);

        form.add(new JLabel("Notes:")); form.add(new JScrollPane(notesArea));

        int result = JOptionPane.showConfirmDialog(
                this, form, "Edit Referral " + existing.getReferralId(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        try {
            LocalDate newDate = parseDateOrNull(dateField.getText());

            Referral updated = new Referral(
                    existing.getReferralId(),
                    existing.getPatientId(),
                    existing.getReferringClinicianId(),
                    existing.getReferredToClinicianId(),
                    existing.getReferringFacilityId(),
                    existing.getReferredToFacilityId(),
                    newDate,
                    urgencyField.getText().trim(),
                    reasonField.getText().trim(),
                    summaryArea.getText().trim(),
                    investigationsField.getText().trim(),
                    (ReferralStatus) statusBox.getSelectedItem(),
                    appointmentIdField.getText().trim(),
                    notesArea.getText().trim()
            );

            controller.update(updated);
            refresh(); // your existing refresh method

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(),
                    "Edit error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private LocalDate parseDateOrNull(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty()) return null;
        return LocalDate.parse(s);
    }

    private void addReferral() {
        // Minimal dialog inputs (you can expand later)
        String referralId = JOptionPane.showInputDialog(this, "Referral ID (e.g., R999):");
        if (referralId == null || referralId.isBlank()) return;

        String patientId = JOptionPane.showInputDialog(this, "Patient ID (e.g., P001):");
        if (patientId == null || patientId.isBlank()) return;

        String fromClin = JOptionPane.showInputDialog(this, "Referring Clinician ID:");
        if (fromClin == null) return;

        String toClin = JOptionPane.showInputDialog(this, "Referred To Clinician ID:");
        if (toClin == null) return;

        // You can ask for facilities too, or set blank for now
        String fromFac = JOptionPane.showInputDialog(this, "Referring Facility ID:");
        if (fromFac == null) fromFac = "";

        String toFac = JOptionPane.showInputDialog(this, "Referred To Facility ID:");
        if (toFac == null) toFac = "";

        String urgency = JOptionPane.showInputDialog(this, "Urgency Level (Routine/Urgent/etc):");
        if (urgency == null) urgency = "Routine";

        String reason = JOptionPane.showInputDialog(this, "Referral Reason:");
        if (reason == null) reason = "";

        String summary = JOptionPane.showInputDialog(this, "Clinical Summary:");
        if (summary == null) summary = "";

        String investigations = JOptionPane.showInputDialog(this, "Requested Investigations:");
        if (investigations == null) investigations = "";

        String appointmentId = JOptionPane.showInputDialog(this, "Appointment ID (optional):");
        if (appointmentId == null) appointmentId = "";

        String notes = JOptionPane.showInputDialog(this, "Notes (optional):");
        if (notes == null) notes = "";

        // Default date today, status PENDING
        Referral r = new Referral(
                referralId,
                patientId,
                fromClin,
                toClin,
                fromFac,
                toFac,
                LocalDate.now(),
                urgency,
                reason,
                summary,
                investigations,
                uk.ac.healthcare.model.ReferralStatus.PENDING,
                appointmentId,
                notes
        );

        controller.add(r);
        refresh();
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        Referral r = tableModel.getAt(row);
        if (r == null) return;

        int ok = JOptionPane.showConfirmDialog(this,
                "Delete referral " + r.getReferralId() + "?",
                "Confirm",
                JOptionPane.YES_NO_OPTION);

        if (ok == JOptionPane.YES_OPTION) {
            controller.delete(r.getReferralId());
            refresh();
        }
    }

    private void exportSelected() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        Referral r = tableModel.getAt(row);
        if (r == null) return;

        Path out = controller.exportEmailText(r.getReferralId());
        JOptionPane.showMessageDialog(this, "Exported:\n" + out.toString());
    }
}

