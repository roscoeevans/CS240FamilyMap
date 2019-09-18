package Model;

import java.util.Random;
import java.util.UUID;

public class Person {
    private String personID;
    private String descendant;
    private String firstName;
    private String lastName;
    private String gender;
    private String father;
    private String mother;
    private String spouse;

    public Person(String personID, String descendant, String firstName, String lastName, String gender, String father, String mother, String spouse) {
        this.personID = personID;
        this.descendant = descendant;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.father = father;
        this.mother = mother;
        this.spouse = spouse;
    }

    public Person(String personID, String descendant, String firstName, String lastName, String gender) {
        this.personID = personID;
        this.descendant = descendant;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.father = null;
        this.mother = null;
        this.spouse = null;
    }

    /*public Person generateFather() throws DataAccessException{
        PersonDAO personDao = new PersonDAO();
        Names maleNames = new Names("/Users/roscoeevans/Desktop/code/240/truefms/familymapserver/src/main/json/mnames.json");
        Names surNames = new Names("/Users/roscoeevans/Desktop/code/240/truefms/familymapserver/src/main/json/snames.json");

        UUID personID = UUID.randomUUID();
        Person newFather = new Person(personID.toString(), this.descendant, maleNames.getRandomName(), surNames.getRandomName(), "m");
        this.father = newFather.personID;
        personDao.updatePerson(this);
        personDao.insert(newFather);

        return newFather;
    }

    public Person generateMother() throws DataAccessException {
        PersonDAO personDao = new PersonDAO();
        Names femaleNames = new Names("/Users/roscoeevans/Desktop/code/240/truefms/familymapserver/src/main/json/fnames.json");
        Names surNames = new Names("/Users/roscoeevans/Desktop/code/240/truefms/familymapserver/src/main/json/snames.json");

        UUID personID = UUID.randomUUID();
        Person newMother = new Person(personID.toString(), this.descendant, femaleNames.getRandomName(), surNames.getRandomName(), "f");
        this.mother = newMother.personID;
        personDao.updatePerson(this);
        personDao.insert(newMother);

        return newMother;

    }

    public void generateEvents(Integer year) throws DataAccessException {
        EventDAO eventDao = new EventDAO();

        eventDao.insert(generateBirth(year));
        eventDao.insert(generateDeath(year));
    }

    public Event generateBirth(Integer year) throws DataAccessException {
        Locations locations = new Locations("/Users/roscoeevans/Desktop/code/240/truefms/familymapserver/src/main/json/locations.json");
        Location location = locations.getRandomLocation();

        Random rand = new Random();
        int randy = rand.nextInt(10);

        Integer cushion = 90 - randy;

        year = year - cushion;

        UUID eventID = UUID.randomUUID();
        Event birth = new Event(eventID.toString(), this.descendant, this.personID, location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(), "birth", year);
        return birth;
    }

    public void generateMarriage(Integer year, Person spouse) throws DataAccessException {
        EventDAO eventDao = new EventDAO();
        Locations locations = new Locations("/Users/roscoeevans/Desktop/code/240/truefms/familymapserver/src/main/json/locations.json");
        Location location = locations.getRandomLocation();

        Random rand = new Random();
        int randy = rand.nextInt(10);
        Integer cushion = 60 - randy;
        year = year - cushion;

        this.spouse = spouse.getPersonID();
        spouse.setSpouse(this.getPersonID());

        UUID eventID = UUID.randomUUID();
        UUID eventID2 = UUID.randomUUID();

        Event male = new Event(eventID.toString(), this.descendant, this.personID, location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(), "marriage", year);
        Event female = new Event(eventID2.toString(), this.descendant, spouse.getPersonID(), location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(), "marriage", year);
        eventDao.insert(male);
        eventDao.insert(female);
    }

    public Event generateDeath(Integer year) {
        Locations locations = new Locations("/Users/roscoeevans/Desktop/code/240/truefms/familymapserver/src/main/json/locations.json");
        Location location = locations.getRandomLocation();

        UUID eventID = UUID.randomUUID();
        Event death = new Event(eventID.toString(), this.descendant, this.personID, location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(), "death", year);
        return death;
    }*/

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }
}
