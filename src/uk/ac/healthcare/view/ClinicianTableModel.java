package uk.ac.healthcare.view;

import uk.ac.healthcare.model.*;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ClinicianTableModel extends AbstractTableModel {

    private final String[] cols = {
            "clinician_id","first_name","last_name","email","type","speciality","registration_no","workplace_id"
    };

    private List<Clinician> data = new ArrayList<>();

    public void setData(List<Clinician> clinicians) {
        this.data = clinicians;
        fireTableDataChanged();
    }

    public Clinician getAt(int row) {
        if (row < 0 || row >= data.size()) return null;
        return data.get(row);
    }

    @Override public int getRowCount() { return data.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int column) { return cols[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Clinician c = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> c.getClinicianId();
            case 1 -> c.getFirstName();
            case 2 -> c.getLastName();
            case 3 -> c.getEmail();
            case 4 -> typeOf(c);
            case 5 -> c.getSpeciality();
            case 6 -> c.getRegistrationNo();
            case 7 -> c.getWorkplaceId();
            default -> "";
        };
    }

    private String typeOf(Clinician c) {
        if (c instanceof GeneralPractitioner) return "GP";
        if (c instanceof Nurse) return "Nurse";
        if (c instanceof SpecialistDoctor) return "Consultant";
        return "Clinician";
    }
}
