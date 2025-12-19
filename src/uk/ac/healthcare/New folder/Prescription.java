package uk.ac.healthcare.model;

import java.time.LocalDate;

public class Prescription {
    private final String prescriptionId;
    private final String patientId;
    private final String clinicianId;
    private final String appointmentId;

    private final LocalDate prescriptionDate;
    private final Drug drug;
    private String dosage;
    private String frequency;
    private int durationDays;
    private int quantity;
    private String instructions;
    private String pharmacyName;
    private PrescriptionStatus status;
    private LocalDate issueDate;
    private LocalDate collectionDate;

    private final Condition condition;

    public Prescription(String prescriptionId, String patientId, String clinicianId, String appointmentId,
                        LocalDate prescriptionDate, Drug drug, Condition condition,
                        String dosage, String frequency, int durationDays, int quantity,
                        String instructions, String pharmacyName, PrescriptionStatus status,
                        LocalDate issueDate, LocalDate collectionDate) {
        this.prescriptionId = prescriptionId;
        this.patientId = patientId;
        this.clinicianId = clinicianId;
        this.appointmentId = appointmentId;
        this.prescriptionDate = prescriptionDate;
        this.drug = drug;
        this.condition = condition;
        this.dosage = dosage;
        this.frequency = frequency;
        this.durationDays = durationDays;
        this.quantity = quantity;
        this.instructions = instructions;
        this.pharmacyName = pharmacyName;
        this.status = status;
        this.issueDate = issueDate;
        this.collectionDate = collectionDate;
    }

    public String getPrescriptionId() { return prescriptionId; }
    public String getPatientId() { return patientId; }
    public String getClinicianId() { return clinicianId; }
    public String getAppointmentId() { return appointmentId; }
    public Drug getDrug() { return drug; }
    public Condition getCondition() { return condition; }
    public PrescriptionStatus getStatus() { return status; }

    public String toFileContent() {
        return "Prescription ID: " + prescriptionId + "\n" +
               "Patient ID: " + patientId + "\n" +
               "Clinician ID: " + clinicianId + "\n" +
               "Condition: " + condition + "\n" +
               "Drug: " + drug + "\n" +
               "Dosage: " + dosage + "\n" +
               "Frequency: " + frequency + "\n" +
               "Duration(days): " + durationDays + "\n" +
               "Quantity: " + quantity + "\n" +
               "Instructions: " + instructions + "\n" +
               "Pharmacy: " + pharmacyName + "\n" +
               "Status: " + status + "\n";
    }
}
