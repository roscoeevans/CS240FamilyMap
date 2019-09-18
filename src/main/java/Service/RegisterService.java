package Service;

import java.util.UUID;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.EventDAO;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Request.LoginRequest;
import Request.RegisterRequest;
import Response.RegisterResponse;

public class RegisterService {
    public RegisterResponse register(RegisterRequest request) {
        RegisterResponse response = null;
        UserDAO userDao = new UserDAO();
        PersonDAO personDao = new PersonDAO();
        UUID token = UUID.randomUUID();
        UUID personID = UUID.randomUUID();

        User user = new User(request.getUserName(), request.getPassword(), request.getEmail(), request.getFirstName(), request.getLastName(), request.getGender(), personID.toString());
        try {
            if (!request.validate()) {
                response = new RegisterResponse("Missing data.");
            }
            else {
                if (!request.getGender().equals("m") && !request.getGender().equals("f")) {
                    response = new RegisterResponse("Gender MUST be either m or f.");
                }
                else {
                    if (userDao.find(user.getUserName()) != null) {
                        response = new RegisterResponse("This username is already taken.");
                    } else {
                        userDao.insert(user);
                        Person person = new Person(personID.toString(), user.getUserName(), user.getFirstName(), user.getLastName(), user.getGender());
                        personDao.insert(person);
                        FillService fillService = new FillService();
                        fillService.fill(user.getUserName(), 4);

                        LoginRequest loginRequest = new LoginRequest(user.getUserName(), user.getPassword());
                        LoginService loginService = new LoginService();
                        loginService.login(loginRequest);
                        response = new RegisterResponse(token.toString(), request.getUserName(), personID.toString());
                    }
                }
            }
        } catch (DataAccessException ex) {
            response.setMessage("Internal server error.");
            ex.printStackTrace();
            return response;
        }
        return response;
    }
}
