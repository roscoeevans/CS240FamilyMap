package DataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.AuthToken;

/**
 * Data Access Object class for the Authorization Token model class. Data Access allows us to access the stored information in the database.
 */

public class AuthTokenDAO {
    public Connection conn;
    public Database db;

    public AuthTokenDAO() {
        this.db = new Database();
        this.conn = null;
    }
    public AuthTokenDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Clears all authorization tokens
     * @throws DataAccessException if the authorization tokens are not properly accessed
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM AuthTokens";
        try {
            this.conn = db.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            int deletedRows = stmt.executeUpdate();
            db.closeConnection(true);
        }
        catch(SQLException e) {
            db.closeConnection(false);
            e.printStackTrace();
            throw new DataAccessException("Error occurred while clearing AuthTokens table");
        }
    }

    /**
     * creates empty table of authTokens.
     * @throws DataAccessException
     */
   public void createAuthTokenTable() throws DataAccessException {
       String sql = "Create table if not exists AuthTokens (\n" +
               "\tauthToken varchar(255) not null,\n" +
               "\tuserName varchar(255) not null,\n" +
               "\tforeign key (userName) references user(userName)\n" +
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
           throw new DataAccessException("Error occurred while creating the AuthTokens table");
       }
   }

    /**
     * Inserts authToken into AuthTokens table/
     * @param token token to be inserted
     * @return success or failure
     * @throws DataAccessException
     */
   public boolean insert(AuthToken token) throws DataAccessException {
       boolean commit = true;
       String sql = "INSERT INTO AuthTokens (authToken, userName) VALUES(?,?)";
       //if(findAuthToken(token.getUserName()) == null) {
           try {
               this.conn = db.openConnection();
               PreparedStatement stmt = conn.prepareStatement(sql);
               stmt.setString(1, token.getAuthToken());
               stmt.setString(2, token.getUserName());
               stmt.executeUpdate();
               db.closeConnection(true);
           } catch (SQLException e) {
               db.closeConnection(false);
               e.printStackTrace();
               throw new DataAccessException("Error occurred while inserting a token into the authtokens database");
           }
      /* else {
           commit = false;
           throw new DataAccessException("Error token already in the table");
       }*/

       return commit;
   }

    /**
     * Finds the authorization token of a specific user given the user's username
     * @param username the username of the user who's authorization token we will return
     * @return the authorization token of the given user
     * @throws DataAccessException if the authorization tokens are not properly accessed
     */
    public AuthToken findAuthToken(String username) throws DataAccessException {
        AuthToken token = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthTokens WHERE userName = ?;";
        try {
            this.conn = db.openConnection(); // offending line
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next() == true) {
                token = new AuthToken(rs.getString("authToken"), rs.getString("userName"));
                db.closeConnection(true);
                return token;
            }
            else {
                db.closeConnection(true);
            }
        } catch (SQLException e) {
            db.closeConnection(false);
            e.printStackTrace();
            throw new DataAccessException("Error encountered while trying to find an auth token");
        }
        return null;
    }

    /**
     * finds an AuthToken object by searching its authToken string.
     * @param authToken token to be inserted.
     * @return found token. returns null if not found
     * @throws DataAccessException
     */
    public AuthToken findByAuthToken(String authToken) throws DataAccessException {
        AuthToken token = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthTokens WHERE authToken = ?;";
        try {
            this.conn = db.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, authToken);
            rs = stmt.executeQuery();
            if (rs.next() == true) {
                token = new AuthToken(rs.getString("authToken"), rs.getString("userName"));
                db.closeConnection(true);
                return token;
            }
            else {
                db.closeConnection(true);
            }
        } catch (SQLException e) {
            db.closeConnection(false);
            e.printStackTrace();
            throw new DataAccessException("Error encountered while trying to find an auth token");
        }
        return null;
    }


}
