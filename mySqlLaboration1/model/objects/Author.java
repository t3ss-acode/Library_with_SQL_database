package model.objects;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Author {
    private String firstName;
    private String lastName;
    private Date dob;

    public Author(String firstName, String lastName, Date dob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
    }
    /**
     * Method to access the lastName value
     * @return the authors last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Method to access the Dob value
     * @return the authors date of birth
     */
    public Date getDob() {
        return dob;
    }

    /**
     * Method to access the firstName value
     * @return the authors first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Checks if the given first name is a valid firstname-value
     * @param firstname
     * @return boolean value for if it's valid
     */
    static public boolean checkValidFirstname(String firstname){
        return !firstname.trim().isEmpty() && !firstname.matches(".*\\d.*");
    }

    /**
     * Checks if the given last name is a valid lastname-value
     * @param lastname
     * @return boolean value for if it's valid
     */
    static public boolean checkValidLastname(String lastname){
        return !lastname.trim().isEmpty() && !lastname.matches(".*\\d.*");
    }

    /**
     * Checks if the given date is a valid date
     * @param dateOfBirth
     * @return boolean value for if it's valid
     */
    static public boolean checkValidDate(String dateOfBirth){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        try{
            format.parse(dateOfBirth);
            return true;
        }catch(ParseException ex){
            return false;
        }
    }

    /**
     * Custom toString
     *
     * @return return the authors first and last name in string format
     */
    @Override
    public String toString() {
        return "" + firstName + " " + lastName;
    }
}
