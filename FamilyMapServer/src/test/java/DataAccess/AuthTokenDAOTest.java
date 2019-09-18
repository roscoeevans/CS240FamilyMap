package DataAccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import Model.AuthToken;

import static org.junit.Assert.*;

public class AuthTokenDAOTest {
    private AuthToken authToken;
    private Connection conn;
    private Database db;
    private AuthTokenDAO dao;
    private AuthToken authToken2;

    @Before
    public void setUp() throws Exception {
        authToken = new AuthToken("123", "revan");
        authToken2 = new AuthToken("456","jhunt");
        db = new Database();
        conn = db.openConnection();
        dao = new AuthTokenDAO(conn);
        dao.createAuthTokenTable();
    }

    @After
    public void tearDown() throws Exception {
        authToken = null;
        authToken2 = null;
        db.closeConnection(true);
    }

    @Test
    public void insert() {
        try {
            dao.insert(authToken);
            AuthToken token = dao.findAuthToken(authToken.getUserName());
            if (token == null) {
                fail("AuthToken found is null");
            }
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            fail("Data Access exception thrown while testing insertAuthToken");
        }
    }

    @Test
    public void find() {
        try {
            //dao.insertAuthToken(authToken2);
            AuthToken at = dao.findAuthToken(authToken.getUserName());
            assertEquals(at.getUserName(), authToken.getUserName());
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            fail("Data Access exception thrown while testing find authtoken");
        }
    }

    @Test
    public void clear() {
        try {
            dao.clear();
        }
        catch(DataAccessException e) {
            e.printStackTrace();
            fail("Data Access exception thrown while testing clear on AuthTokens");
        }
    }

    @Test
    public void insert2() {
        try {
            dao.insert(authToken2);
            AuthToken at = dao.findAuthToken(authToken.getUserName());
            if (at == null) {
                fail("authtoken found is null");
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            fail("Data Access exception thrown while testing insert2 on authtoken");
        }
    }

    @Test
    public void find2() {
        try {
            //dao.insertAuthToken(authToken2);
            AuthToken at = dao.findAuthToken(authToken.getUserName());
            assertEquals(at.getUserName(), authToken2.getUserName());
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            fail("Data Access exception thrown while testing find person");
        }
    }

    @Test
    public void clear2() {
        try {
            dao.clear();
            dao.clear();
        }
        catch(DataAccessException e) {
            e.printStackTrace();
            fail("Data Access exception thrown while testing clear2 on AuthTokens");
        }
    }

}