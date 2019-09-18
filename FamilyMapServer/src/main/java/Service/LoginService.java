package Service;

import java.util.UUID;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.UserDAO;
import Model.AuthToken;
import Model.User;
import Request.LoginRequest;
import Response.LoginResponse;

public class LoginService {
    public LoginResponse login(LoginRequest request) {
        LoginResponse response;
        AuthTokenDAO atdao = new AuthTokenDAO();
        UserDAO udao = new UserDAO();

        UUID token = UUID.randomUUID();

        try {
            if (!request.validate()) {
                response = new LoginResponse("Missing data.");
            }
            else {
                User user = udao.find(request.getUserName());
                if (user != null) {
                    if (user.getPassword().equals(request.getPassword())) {
                        AuthToken authToken = new AuthToken(token.toString(), request.getUserName());
                        if (!atdao.insert(authToken)) {
                            response = new LoginResponse("Internal server error while inserting Authorization Token into database.");
                        } else {
                            response = new LoginResponse(token.toString(), request.getUserName(), user.getPersonID());
                        }
                    } else {
                        response = new LoginResponse("Wrong password.");
                    }
                } else {
                    response = new LoginResponse("No username found by that name.");
                }
            }
        } catch (DataAccessException ex) {
            response = new LoginResponse("Internal server error.");
            return response;
        }

        return response;
    }
}
