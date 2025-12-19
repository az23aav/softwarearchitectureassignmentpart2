package uk.ac.healthcare.model;

public class GeneralPractitioner extends Clinician {
    public GeneralPractitioner(String clinicianId, String firstName, String lastName, String email,
                               String speciality, String gmcNumber, String workplaceId) {
        super(clinicianId, firstName, lastName, email, speciality, gmcNumber, workplaceId);
    }
}
