package DataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.Person;

/**
 * Data Access Object class for the Person model class. Data Access allows us to access the stored information in the database.
 */

public class PersonDAO {
    private Connection conn;
    private Database db;

    public PersonDAO() {
        this.db = new Database();
        this.conn = null;
    }
    public PersonDAO(Connection conn) {
        this.conn = conn;
    }
    /**
     * Clears all persons in the database
     * @throws DataAccessException if the persons in the database are not properly accessed
     */
    public boolean clear() throws DataAccessException {
        boolean success = false;
        String sql = "DELETE FROM Persons";
        try {
            this.conn = this.db.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            int deletedRows = stmt.executeUpdate();
            success = true;
            db.closeConnection(success);
        }
        catch(SQLException e) {
            e.printStackTrace();
            success = false;
            db.closeConnection(success);
            throw new DataAccessException("Error occurred while clearing persons table");
        }
        return success;
    }

    /**
     * Finds a specific person given a personID
     * @param personID the ID of the person to be found
     * @return The person if found, null if not found
     * @throws DataAccessException if the persons in the database are not properly accessed
     */
    public Person find(String personID) throws DataAccessException {
        Person p = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE personID = ?;";
        try {
            this.conn = this.db.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next() == true) {
                p = new Person(rs.getString("personID"), rs.getString("descendant"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"), rs.getString("father"), rs.getString("mother"), rs.getString("spouse"));
                db.closeConnection(true);
                return p;
            }
            else {
                db.closeConnection(true);
                return null;
            }
        } catch (SQLException e) {
            db.closeConnection(false);
            e.printStackTrace();
            throw new DataAccessException("Error encountered while trying to find a person");
        }
    }

    /**
     * finds all Persons in persons table with a specific descendant.
     * @param descendant descendant to be searched for.
     * @return arraylist of found persons.
     * @throws DataAccessException for bad data access
     */
    public ArrayList<Person> findAllPersons(String descendant) throws DataAccessException {
        ArrayList<Person> persons = new ArrayList<>();

        Person person;
        ResultSet rs;
        String sql = "SELECT * FROM Persons WHERE descendant = ?;";
        try {
            this.conn = db.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, descendant);
            rs = stmt.executeQuery();
            while (rs.next() == true) {
                person = new Person(rs.getString("personID"), rs.getString("descendant"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"), rs.getString("father"), rs.getString("mother"), rs.getString("spouse"));
                persons.add(person);
            }
            db.closeConnection(true);

        } catch (SQLException e) {
            db.closeConnection(false);
            e.printStackTrace();
            throw new DataAccessException("Error encountered while attempting findAllPersons");
        }

        return persons;
    }

    /**
     * Deletes all persons with a specific descendant.
     * @param descendant descendant to be searched for.
     * @throws DataAccessException for bad data access
     */
    public void deletePersons(String descendant) throws DataAccessException {
        String sql = "DELETE FROM Persons WHERE descendant = ?;";
        try {
            this.conn = db.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, descendant);
            stmt.executeUpdate();
            db.closeConnection(true);

        } catch (SQLException e) {
            db.closeConnection(false);
            e.printStackTrace();
            throw new DataAccessException("Error encountered while attempting findAllPersons");
        }
    }

    /**
     * updates a persons data in the database. Used for if a person is inserted, and then attributes are added later.
     * @param person to be updated
     * @throws DataAccessException for bad data access
     */
    public void updatePerson(Person person) throws DataAccessException {
        String personID = person.getPersonID();
        String sql = "DELETE FROM Persons WHERE personID = ?;";
        try {
            this.conn = db.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, personID);
            stmt.executeUpdate();
            db.closeConnection(true);
            insert(person);

        } catch (SQLException e) {
            db.closeConnection(false);
            e.printStackTrace();
            throw new DataAccessException("Error encountered while attempting updatePerson");
        }
    }

    /**
     * Inserts a person into the database
     * @param person the person to be inserted
     * @return true or false if
     * @throws DataAccessException if the persons in the database are not properly accessed
     */
    public boolean insert(Person person) throws DataAccessException {
        boolean commit = true;
        String sql = "INSERT INTO Persons (personID, descendant, firstName, lastName, gender, father, mother, spouse) VALUES(?,?,?,?,?,?,?,?)";
        if(find(person.getPersonID()) == null) {
            try {
                this.conn = this.db.openConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, person.getPersonID());
                stmt.setString(2, person.getDescendant());
                stmt.setString(3, person.getFirstName());
                stmt.setString(4, person.getLastName());
                stmt.setString(5, person.getGender());
                stmt.setString(6, person.getFather());
                stmt.setString(7, person.getMother());
                stmt.setString(8, person.getSpouse());
                stmt.executeUpdate();
                db.closeConnection(true);
            } catch (SQLException e) {
                commit = false;
                db.closeConnection(false);
                System.out.println("Invalid data");
            }
        }
        else {
            commit = false;
            //System.out.println("Person already in table.");
        }

        return commit;
    }

    /**
     * creates a new table of Persons.
     * @throws DataAccessException for bad access.
     */
    public void createPersonsTable() throws DataAccessException {
        String sql = "Create table if not exists Persons (\n" +
                "\tpersonID varchar(255) not null primary key,\n" +
                "\tdescendant varchar(255) not null,\n" +
                "\tfirstName varchar(255) not null,\n" +
                "\tlast varchar(255) not null,\n" +
                "\tgender varchar(255) not null,\n" +
                "\tfather varchar(255),\n" +
                "\tmother varchar(255),\n" +
                "\tspouse varchar(255),\n" +
                "\tforeign key (descendant) references user(userName)\n" +
                ");";
        try {
            this.conn = this.db.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.execute();
            db.closeConnection(true);
        }
        catch(SQLException e) {
            db.closeConnection(false);
            e.printStackTrace();
            throw new DataAccessException("Error occurred while creating the persons table");
        }
    }
}
