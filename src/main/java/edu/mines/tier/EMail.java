package edu.mines.tier;

import java.util.Objects;

public class EMail {
    private String email;
    private String type;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof EMail)) return false;
        EMail eMail = (EMail) o;
        return Objects.equals(email, eMail.email) &&
                Objects.equals(type, eMail.type);
    }

    @Override
    public int hashCode() {

        return Objects.hash(email, type);
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
