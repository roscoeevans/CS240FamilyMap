package Model;

import java.sql.SQLException;

public class User {
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String personID;

    public User(String userName, String password, String email, String firstName, String lastName, String gender, String personID) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    public User() {
        this.userName = "";
        this.password = "";
        this.email = "";
        this.firstName = "";
        this.lastName = "";
        this.gender = "";
        this.personID = "";
    }

    public String getUserName() {
        if (userName == null) {
            return "";
        }
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void validate() throws SQLException {
        SQLException ex = new SQLException();
        if (userName == null) {
            throw ex;
        }
        if (password == null) {
            throw ex;
        }
        if (email == null) {
            throw ex;
        }
        if (firstName == null) {
            throw ex;
        }
        if (lastName == null) {
            throw ex;
        }
        if (gender == null) {
            throw ex;
        }
        if (personID == null) {
            throw ex;
        }
    }
}
