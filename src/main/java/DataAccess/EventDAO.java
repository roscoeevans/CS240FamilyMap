package DataAccess;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.crypto.Data;

import Model.Event;

/**
 * Data Access Object class for the Event model class. Data Access allows us to access the stored information in the database.
 */

public class EventDAO {
    private Connection conn;
    private Database db;

    public EventDAO(Connection conn) {
        this.conn = conn;
    }

    public EventDAO() {
        this.db = new Database();
        this.conn = null;
    }

    /**
     * creates a table of events.
     * @throws DataAccessException
     */
    public void createEventTable() throws DataAccessException  {
        String sql = "Create table if not exists Events (\n" +
        "\teventID varchar(255) not null primary key,\n" +
        "\tdescendant varchar(255) not null,\n" +
        "\tpersonID varchar(255) not null,\n" +
        "\tlatitude float not null,\n" +
        "\tlongitude float not null,\n" +
        "\tcountry varchar(255) not null,\n" +
        "\tcity varchar(255) not null,\n" +
        "\teventType varchar(255) not null,\n" +
        "\tyear integer not null,\n" +
        "\tforeign key (descendant) references user(userName),\n" +
        "\tforeign key (personID) references person(personID)\n" + ");";
        try {
            this.conn = this.db.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.execute();
            this.db.closeConnection(true);
        }
        catch(SQLException e) {
            db.closeConnection(false);
            e.printStackTrace();
            throw new DataAccessException("Error occurred while creating the persons table");
        }
    }

    /**
     * Clears all events in the database
     * @throws DataAccessException if the events in the database are not properly accessed
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Events";
        try {
            this.conn = db.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            int deletedRows = stmt.executeUpdate();
            db.closeConnection(true);
        }
        catch(SQLException e) {
            db.closeConnection(false);
            e.printStackTrace();
            throw new DataAccessException("Error occurred while clearing Events table");
        }
    }

    /**
     * Insert a new event in the database
     * @param event the event to be inserted
     * @return true or false, dependent upon the success of the insertion
     * @throws DataAccessException if the events in the database are not properly accessed
     */
    public boolean insert(Event event) throws DataAccessException {
        boolean commit = true;
        String sql = "INSERT INTO Events (eventID, descendant, personID, latitude, longitude, country, city, eventType, year) VALUES (?,?,?,?,?,?,?,?,?)";
        if(findEvent(event.getEventID()) == null) {
            try {
                this.conn = db.openConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, event.getEventID());
                stmt.setString(2, event.getDescendant());
                stmt.setString(3, event.getPersonID());
                stmt.setDouble(4, event.getLatitude());
                stmt.setDouble(5, event.getLongitude());
                stmt.setString(6, event.getCountry());
                stmt.setString(7, event.getCity());
                stmt.setString(8, event.getEventType());
                stmt.setInt(9, event.getYear());
                stmt.executeUpdate();
                db.closeConnection(true);
            } catch (SQLException e) {
                db.closeConnection(false);
                commit = false;
                e.printStackTrace();
                System.out.println("Invalid data");
            }
        }
        else {
            commit = false;
            System.out.println("This eventID already exists in the table.");
        }

        return commit;
    }

    /**
     * Finds a specfic event given an eventID
     * @param eventID the ID of the event to be found
     * @return the event if the search is successful, null otherwise
     * @throws DataAccessException if the events in the database are not properly accessed
     */
    public Event findEvent(String eventID) throws DataAccessException {
        Event event = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE eventID = ?;";
        try {
            this.conn = db.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next() == true) {
                event = new Event(rs.getString("eventID"), rs.getString("descendant"), rs.getString("personID"), rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getString("country"), rs.getString("city"), rs.getString("eventType"), rs.getInt("year"));
                db.closeConnection(true);
                return event;
            } else {
                db.closeConnection(true);
                return null;
            }
        } catch (SQLException e) {
            db.closeConnection(false);
            e.printStackTrace();
            throw new DataAccessException("Error encountered while trying to find an event");
        }
    }

    /**
     * Deletes all events that contain a specific descendant.
     * @param descendant the descendant whose events will be deleted.
     * @throws DataAccessException
     */
    public void deleteEvents(String descendant) throws DataAccessException {
        String sql = "DELETE FROM Events WHERE descendant = ?;";
        try {
            this.conn = db.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, descendant);
            stmt.executeUpdate();
            db.closeConnection(true);

        } catch (SQLException e) {
            db.closeConnection(false);
            e.printStackTrace();
            throw new DataAccessException("Error encountered while attempting deleteEvents");
        }
    }

    /**
     * Gets an arraylist of Events that have a certain descendant
     * @param descendant descandant to be searched for.
     * @return
     * @throws DataAccessException
     */
    public ArrayList<Event> findAllEvents(String descendant) throws DataAccessException {
        ArrayList<Event> events = new ArrayList<>();

        Event event;
        ResultSet rs;
        String sql = "SELECT * FROM Events WHERE descendant = ?;";
        try {
            this.conn = db.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, descendant);
            rs = stmt.executeQuery();
            while (rs.next() == true) {
                event = new Event(rs.getString("eventID"), rs.getString("descendant"), rs.getString("personID"), rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getString("country"), rs.getString("city"), rs.getString("eventType"), rs.getInt("year"));
                events.add(event);
            }
                db.closeConnection(true);

        } catch (SQLException e) {
            db.closeConnection(false);
            e.printStackTrace();
            throw new DataAccessException("Error encountered while trying to find an event");
        }

        return events;
    }


}
