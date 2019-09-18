package Service;

import java.awt.Event;

import DataAccess.DataAccessException;
import DataAccess.EventDAO;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import Model.Person;
import Model.User;
import Response.FillResponse;

public class FillService {
    int persons;
    int events;

    public FillService() {
        persons = 0;
        events = 0;
    }

    public FillResponse fill(String username, Integer gens) {
        FillResponse response = null;
        UserDAO userDao = new UserDAO();
        PersonDAO personDao = new PersonDAO();
        EventDAO eventDao = new EventDAO();
        try {
            User user = userDao.find(username);
            if (user == null) {
                response = new FillResponse("User not found.");
            }
            else {
                Person root = personDao.find(user.getPersonID());
                if (root == null) {
                    response = new FillResponse("No Person associated with this User");
                }
                else {
                    if (gens < 0) {
                        response = new FillResponse("The number of generations must be non-negative");
                    } else {
                        eventDao.deleteEvents(username);
                        personDao.deletePersons(username);
                        eventDao.insert(root.generateBirth(2080));
                        generateTree(root, gens, 2012);
                        personDao.insert(root);
                        events++;
                        persons++;

                        response = new FillResponse("Successfully added " + persons + " persons and " + events + " events to the database.");
                    }
                }
            }

        } catch (DataAccessException ex) {
            ex.printStackTrace();
            response = new FillResponse("Internal server error.");
            return response;
        }
        return response;
    }

    private void generateTree(Person person, Integer gens, Integer year) throws DataAccessException {
        if (gens > 0) {
            PersonDAO personDao = new PersonDAO();
            Person father = person.generateFather();
            Person mother = person.generateMother();
            father.generateEvents(year);
            mother.generateEvents(year);
            father.generateMarriage(year, mother);
            persons += 2;
            events += 6;

            gens--;
            year = year - 100;
            generateTree(father, gens, year);
            generateTree(mother, gens, year);
            personDao.updatePerson(mother);
            personDao.updatePerson(father);
        }
    }
}
