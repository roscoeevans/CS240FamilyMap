package Service;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import Model.AuthToken;
import Model.Person;
import Model.User;
import Response.PersonResponse;

public class PersonService {
    public PersonResponse getPerson(String authString, String personID) {
        PersonDAO personDao = new PersonDAO();
        PersonResponse response = null;
        UserDAO userDao = new UserDAO();
        try {
            Person person = personDao.find(personID);
            if (person == null) {
                response = new PersonResponse("There is no person found with that personID.");
            }
            else {
                AuthTokenDAO authTokenDao = new AuthTokenDAO();
                AuthToken authToken = authTokenDao.findByAuthToken(authString);

                if (authToken == null) {
                    response = new PersonResponse("Invalid authorization token.");
                }
                else {
                    User owner = userDao.find(authToken.getUserName());
                    String username = owner.getUserName();

                    if (!person.getDescendant().equals(username)) {
                        response = new PersonResponse("This Person does not belong to you.");
                    }
                    else {
                        response = new PersonResponse(person);
                    }
                }
            }
        } catch(DataAccessException ex){
            response = new PersonResponse("Internal server error.");
        }

        return response;
    }
    public PersonResponse getAllPersons(String authString) {
        PersonResponse response = null;
        PersonDAO personDao = new PersonDAO();

        UserDAO userDao = new UserDAO();
        AuthTokenDAO authTokenDao = new AuthTokenDAO();

        try {
            AuthToken authToken = authTokenDao.findByAuthToken(authString);
            if (authToken == null) {
                response = new PersonResponse("Invalid authorization token.");
            }
            else {
                User user = userDao.find(authToken.getUserName());
                String username = user.getUserName();
                response = new PersonResponse(personDao.findAllPersons(username));
            }

        } catch (DataAccessException ex) {
            response = new PersonResponse("Internal server error.");
        }
        return response;
    }
}

