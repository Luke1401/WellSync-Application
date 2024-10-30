package com.well_sync.objects;

import java.util.Objects;

public class Medication {
    private String name;
    private final int quantity; // number of tablets?
    private final int dosage;

    public Medication(String name, int quantity, int dosage) {
        this.name = name;
        this.quantity = quantity;
        this.dosage = dosage;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public int getDosage(){
        return this.dosage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medication that = (Medication) o;
        return quantity == that.quantity && dosage == that.dosage && Objects.equals(name, that.name);
    }

}
