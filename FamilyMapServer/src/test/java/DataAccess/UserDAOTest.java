package DataAccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import Model.User;

import static org.junit.Assert.*;

public class UserDAOTest {
    private User user;
    private Connection conn;
    private Database db;
    private UserDAO dao;
    private User userInDB;
    @Before
    public void setUp() throws Exception {
        user = new User("revan", "legendsNeverDie", "roscoe7evans@gmail.com", "Rocky", "Evans", "m", "123");
        userInDB = new User("jhunt","howlOfWolf", "jhunt24@gmail.com", "Justin", "Hunt", "m", "456");
        db = new Database();
        dao = new UserDAO();
        db.createTables();
        dao.insert(userInDB);
    }

    @After
    public void tearDown() throws Exception {
        user = null;
        userInDB = null;
        db.clearTables();
        db = null;
        dao = null;
    }

    @Test
    public void insert() {
        boolean expected = true;
        boolean actual = false;
        try {
            actual = dao.insert(user);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            assertNotEquals(actual, expected);
        }
    }

    @Test
    public void find() {
        User expected = userInDB;
        User actual = user;
        try {
            actual = dao.find(userInDB.getUserName());
            assertEquals(expected.getUserName(), actual.getUserName());
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            assertEquals(actual.getUserName(), expected.getUserName());
        }
    }

    @Test
    public void clear() {
        boolean actual = false;
        boolean expected = true;
        try {
            actual = dao.clear();

            assertEquals(actual, expected);
        }
        catch(DataAccessException e) {
            assertEquals(actual, expected);
        }
    }

    @Test
    public void find2() {
        boolean actual = true;
        boolean expected = false;

        try {
            if (dao.find(user.getUserName()) == null) {
                actual = false;
            }
            assertEquals(actual, expected);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            assertEquals(actual, expected);
        }
    }
    @Test
    public void insert2() {
        boolean actual = true;
        boolean expected = false;
        try {
            actual = dao.insert(userInDB);

            assertEquals(actual, expected);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            assertEquals(actual, expected);
        }
    }
}