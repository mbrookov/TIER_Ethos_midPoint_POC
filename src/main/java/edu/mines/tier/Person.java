package edu.mines.tier;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private String id;
    private String UDCID;
    private String pidm;
    private String firstName;
    private String lastName;
    private String formatted;

    private boolean ok;
    public List<EMail> email;
    public List<Phone> phone;

    public Person() {
        email = new ArrayList<EMail>();
        phone = new ArrayList<Phone>();
    }

    public boolean getOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getUDCID() {
        return UDCID;
    }

    public void setUDCID(String UDCID) {
        this.UDCID = UDCID;
    }

    public String getPidm() {
        return pidm;
    }

    public void setPidm(String pidm) {
        this.pidm = pidm;
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
