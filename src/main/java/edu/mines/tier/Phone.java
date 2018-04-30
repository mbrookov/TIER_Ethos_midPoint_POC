package edu.mines.tier;

import java.util.Objects;

public class Phone {
    private String type;
    private String number;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Phone)) return false;
        Phone phone = (Phone) o;
        return Objects.equals(type, phone.type) &&
                Objects.equals(number, phone.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, number);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
