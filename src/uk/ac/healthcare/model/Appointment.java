package uk.ac.healthcare.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private final String appointmentId;
    private final String patientId;
    private final String clinicianId;
    private final String facilityId;

    private LocalDate date;
    private LocalTime time;
    private int durationMinutes;
    private String type;
    private AppointmentStatus status;
    private String reason;
    private String notes;

    public Appointment(String appointmentId, String patientId, String clinicianId, String facilityId,
                       LocalDate date, LocalTime time, int durationMinutes, String type,
                       AppointmentStatus status, String reason, String notes) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.clinicianId = clinicianId;
        this.facilityId = facilityId;
        this.date = date;
        this.time = time;
        this.durationMinutes = durationMinutes;
        this.type = type;
        this.status = status;
        this.reason = reason;
        this.notes = notes;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getClinicianId() {
        return clinicianId;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getType() {
        return type;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public String getNotes() {
        return notes;
    }

    public void modify(LocalDate newDate, LocalTime newTime, int newDurationMinutes) {
        this.date = newDate;
        this.time = newTime;
        this.durationMinutes = newDurationMinutes;
    }

    public void cancel() { this.status = AppointmentStatus.CANCELLED; }
}
