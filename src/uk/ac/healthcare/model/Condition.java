package uk.ac.healthcare.model;

import java.util.Objects;

public class Condition {
    private final String name;

    public Condition(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("condition name required");
        this.name = name.trim();
    }

    public String getName() { return name; }

    @Override public String toString() { return name; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Condition)) return false;
        Condition that = (Condition) o;
        return name.equalsIgnoreCase(that.name);
    }

    @Override public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }
}
