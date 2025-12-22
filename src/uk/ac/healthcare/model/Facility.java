package uk.ac.healthcare.model;

public class Facility {

    private final String facilityId;
    private final String name;
    private final String type;        // GP Surgery / Hospital
    private final String address;
    private final String postcode;
    private final String phone;
    private final String email;
    private final String openingHours;
    private final String managerName;
    private final int capacity;
    private final String specialtiesOffered;

    public Facility(String facilityId,
                    String name,
                    String type,
                    String address,
                    String postcode,
                    String phone,
                    String email,
                    String openingHours,
                    String managerName,
                    int capacity,
                    String specialtiesOffered) {

        this.facilityId = facilityId;
        this.name = name;
        this.type = type;
        this.address = address;
        this.postcode = postcode;
        this.phone = phone;
        this.email = email;
        this.openingHours = openingHours;
        this.managerName = managerName;
        this.capacity = capacity;
        this.specialtiesOffered = specialtiesOffered;
    }

    public String getFacilityId() { return facilityId; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getAddress() { return address; }
    public String getPostcode() { return postcode; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getOpeningHours() { return openingHours; }
    public String getManagerName() { return managerName; }
    public int getCapacity() { return capacity; }
    public String getSpecialtiesOffered() { return specialtiesOffered; }
}

