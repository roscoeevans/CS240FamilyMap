package Request;

import java.sql.SQLException;

public class LoginRequest {
    private String userName;
    private String password;

    public String getUserName() {
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

    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    public boolean validate() {
        SQLException ex = new SQLException();
        if (this.userName.equals(null) || this.userName.isEmpty()) {
            return false;
        }
        if (this.password.equals(null) || this.password.isEmpty()) {
            return false;
        }
        return true;
    }
}
