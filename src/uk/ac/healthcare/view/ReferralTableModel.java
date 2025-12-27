package uk.ac.healthcare.view;

import uk.ac.healthcare.model.Referral;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ReferralTableModel extends AbstractTableModel {
    private final String[] cols = {
            "referral_id", "patient_id", "referring_clinician_id", "referred_to_clinician_id",
            "referral_date", "urgency_level", "status", "appointment_id"
    };

    private List<Referral> data = new ArrayList<>();

    public void setData(List<Referral> referrals) {
        this.data = referrals == null ? new ArrayList<>() : new ArrayList<>(referrals);
        fireTableDataChanged();
    }

    public Referral getAt(int row) {
        if (row < 0 || row >= data.size()) return null;
        return data.get(row);
    }

    @Override public int getRowCount() { return data.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int column) { return cols[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Referral r = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> r.getReferralId();
            case 1 -> r.getPatientId();
            case 2 -> r.getReferringClinicianId();
            case 3 -> r.getReferredToClinicianId();
            case 4 -> r.getReferralDate() == null ? "" : r.getReferralDate().toString();
            case 5 -> r.getUrgencyLevel();
            case 6 -> r.getStatus() == null ? "" : r.getStatus().name();
            case 7 -> r.getAppointmentId();
            default -> "";
        };
    }
}

