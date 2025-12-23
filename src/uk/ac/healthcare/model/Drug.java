package uk.ac.healthcare.model;

public class Drug {
    private final String name;

    public Drug(String name) {
        this.name = name == null ? "" : name.trim();
    }

    public String getName() { return name; }

    @Override
    public String toString() { return name; }
}

