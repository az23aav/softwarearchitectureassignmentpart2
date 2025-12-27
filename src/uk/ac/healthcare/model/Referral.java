package uk.ac.healthcare.model;

import java.time.LocalDate;

public class Referral {
    private final String referralId;
    private final String patientId;
    private final String referringClinicianId;
    private final String referredToClinicianId;
    private final String referringFacilityId;
    private final String referredToFacilityId;

    private final LocalDate referralDate;
    private final String urgencyLevel;
    private final String reason;
    private final String clinicalSummary;
    private final String requestedInvestigations;

    private ReferralStatus status;
    private final String appointmentId;
    private String notes;

    public Referral(String referralId, String patientId, String referringClinicianId, String referredToClinicianId,
                    String referringFacilityId, String referredToFacilityId, LocalDate referralDate, String urgencyLevel,
                    String reason, String clinicalSummary, String requestedInvestigations, ReferralStatus status,
                    String appointmentId, String notes) {
        this.referralId = referralId;
        this.patientId = patientId;
        this.referringClinicianId = referringClinicianId;
        this.referredToClinicianId = referredToClinicianId;
        this.referringFacilityId = referringFacilityId;
        this.referredToFacilityId = referredToFacilityId;
        this.referralDate = referralDate;
        this.urgencyLevel = urgencyLevel;
        this.reason = reason;
        this.clinicalSummary = clinicalSummary;
        this.requestedInvestigations = requestedInvestigations;
        this.status = status;
        this.appointmentId = appointmentId;
        this.notes = notes;
    }

    public String getReferralId() {
        return referralId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getReferringClinicianId() {
        return referringClinicianId;
    }

    public String getReferredToClinicianId() {
        return referredToClinicianId;
    }

    public String getReferringFacilityId() {
        return referringFacilityId;
    }

    public String getReferredToFacilityId() {
        return referredToFacilityId;
    }

    public ReferralStatus getStatus() {
        return status;
    }

    public LocalDate getReferralDate() {return referralDate;}

    public String getUrgencyLevel() {return urgencyLevel;}

    public String getReason() {return reason;}

    public String getClinicalSummary() {return clinicalSummary;}

    public String getRequestedInvestigations() {return requestedInvestigations;}

    public String getAppointmentId() {return appointmentId;}

    public String getNotes() {return notes;}

    public void setNotes(String notes) {this.notes = notes;}

    public void markSent() {
        this.status = ReferralStatus.SENT;
    }
    public void markReceived() {
        this.status = ReferralStatus.RECEIVED;
    }
    public void close() {
        this.status = ReferralStatus.CLOSED;
    }

    public String toEmailText() {
        return "REFERRAL: " + referralId + "\n" +
               "Patient ID: " + patientId + "\n" +
               "From Clinician: " + referringClinicianId + " (Facility " + referringFacilityId + ")\n" +
               "To Clinician: " + referredToClinicianId + " (Facility " + referredToFacilityId + ")\n" +
               "Date: " + referralDate + "\n" +
               "Urgency: " + urgencyLevel + "\n" +
               "Reason: " + reason + "\n" +
               "Clinical summary: " + clinicalSummary + "\n" +
               "Investigations requested: " + requestedInvestigations + "\n" +
               "Status: " + status + "\n" +
               "Notes: " + notes + "\n";
    }
}
