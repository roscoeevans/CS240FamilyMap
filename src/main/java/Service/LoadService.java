package Service;

import java.util.ArrayList;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.EventDAO;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import Model.Event;
import Model.Person;
import Model.User;
import Request.LoadRequest;
import Response.LoadResponse;

public class LoadService {
    public LoadResponse load(LoadRequest request) {
        UserDAO userDao = new UserDAO();
        AuthTokenDAO authTokenDao = new AuthTokenDAO();
        EventDAO eventDao = new EventDAO();
        PersonDAO personDao = new PersonDAO();
        LoadResponse response = null;
        ArrayList<User> users = request.getUsers();
        ArrayList<Person> persons = request.getPersons();
        ArrayList<Event> events = request.getEvents();

        try {
            userDao.clear();
            authTokenDao.clear();
            eventDao.clear();
            personDao.clear();

            for (int i = 0; i < users.size(); i++) {
                userDao.insert(users.get(i));
            }
            for (int i = 0; i < events.size(); i++) {
                eventDao.insert(events.get(i));
            }
            for (int i = 0; i < persons.size(); i++) {
                personDao.insert(persons.get(i));
            }
            response = new LoadResponse("Succesfully added " + users.size() +
                    " users, " + persons.size() + " persons, and " + events.size() + " events to the database");
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            response = new LoadResponse("Internal Server Error");

        }

        return response;
    }
}
