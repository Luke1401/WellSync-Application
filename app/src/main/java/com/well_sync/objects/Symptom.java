package com.well_sync.objects;

import java.util.Objects;

public class Symptom {
    private String name;
    private final int intensity; // from 0 to 10

    public Symptom(String name, int intensity) {
        this.name = name;
        this.intensity = intensity;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIntensity() {
        return this.intensity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symptom symptom = (Symptom) o;
        return intensity == symptom.intensity && Objects.equals(name, symptom.name);
    }

}
