package uk.ac.healthcare.view;

import uk.ac.healthcare.model.Patient;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PatientTableModel extends AbstractTableModel {

    private final String[] columns = {
            "Patient ID", "First Name", "Last Name", "Email", "NHS Number", "Registered Surgery"
    };

    private List<Patient> data = new ArrayList<>();

    public void setPatients(List<Patient> patients) {
        this.data = new ArrayList<>(patients);
        fireTableDataChanged();
    }

    public Patient getPatientAt(int row) {
        if (row < 0 || row >= data.size()) return null;
        return data.get(row);
    }

    @Override
    public int getRowCount() {
        return data.size();
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
    public Object getValueAt(int rowIndex, int columnIndex) {
        Patient p = data.get(rowIndex);

        return switch (columnIndex) {
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
