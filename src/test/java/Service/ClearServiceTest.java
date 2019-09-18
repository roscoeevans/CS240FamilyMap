package Service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import DataAccess.DataAccessException;
import DataAccess.Database;
import Response.ClearResponse;

import static org.junit.Assert.*;

public class ClearServiceTest {
    Database db;

    @Before
    public void setUp() throws Exception {
        db = new Database();
        db.createTables();
    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }

    @Test
    public void clear() {
        ClearService service = new ClearService();
        ClearResponse actual;
        ClearResponse expected = new ClearResponse("Clear succeeded.");
        try {
            actual = service.clear();
        } catch (DataAccessException ex) {
            actual = new ClearResponse("Internal Server error.");
        }

        assertEquals(actual.getMessage(), expected.getMessage());
    }
}