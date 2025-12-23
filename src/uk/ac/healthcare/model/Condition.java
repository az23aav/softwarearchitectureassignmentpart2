package uk.ac.healthcare.model;

public class Condition {
    private final String name;

    public Condition(String name) {
        this.name = name == null ? "" : name.trim();
    }

    public String getName() { return name; }

    @Override
    public String toString() { return name; }
}