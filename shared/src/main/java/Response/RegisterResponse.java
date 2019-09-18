package Response;

public class RegisterResponse {
    private String authToken;
    private String userName;
    private String personID;
    private String message;

    public RegisterResponse(String authToken, String userName, String personID) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
        this.message = null;
    }

    public RegisterResponse(String message) {
        this.message = message;
        this.authToken = null;
        this.userName = null;
        this.personID = null;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
