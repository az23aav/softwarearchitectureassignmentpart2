package uk.ac.healthcare.model;

public class Facility {
    private final String facilityId;
    private final String name;
    private final String type;
    private final String address;
    private final String postcode;
    private final String phone;
    private final String email;

    public Facility(String facilityId, String name, String type, String address, String postcode, String phone, String email) {
        this.facilityId = facilityId;
        this.name = name;
        this.type = type;
        this.address = address;
        this.postcode = postcode;
        this.phone = phone;
        this.email = email;
    }

    public String getFacilityId() { return facilityId; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getAddress() { return address; }
    public String getPostcode() { return postcode; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
}
