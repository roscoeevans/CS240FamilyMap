package Service;

import java.util.Random;
import java.util.UUID;

import DataAccess.DataAccessException;
import DataAccess.EventDAO;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import Model.Person;
import Model.Event;
import Model.User;
import Response.FillResponse;
import DataAccess.DataAccessException;
import DataAccess.EventDAO;
import DataAccess.PersonDAO;
import Info.Location;
import Info.Locations;
import Info.Names;

public class FillService {
    int persons;
    int events;

    public FillService() {
        persons = 0;
        events = 0;
    }


    ////YOU GOTTA LOVE THE PAIN AND AGONY
    public Person generateFather(Person subject) throws DataAccessException{
        PersonDAO personDao = new PersonDAO();
        Names maleNames = new Names("/Users/roscoeevans/Desktop/code/240/truefms/familymapserver/src/main/json/mnames.json");
        Names surNames = new Names("/Users/roscoeevans/Desktop/code/240/truefms/familymapserver/src/main/json/snames.json");

        UUID personID = UUID.randomUUID();
        Person newFather = new Person(personID.toString(), subject.getDescendant(), maleNames.getRandomName(), surNames.getRandomName(), "m");
        //this.father = newFather.personID;
        subject.setFather(newFather.getPersonID());
        personDao.updatePerson(subject);
        personDao.insert(newFather);

        return newFather;
    }

    public Person generateMother(Person subject) throws DataAccessException {
        PersonDAO personDao = new PersonDAO();
        Names femaleNames = new Names("/Users/roscoeevans/Desktop/code/240/truefms/familymapserver/src/main/json/fnames.json");
        Names surNames = new Names("/Users/roscoeevans/Desktop/code/240/truefms/familymapserver/src/main/json/snames.json");

        UUID personID = UUID.randomUUID();
        Person newMother = new Person(personID.toString(), subject.getDescendant(), femaleNames.getRandomName(), surNames.getRandomName(), "f");
        //this.mother = newMother.personID;
        subject.setMother(newMother.getPersonID());
        personDao.updatePerson(subject);
        personDao.insert(newMother);

        return newMother;

    }

    public void generateEvents(Person subject, Integer year) throws DataAccessException {
        EventDAO eventDao = new EventDAO();

        eventDao.insert(generateBirth(subject, year));
        eventDao.insert(generateDeath(subject, year));
    }

    public Event generateBirth(Person subject, Integer year) throws DataAccessException {
        Locations locations = new Locations("/Users/roscoeevans/Desktop/code/240/truefms/familymapserver/src/main/json/locations.json");
        Location location = locations.getRandomLocation();

        Random rand = new Random();
        int randy = rand.nextInt(10);

        Integer cushion = 90 - randy;

        year = year - cushion;

        UUID eventID = UUID.randomUUID();
        Event birth = new Event(eventID.toString(), subject.getDescendant(), subject.getPersonID(), location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(), "birth", year);
        return birth;
    }

    public void generateMarriage(Person subject, Integer year, Person spouse) throws DataAccessException {
        EventDAO eventDao = new EventDAO();
        Locations locations = new Locations("/Users/roscoeevans/Desktop/code/240/truefms/familymapserver/src/main/json/locations.json");
        Location location = locations.getRandomLocation();

        Random rand = new Random();
        int randy = rand.nextInt(10);
        Integer cushion = 60 - randy;
        year = year - cushion;

        //this.spouse = spouse.getPersonID();
        subject.setSpouse(spouse.getPersonID());
        spouse.setSpouse(subject.getPersonID());

        UUID eventID = UUID.randomUUID();
        UUID eventID2 = UUID.randomUUID();

        Event male = new Event(eventID.toString(), subject.getDescendant(), subject.getPersonID(), location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(), "marriage", year);
        Event female = new Event(eventID2.toString(), subject.getDescendant(), spouse.getPersonID(), location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(), "marriage", year);
        eventDao.insert(male);
        eventDao.insert(female);
    }

    public Event generateDeath(Person subject, Integer year) {
        Locations locations = new Locations("/Users/roscoeevans/Desktop/code/240/truefms/familymapserver/src/main/json/locations.json");
        Location location = locations.getRandomLocation();

        UUID eventID = UUID.randomUUID();
        Event death = new Event(eventID.toString(), subject.getDescendant(), subject.getPersonID(), location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(), "death", year);
        return death;
    }
    ////

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
                        eventDao.insert(generateBirth(root, 2080));
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
            Person father = generateFather(person);
            Person mother = generateMother(person);
            generateEvents(father, year);
            generateEvents(mother, year);
            generateMarriage(father, year, mother);
            persons += 2;
            events += 6;

            gens--;
            year-=100;
            generateTree(father, gens, year);
            generateTree(mother, gens, year);
            personDao.updatePerson(mother);
            personDao.updatePerson(father);
        }
    }
}
