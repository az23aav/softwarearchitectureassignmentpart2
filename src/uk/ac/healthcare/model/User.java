package uk.ac.healthcare.model;

public abstract class User {
    protected final String userId;
    protected String firstName;
    protected String lastName;
    protected String email;

    protected User(String userId, String firstName, String lastName, String email) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return firstName + " " + lastName;
    }
}
