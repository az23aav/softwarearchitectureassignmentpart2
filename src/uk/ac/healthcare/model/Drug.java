package uk.ac.healthcare.model;

import java.util.Objects;

public class Drug {
    private final String name;

    public Drug(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("drug name required");
        this.name = name.trim();
    }

    public String getName() { return name; }

    @Override public String toString() { return name; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Drug)) return false;
        Drug drug = (Drug) o;
        return name.equalsIgnoreCase(drug.name);
    }

    @Override public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }
}
