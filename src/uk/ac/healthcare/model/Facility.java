package uk.ac.healthcare.model;

public class Facility {

    private String facilityId;
    private String name;
    private String type;   // GP / Hospital
    private String address;

    public Facility(String facilityId, String name, String type, String address) {
        this.facilityId = facilityId;
        this.name = name;
        this.type = type;
        this.address = address;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }
}
