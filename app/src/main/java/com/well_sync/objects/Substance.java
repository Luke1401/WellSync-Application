package com.well_sync.objects;

import java.util.Objects;

public class Substance {
    private String name;
    private final int quantity;

    public Substance(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Substance substance = (Substance) o;
        return quantity == substance.quantity && Objects.equals(name, substance.name);
    }

}
