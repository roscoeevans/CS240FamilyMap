package Service;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.EventDAO;
import DataAccess.UserDAO;
import Model.AuthToken;
import Model.Event;
import Model.User;
import Response.EventResponse;

public class EventService {
    public EventResponse getEvent(String authString, String eventID) {
        EventDAO eventDao = new EventDAO();
        EventResponse response;
        try {
            Event event = eventDao.findEvent(eventID);
            if (event == null) {
                response = new EventResponse("There is no event found with that eventID.");
            }
            else {
                UserDAO userDao = new UserDAO();
                AuthTokenDAO authTokenDao = new AuthTokenDAO();
                AuthToken authToken = authTokenDao.findByAuthToken(authString);
                if (authToken == null) {
                    response = new EventResponse("Invalid authorization token.");
                }
                else {
                    User user = userDao.find(authToken.getUserName());
                    String username = user.getUserName();
                    if (!username.equals(event.getDescendant())) {
                        response = new EventResponse("This event does not belong to you.");
                    }
                    else {
                        response = new EventResponse(event);
                    }
                }
            }

        } catch (DataAccessException ex) {
            response = new EventResponse("Internal server error.");
        }

        return response;
    }

    public EventResponse getAllEvents(String authString) {
        EventResponse response = null;
        EventDAO eventDAO = new EventDAO();

        UserDAO userDao = new UserDAO();
        AuthTokenDAO authTokenDao = new AuthTokenDAO();

        try {
        AuthToken authToken = authTokenDao.findByAuthToken(authString);
        if (authToken == null) {
            response = new EventResponse("Invalid authorization token.");
        }
        else {
            User user = userDao.find(authToken.getUserName());
            String username = user.getUserName();
            response = new EventResponse(eventDAO.findAllEvents(username));
        }

    } catch (DataAccessException ex) {
        response = new EventResponse("Internal server error.");
    }
        return response;
    }
}
