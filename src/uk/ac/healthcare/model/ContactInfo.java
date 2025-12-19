package uk.ac.healthcare.model;

public final class ContactInfo {
    private final String phoneNumber;
    private final String addressLine;
    private final String city;
    private final String postcode;

    public ContactInfo(String phoneNumber, String addressLine, String city, String postcode) {
        this.phoneNumber = phoneNumber;
        this.addressLine = addressLine;
        this.city = city;
        this.postcode = postcode;
    }

    public String getPhoneNumber() { return phoneNumber; }
    public String getAddressLine() { return addressLine; }
    public String getCity() { return city; }
    public String getPostcode() { return postcode; }
}
