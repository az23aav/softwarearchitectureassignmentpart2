package uk.ac.healthcare.view;

import uk.ac.healthcare.controller.ClinicianController;
import uk.ac.healthcare.model.*;

import javax.swing.*;
import java.awt.*;

public class ClinicianPanel extends JPanel {

    private final ClinicianController controller;
    private final ClinicianTableModel tableModel = new ClinicianTableModel();
    private final JTable table = new JTable(tableModel);

    public ClinicianPanel(ClinicianController controller) {
        this.controller = controller;

        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Refresh");
        JButton addBtn = new JButton("Add");
        JButton deleteBtn = new JButton("Delete");
        JButton saveBtn = new JButton("Save");

        top.add(refreshBtn);
        top.add(addBtn);
        top.add(deleteBtn);
        top.add(saveBtn);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> refresh());
        addBtn.addActionListener(e -> addClinician());
        deleteBtn.addActionListener(e -> deleteSelected());
        saveBtn.addActionListener(e -> save());

        refresh();
    }

    private void refresh() {
        tableModel.setData(controller.getAll());
    }

    private void addClinician() {
        JTextField id = new JTextField();
        JTextField first = new JTextField();
        JTextField last = new JTextField();
        JTextField email = new JTextField();
        JTextField speciality = new JTextField();
        JTextField regNo = new JTextField();
        JTextField workplaceId = new JTextField();

        String[] types = {"GP", "Nurse", "Consultant"};
        JComboBox<String> typeBox = new JComboBox<>(types);

        JPanel p = new JPanel(new GridLayout(0, 2, 8, 8));
        p.add(new JLabel("Type")); p.add(typeBox);
        p.add(new JLabel("clinician_id")); p.add(id);
        p.add(new JLabel("first_name")); p.add(first);
        p.add(new JLabel("last_name")); p.add(last);
        p.add(new JLabel("email")); p.add(email);
        p.add(new JLabel("speciality")); p.add(speciality);
        p.add(new JLabel("registration_no")); p.add(regNo);
        p.add(new JLabel("workplace_id")); p.add(workplaceId);

        int ok = JOptionPane.showConfirmDialog(this, p, "Add Clinician",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (ok != JOptionPane.OK_OPTION) return;

        String chosen = (String) typeBox.getSelectedItem();
        Clinician c;
        if ("GP".equals(chosen)) {
            c = new GeneralPractitioner(id.getText().trim(), first.getText().trim(), last.getText().trim(),
                    email.getText().trim(), speciality.getText().trim(), regNo.getText().trim(), workplaceId.getText().trim());
        } else if ("Nurse".equals(chosen)) {
            c = new Nurse(id.getText().trim(), first.getText().trim(), last.getText().trim(),
                    email.getText().trim(), speciality.getText().trim(), regNo.getText().trim(), workplaceId.getText().trim());
        } else {
            c = new SpecialistDoctor(id.getText().trim(), first.getText().trim(), last.getText().trim(),
                    email.getText().trim(), speciality.getText().trim(), regNo.getText().trim(), workplaceId.getText().trim());
        }

        controller.add(c);
        refresh();
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        Clinician c = tableModel.getAt(row);
        if (c == null) return;

        int ok = JOptionPane.showConfirmDialog(this,
                "Delete clinician " + c.getClinicianId() + "?",
                "Confirm", JOptionPane.OK_CANCEL_OPTION);

        if (ok != JOptionPane.OK_OPTION) return;

        controller.delete(c.getClinicianId());
        refresh();
    }

    private void save() {
        try {
            controller.saveNow();
            JOptionPane.showMessageDialog(this, "clinicians.csv saved!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Save error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
