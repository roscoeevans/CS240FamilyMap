package Service;

import DataAccess.DataAccessException;
import DataAccess.Database;
import Response.ClearResponse;

public class ClearService {
    public ClearService() {
    }

    public ClearResponse clear() throws DataAccessException {
        Database db = new Database();

        boolean commit = false;
        ClearResponse response;

        try {
            db.clearTables();
            response = new ClearResponse("Clear succeeded.");
            commit = true;
            return response;
        }

        catch (DataAccessException ex) {
            response = new ClearResponse("Internal server error occured while clearing tables.");
            ex.printStackTrace();
            db.closeConnection(commit);
            return response;
        }
    }
}
