package com.well_sync.objects;

import java.util.Objects;

public class Doctor {
    private final String email;
    private String firstName;
    private String lastName;

    public Doctor(final String email) {
        this.email = email;
    }

    public Doctor(final String email, final String firstName, final String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(email, doctor.email) &&
                Objects.equals(firstName, doctor.firstName) &&
                Objects.equals(lastName, doctor.lastName);
    }
}
