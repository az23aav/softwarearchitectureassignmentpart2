package uk.ac.healthcare.model;

public class Nurse extends Clinician {
    public Nurse(String clinicianId, String firstName, String lastName, String email,
                 String speciality, String nmcNumber, String workplaceId) {
        super(clinicianId, firstName, lastName, email, speciality, nmcNumber, workplaceId);
    }
}
