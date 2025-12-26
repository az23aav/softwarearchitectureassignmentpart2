package uk.ac.healthcare.view;

import uk.ac.healthcare.controller.AppointmentController;
import uk.ac.healthcare.model.Appointment;
import uk.ac.healthcare.model.AppointmentStatus;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentPanel extends JPanel {

    private final AppointmentController controller;

    private final AppointmentTableModel tableModel = new AppointmentTableModel();
    private final JTable table = new JTable(tableModel);

    public AppointmentPanel(AppointmentController controller) {
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
        addBtn.addActionListener(e -> addAppointment());
        deleteBtn.addActionListener(e -> deleteSelected());
        saveBtn.addActionListener(e -> {
            controller.saveNow();
            JOptionPane.showMessageDialog(this, "appointments.csv saved!");
        });

        refresh();
    }

    private void refresh() {
        tableModel.setData(controller.getAll());
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an appointment first.");
            return;
        }
        Appointment a = tableModel.getAt(row);
        if (a == null) return;

        int ok = JOptionPane.showConfirmDialog(this,
                "Delete appointment " + a.getAppointmentId() + "?",
                "Confirm delete",
                JOptionPane.YES_NO_OPTION);

        if (ok == JOptionPane.YES_OPTION) {
            controller.delete(a.getAppointmentId());
            refresh();
        }
    }

    private void addAppointment() {
        JTextField id = new JTextField();
        JTextField patientId = new JTextField();
        JTextField clinicianId = new JTextField();
        JTextField facilityId = new JTextField();
        JTextField date = new JTextField("2025-09-20");   // yyyy-MM-dd
        JTextField time = new JTextField("09:00");        // HH:mm
        JTextField duration = new JTextField("15");
        JTextField type = new JTextField("Routine Consultation");
        JComboBox<AppointmentStatus> status = new JComboBox<>(AppointmentStatus.values());
        JTextField reason = new JTextField();
        JTextField notes = new JTextField();

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("appointment_id")); form.add(id);
        form.add(new JLabel("patient_id")); form.add(patientId);
        form.add(new JLabel("clinician_id")); form.add(clinicianId);
        form.add(new JLabel("facility_id")); form.add(facilityId);
        form.add(new JLabel("appointment_date (yyyy-MM-dd)")); form.add(date);
        form.add(new JLabel("appointment_time (HH:mm)")); form.add(time);
        form.add(new JLabel("duration_minutes")); form.add(duration);
        form.add(new JLabel("appointment_type")); form.add(type);
        form.add(new JLabel("status")); form.add(status);
        form.add(new JLabel("reason_for_visit")); form.add(reason);
        form.add(new JLabel("notes")); form.add(notes);

        int ok = JOptionPane.showConfirmDialog(this, form, "Add Appointment", JOptionPane.OK_CANCEL_OPTION);
        if (ok != JOptionPane.OK_OPTION) return;

        try {
            Appointment a = new Appointment(
                    id.getText().trim(),
                    patientId.getText().trim(),
                    clinicianId.getText().trim(),
                    facilityId.getText().trim(),
                    LocalDate.parse(date.getText().trim()),
                    LocalTime.parse(time.getText().trim()),
                    Integer.parseInt(duration.getText().trim()),
                    type.getText().trim(),
                    (AppointmentStatus) status.getSelectedItem(),
                    reason.getText().trim(),
                    notes.getText().trim()
            );

            controller.add(a);
            refresh();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid input: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
