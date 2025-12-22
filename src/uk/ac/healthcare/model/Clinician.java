package uk.ac.healthcare.model;

public abstract class Clinician extends User {
    protected final String clinicianId;
    protected final String speciality;
    protected final String registrationNo; // e.g., GMC/NMC
    protected final String workplaceId;

    protected Clinician(String clinicianId, String firstName, String lastName, String email,
                        String speciality, String registrationNo, String workplaceId) {
        super(clinicianId, firstName, lastName, email);
        this.clinicianId = clinicianId;
        this.speciality = speciality;
        this.registrationNo = registrationNo;
        this.workplaceId = workplaceId;
    }

    public String getClinicianId() {
        return clinicianId;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public String getWorkplaceId() {
        return workplaceId;
    }
}
