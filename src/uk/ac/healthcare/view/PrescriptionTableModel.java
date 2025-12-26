package uk.ac.healthcare.view;

import uk.ac.healthcare.model.Prescription;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionTableModel extends AbstractTableModel {

    private final String[] cols = {
            "prescription_id", "patient_id", "clinician_id", "appointment_id",
            "prescription_date", "medication", "dosage", "frequency",
            "duration_days", "quantity", "instructions", "pharmacy_name", "status",
            "issue_date", "collection_date"
    };

    private List<Prescription> data = new ArrayList<>();

    public void setData(List<Prescription> prescriptions) {
        this.data = prescriptions;
        fireTableDataChanged();
    }

    public Prescription getAt(int row) {
        return data.get(row);
    }

    @Override public int getRowCount() { return data.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int c) { return cols[c]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Prescription p = data.get(rowIndex);

        // Adjust these if your Prescription getters differ
        return switch (columnIndex) {
            case 0 -> p.getPrescriptionId();
            case 1 -> p.getPatientId();
            case 2 -> p.getClinicianId();
            case 3 -> p.getAppointmentId();
            case 4 -> ""; // you didn't expose prescriptionDate getter in your snippet
            case 5 -> (p.getDrug() == null ? "" : p.getDrug().toString());
            case 6 -> ""; // dosage (no getter in your snippet)
            case 7 -> ""; // frequency
            case 8 -> ""; // durationDays
            case 9 -> ""; // quantity
            case 10 -> ""; // instructions
            case 11 -> ""; // pharmacyName
            case 12 -> (p.getStatus() == null ? "" : p.getStatus().name());
            case 13 -> ""; // issueDate
            case 14 -> ""; // collectionDate
            default -> "";
        };
    }
}

