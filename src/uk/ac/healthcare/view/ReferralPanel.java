package uk.ac.healthcare.view;

import uk.ac.healthcare.controller.ReferralController;
import uk.ac.healthcare.model.Referral;

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

        top.add(refreshBtn);
        top.add(addBtn);
        top.add(deleteBtn);
        top.add(saveBtn);
        top.add(exportBtn);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> refresh());
        addBtn.addActionListener(e -> addReferral());
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

