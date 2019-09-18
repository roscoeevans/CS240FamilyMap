package DataAccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import Model.Event;

import static org.junit.Assert.*;

public class EventDAOTest {
    private Event event;
    private Connection conn;
    private Database db;
    private EventDAO dao;
    private Event event2;

    @Before
    public void setUp() throws Exception {
        event = new Event("evenid", "mydesc", "perid", 0.0, 0.0, "usa", "sandy", "typoo", 2019);
        event2 = new Event("eved", "desc", "periood", 1.0, 1.0, "uta", "draper", "typeo", 2018);
        db = new Database();
        conn = db.openConnection();
        dao = new EventDAO(conn);
        dao.createEventTable();
    }

    @After
    public void tearDown() throws Exception {
        event = null;
        event2 = null;
        db.closeConnection(true);
    }

    @Test
    public void insert() {
        try {
            dao.insert(event);
            Event ev = dao.findEvent(event.getEventID());
            if (ev == null) {
                fail("Event not inserted.");
            }
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            fail("Data Access exception thrown while testing insert on Events");
        }
    }

    @Test
    public void find() {
        try {
            Event ev = dao.findEvent(event.getEventID());
            assertEquals(ev.getEventID(), event.getEventID());
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            fail("Data Access exception thrown while testing find on Events");
        }
    }

    @Test
    public void clear() {
        try {
            dao.clear();
        }
        catch(DataAccessException e) {
            e.printStackTrace();
            fail("Data Access exception thrown while testing clear on Events");
        }
    }

    @Test
    public void insert2() {
        try {
            dao.insert(event2);
            Event ev = dao.findEvent(event.getEventID());
            if (ev == null) {
                fail("event found is null");
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            fail("Data Access exception thrown while testing insert2 on events");
        }
    }

    @Test
    public void find2() {
        try {
            //dao.insert(event2);
            Event ev = dao.findEvent(event.getEventID());
            assertEquals(ev.getEventID(), event2.getEventID());
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            fail("Data Access exception thrown while testing find2 on events");
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
            fail("Data Access exception thrown while testing clear2 on events");
        }
    }
}