package Service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import DataAccess.Database;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import Model.Person;
import Model.User;
import Response.FillResponse;

import static org.junit.Assert.*;

public class FillServiceTest {
    Database db;
    User user;

    @Before
    public void setUp() throws Exception {
        db = new Database();
        db.createTables();
        user = new User("rocky37", "justinsucks", "myemail@gmail.com", "rocky", "evans", "m", "rockysID");
        Person person = new Person("rockysID", "rocky37", "rocky", "evans", "m");
        UserDAO userDao = new UserDAO();
        PersonDAO personDao = new PersonDAO();
        personDao.insert(person);
        userDao.insert(user);
    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void fill() {
        FillService service = new FillService();
        FillResponse actual = service.fill(user.getUserName(), 4);
        FillResponse expected = new FillResponse("Successfully added 31 persons and 91 events to the database.");

        assertEquals(expected.getMessage(), actual.getMessage());
    }

    @Test
    public void fill2() {
        FillService service = new FillService();
        FillResponse actual = service.fill("username_not_in_db", 4);
        FillResponse expected = new FillResponse("Successfully added 31 persons and 91 events to the database.");

        assertNotEquals(expected.getMessage(), actual.getMessage());
    }

}