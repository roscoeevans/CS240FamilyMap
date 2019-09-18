package DataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.User;

/**
 * Data Access Object class for the User model class. Data Access allows us to access the stored information in the database.
 */

public class UserDAO {
    private Connection conn;
    private Database db;

    public UserDAO() {
        this.db = new Database();
        this.conn = null;
    }
    public UserDAO(Connection conn) {
        this.conn = conn;
    }
    /**
     * Clears all users in the database.
     * @throws DataAccessException if the users in the database are not properly accessed
     */
    public boolean clear() throws DataAccessException {
        boolean success = false;
        String sql = "DELETE FROM Users";
        try {
            this.conn = db.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            int deletedRows = stmt.executeUpdate();
            success = true;
            db.closeConnection(success);
        }
        catch(SQLException e) {
            db.closeConnection(success);
            e.printStackTrace();
            throw new DataAccessException("Error occurred while clearing Users table");
        }
        return success;
    }

    /**
     * Inserts a user into the database
     * @param user the user to be inserted
     * @return a boolean true or false, dependent upon the success of the insert
     * @throws DataAccessException if the users in the database are not properly accessed
     */
    public boolean insert(User user) throws DataAccessException {
        boolean commit = true;
        String sql = "INSERT INTO Users (userName, password, email, firstName, lastName, gender, personID) VALUES(?,?,?,?,?,?,?)";
        if(find(user.getUserName()) == null) {
            try {
                this.conn = db.openConnection();
                user.validate();
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, user.getUserName());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getFirstName());
                stmt.setString(5, user.getLastName());
                stmt.setString(6, user.getGender());
                stmt.setString(7, user.getPersonID());
                stmt.executeUpdate();
                db.closeConnection(true);

            } catch (SQLException e) {
                db.closeConnection(false);
                System.out.println("Invalid data");
                return false;
            }
        }
        else {
            commit = false;
            System.out.println("Username already taken.");
        }

        return commit;
    }

    /**
     * Finds a user in the database given a username
     * @param userName username to be found
     * @return the user if found, null if not found
     * @throws DataAccessException if the users in the database are not properly accessed
     */
    public User find(String userName) throws DataAccessException {
        User user = null;
        ResultSet rs = null;
        if (userName != "") {
            String sql = "SELECT * FROM Users WHERE userName = ?;";
            try {
                this.conn = db.openConnection();
                PreparedStatement stmt = conn.prepareStatement(sql); // offending line
                stmt.setString(1, userName);
                rs = stmt.executeQuery();
                if (rs.next() == true) {
                    user = new User(rs.getString("userName"), rs.getString("password"), rs.getString("email"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"), rs.getString("personID"));
                    db.closeConnection(true);
                    return user;
                } else {
                    db.closeConnection(true);
                    return null;
                }
            } catch (SQLException e) {
                db.closeConnection(false);
                e.printStackTrace();
                throw new DataAccessException("Error encountered while trying to a user");
            }
        }
        return null;
    }

    /**
     * creates a new table of users.
     * @throws DataAccessException for bad data access
     */
    public void createUsersTable() throws DataAccessException {
        String sql = "Create table if not exists Users (\n" +
                "\tuserName varchar(255) not null primary key,\n" +
                "\tpassword varchar(255) not null,\n" +
                "\temail varchar(255) not null,\n" +
                "\tfirstName varchar(255) not null,\n" +
                "\tlastName varchar(255) not null,\n" +
                "\tgender varchar(255) not null,\n" +
                "\tpersonID varchar(255) not null,\n" +
                "\tforeign key (personID) references person(personID)\n" +
                ");";
        try {
            this.conn = db.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.execute();
            db.closeConnection(true);
        }
        catch(SQLException e) {
            db.closeConnection(false);
            e.printStackTrace();
            throw new DataAccessException("Error occurred while creating the Users table");
        }
    }


}
