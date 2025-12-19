package uk.ac.healthcare.model;

public class Patient extends User {
    private final String nhsNumber;
    private final String registeredSurgeryId;
    private final MedicalRecord medicalRecord;

    public Patient(String patientId, String firstName, String lastName, String email, String nhsNumber, String registeredSurgeryId) {
        super(patientId, firstName, lastName, email);
        this.nhsNumber = nhsNumber;
        this.registeredSurgeryId = registeredSurgeryId;
    }

    public String getNhsNumber() { return nhsNumber; }
    public String getRegisteredSurgeryId() { return registeredSurgeryId; }
}
