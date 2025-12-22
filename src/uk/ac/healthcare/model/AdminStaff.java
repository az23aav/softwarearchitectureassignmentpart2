package uk.ac.healthcare.model;

import java.time.LocalDate;

public class AdminStaff extends User {

    private final String role;
    private final String department;
    private final String facilityId;

    private final String phoneNumber;
    private final String employmentStatus;
    private final LocalDate startDate;
    private final String lineManager;

    private final String accessLevel;

    public AdminStaff(
            String staffId,
            String firstName,
            String lastName,
            String email,
            String role,
            String department,
            String facilityId,
            String phoneNumber,
            String employmentStatus,
            LocalDate startDate,
            String lineManager,
            String accessLevel
    ) {
        super(staffId, firstName, lastName, email);
        this.role = role;
        this.department = department;
        this.facilityId = facilityId;
        this.phoneNumber = phoneNumber;
        this.employmentStatus = employmentStatus;
        this.startDate = startDate;
        this.lineManager = lineManager;
        this.accessLevel = accessLevel;
    }

    public String getRole() { return role; }
    public String getDepartment() { return department; }
    public String getFacilityId() { return facilityId; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmploymentStatus() { return employmentStatus; }
    public LocalDate getStartDate() { return startDate; }
    public String getLineManager() { return lineManager; }
    public String getAccessLevel() { return accessLevel; }
}

