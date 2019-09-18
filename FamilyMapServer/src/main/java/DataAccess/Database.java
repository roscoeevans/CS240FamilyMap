package DataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class for global database operations.
 * Created by westenm on 2/4/19.
 */

public class Database {
    private Connection conn;

    static {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * opens connection to database.
     * @return the opened connection.
     * @throws DataAccessException
     */
    public Connection openConnection() throws DataAccessException {
        try {
            final String CONNECTION_URL = "jdbc:sqlite:familymap.sqlite";

            conn = DriverManager.getConnection(CONNECTION_URL);

            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }

        return conn;
    }

    /**
     * Closes connection to database.
     * @param commit boolean that determines whether or not to commit to changes made to database.
     * @throws DataAccessException
     */
    public void closeConnection(boolean commit) throws DataAccessException {
        try {
            if (commit) {
                conn.commit();
            } else {
                conn.rollback();
            }

            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    /**
     * Creates all tables for the database.
     * @throws DataAccessException
     */
    public void createTables() throws DataAccessException {
        openConnection();
        try {
            Statement stmt = conn.createStatement();
            String sql = "Create table if not exists AuthTokens (\n" +
                    "\tauthToken varchar(255) not null,\n" +
                    "\tuserName varchar(255) not null,\n" +
                    "\tforeign key (username) references user(username)\n" +
                    ");";
            stmt.executeUpdate(sql);

            sql = "Create table if not exists Events (\n" +
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
                    "\tforeign key (personID) references person(personID)\n" +
                    ");";
            stmt.executeUpdate(sql);

           sql = "Create table if not exists Persons (\n" +
                   "\tpersonID varchar(255) not null primary key,\n" +
                   "\tdescendant varchar(255) not null,\n" +
                   "\tfirstName varchar(255) not null,\n" +
                   "\tlastName varchar(255) not null,\n" +
                   "\tgender varchar(255) not null,\n" +
                   "\tfather varchar(255),\n" +
                   "\tmother varchar(255),\n" +
                   "\tspouse varchar(255),\n" +
                   "\tforeign key (descendant) references user(username)\n" +
                   ");";
            stmt.executeUpdate(sql);

           sql = "Create table if not exists Users (\n" +
                   "\tuserName varchar(255) not null primary key,\n" +
                   "\tpassword varchar(255) not null,\n" +
                   "\temail varchar(255) not null,\n" +
                   "\tfirstName varchar(255) not null,\n" +
                   "\tlastName varchar(255) not null,\n" +
                   "\tgender varchar(255) not null,\n" +
                   "\tpersonID varchar(255) not null,\n" +
                   "\tforeign key (personID) references person(personID)\n" +
                   ");";
            stmt.executeUpdate(sql);
            closeConnection(true);
        } catch (DataAccessException e) {
            closeConnection(false);
            throw e;
        } catch (SQLException e) {
            closeConnection(false);
            throw new DataAccessException("SQL Error encountered while creating tables");
        }
    }

    /**
     * Clears all tables.
     * @throws DataAccessException
     */
    public void clearTables() throws DataAccessException
    {
        try {
            AuthTokenDAO authTokens = new AuthTokenDAO();
            EventDAO events = new EventDAO();
            PersonDAO persons = new PersonDAO();
            UserDAO users = new UserDAO();

            authTokens.clear();
            events.clear();
            persons.clear();
            users.clear();

        } catch (DataAccessException e) {
            closeConnection(false);
            e.printStackTrace();
            throw e;
        }
    }
}
