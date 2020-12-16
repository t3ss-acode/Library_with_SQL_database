package model.objects;

import java.sql.Date;

public class Author {
    private String firstName;
    private String lastName;
    private Date dob;

    public Author(String firstName, String lastName, Date dob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getDob() {
        return dob;
    }

    public String getFirstName() {
        return firstName;
    }

    @Override
    public String toString() {
        return "" + firstName + ", " + lastName;
    }
}
