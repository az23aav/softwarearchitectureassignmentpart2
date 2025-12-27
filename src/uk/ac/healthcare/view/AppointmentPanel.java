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
        JButton editBtn = new JButton("Edit");

        top.add(refreshBtn);
        top.add(addBtn);
        top.add(editBtn);
        top.add(deleteBtn);
        top.add(saveBtn);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> refresh());
        addBtn.addActionListener(e -> addAppointment());
        editBtn.addActionListener(e -> editSelectedAppointment());
        deleteBtn.addActionListener(e -> deleteSelected());
        saveBtn.addActionListener(e -> {
            controller.saveNow();
            JOptionPane.showMessageDialog(this, "appointments.csv saved!");
        });

        refresh();
    }

    private void editSelectedAppointment() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Select an appointment first.");
            return;
        }

        int modelRow = table.convertRowIndexToModel(viewRow);
        Appointment existing = tableModel.getAt(modelRow);
        if (existing == null) return;

        JTextField dateField = new JTextField(existing.getDate() == null ? "" : existing.getDate().toString());   // yyyy-MM-dd
        JTextField timeField = new JTextField(existing.getTime() == null ? "" : existing.getTime().toString());   // HH:mm
        JTextField durationField = new JTextField(String.valueOf(existing.getDurationMinutes()));
        JTextField typeField = new JTextField(existing.getType() == null ? "" : existing.getType());
        JTextField reasonField = new JTextField(existing.getReason() == null ? "" : existing.getReason());

        JTextArea notesArea = new JTextArea(existing.getNotes() == null ? "" : existing.getNotes(), 4, 20);
        JScrollPane notesScroll = new JScrollPane(notesArea);

        JComboBox<AppointmentStatus> statusBox = new JComboBox<>(AppointmentStatus.values());
        statusBox.setSelectedItem(existing.getStatus());

        JTextField idField = new JTextField(existing.getAppointmentId());
        idField.setEditable(false);

        JTextField patientIdField = new JTextField(existing.getPatientId());
        patientIdField.setEditable(false);

        JTextField clinicianIdField = new JTextField(existing.getClinicianId());
        clinicianIdField.setEditable(false);

        JTextField facilityIdField = new JTextField(existing.getFacilityId());
        facilityIdField.setEditable(false);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Appointment ID:")); form.add(idField);
        form.add(new JLabel("Patient ID:"));      form.add(patientIdField);
        form.add(new JLabel("Clinician ID:"));    form.add(clinicianIdField);
        form.add(new JLabel("Facility ID:"));     form.add(facilityIdField);

        form.add(new JLabel("Date (yyyy-MM-dd):")); form.add(dateField);
        form.add(new JLabel("Time (HH:mm):"));      form.add(timeField);
        form.add(new JLabel("Duration (minutes):"));//
        form.add(durationField);

        form.add(new JLabel("Type:"));   form.add(typeField);
        form.add(new JLabel("Status:")); form.add(statusBox);
        form.add(new JLabel("Reason:")); form.add(reasonField);

        form.add(new JLabel("Notes:"));
        form.add(notesScroll);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Edit Appointment " + existing.getAppointmentId(),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        try {
            LocalDate newDate = dateField.getText().trim().isEmpty()
                    ? null
                    : LocalDate.parse(dateField.getText().trim());

            LocalTime newTime = timeField.getText().trim().isEmpty()
                    ? null
                    : LocalTime.parse(timeField.getText().trim());

            int newDuration = Integer.parseInt(durationField.getText().trim());

            String newType = typeField.getText().trim();
            AppointmentStatus newStatus = (AppointmentStatus) statusBox.getSelectedItem();
            String newReason = reasonField.getText().trim();
            String newNotes = notesArea.getText().trim();

            Appointment updated = new Appointment(
                    existing.getAppointmentId(),
                    existing.getPatientId(),
                    existing.getClinicianId(),
                    existing.getFacilityId(),
                    newDate,
                    newTime,
                    newDuration,
                    newType,
                    newStatus,
                    newReason,
                    newNotes
            );

            controller.update(updated);
            refresh(); // or refresh()

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid input: " + ex.getMessage(),
                    "Edit Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
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
