package uk.ac.healthcare.model;

public class AdminStaff extends User {
    private final String role;
    private final String facilityId;
    private final String accessLevel;

    public AdminStaff(String staffId, String firstName, String lastName, String email,
                      String role, String facilityId, String accessLevel) {
        super(staffId, firstName, lastName, email);
        this.role = role;
        this.facilityId = facilityId;
        this.accessLevel = accessLevel;
    }

    public String getRole() {
        return role;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public String getAccessLevel() {
        return accessLevel;
    }
}
