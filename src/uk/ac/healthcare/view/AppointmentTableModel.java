package uk.ac.healthcare.view;

import uk.ac.healthcare.model.Appointment;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class AppointmentTableModel extends AbstractTableModel {

    private final String[] cols = {
            "ID", "Patient", "Clinician", "Facility", "Date", "Time",
            "Duration", "Type", "Status", "Reason", "Notes"
    };

    private List<Appointment> data = new ArrayList<>();

    public void setData(List<Appointment> data) {
        this.data = (data == null) ? new ArrayList<>() : data;
        fireTableDataChanged();
    }

    public Appointment getAt(int row) {
        if (row < 0 || row >= data.size()) return null;
        return data.get(row);
    }

    @Override public int getRowCount() { return data.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int c) { return cols[c]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Appointment a = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> a.getAppointmentId();
            case 1 -> a.getPatientId();
            case 2 -> a.getClinicianId();
            case 3 -> a.getFacilityId();
            case 4 -> a.getDate();
            case 5 -> a.getTime();
            case 6 -> a.getDurationMinutes();
            case 7 -> a.getType();
            case 8 -> a.getStatus();
            case 9 -> a.getReason();
            case 10 -> a.getNotes();
            default -> "";
        };
    }
}

