package uk.ac.healthcare.model;

public class SpecialistDoctor extends Clinician {
    public SpecialistDoctor(String clinicianId, String firstName, String lastName, String email,
                            String speciality, String gmcNumber, String workplaceId) {
        super(clinicianId, firstName, lastName, email, speciality, gmcNumber, workplaceId);
    }
}
