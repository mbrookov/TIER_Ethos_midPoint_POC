package edu.mines.tier;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private String id;
    private String firstName;
    private String lastName;
    private String formatted;
    public List<EMail> email;
    public List<Phone> phone;

    public Person() {
        email = new ArrayList<EMail>();
        phone = new ArrayList<Phone>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFormatted() {
        return formatted;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }
}
