package DataAccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import Model.Person;

import static org.junit.Assert.*;

public class PersonDAOTest {
    private Person person;
    private Database db;
    private PersonDAO dao;
    private Person personInDB;

    @Before
    public void setUp() throws Exception {
        person = new Person("123", "revan", "Rocky", "Evans", "m", "Charlie", "Debbie", "tbd");
        personInDB = new Person("456","jhunt", "Justin", "Hunt", "m", "Truman", "Linda", "Mary");
        db = new Database();
        dao = new PersonDAO();
        db.createTables();
    }

    @After
    public void tearDown() throws Exception {
        person = null;
        personInDB = null;
        db = null;
    }

    @Test
    public void insert() {
        boolean expected = true;
        boolean actual = false;
        try {
            actual = dao.insert(person);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            assertNotEquals(actual, expected);
        }
    }

    @Test
    public void find() {
        Person expected = personInDB;
        Person actual = person;
        try {
            actual = dao.find(personInDB.getPersonID());
            assertEquals(expected.getPersonID(), actual.getPersonID());
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            assertEquals(actual.getPersonID(), expected.getPersonID());
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
    public void insert2() {
        boolean actual = true;
        boolean expected = false;
        try {
            actual = dao.insert(personInDB);

            assertEquals(actual, expected);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            assertEquals(actual, expected);
        }
    }

    @Test
    public void find2() {
        boolean actual = true;
        boolean expected = false;

        try {
            if (dao.find(person.getPersonID()) == null) {
                actual = false;
            }
            assertEquals(actual, expected);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            assertEquals(actual, expected);
        }
    }
}