package Model;

public class AuthToken {
    private String authToken;
    private String userName;

    public AuthToken(String authToken, String userName) {
        this.authToken = authToken;
        this.userName = userName;
    }

    public AuthToken() {
        this.authToken = "";
        this.userName = "";
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
}
