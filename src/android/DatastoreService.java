package ag.cordova.plugin.simplesynchronizer;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class DatastoreService {
    private final SQLiteDatabase database;

    public DatastoreService(SQLiteDatabase database){
        this.database = database;
    }

    public void delete(String action) throws DatastoreServiceException{
        String whereClause = "action = '" + action + "'";
        database.delete(DatastoreHelper.DBTABLE_REQUEST, whereClause, null);
    }

    public void insert(String action, String data) throws DatastoreServiceException{
        try {
            //if exists, delete
            if (select(action) != null) {
                delete(action);
            }
            //insert again
            ContentValues values = new ContentValues();
            values.put("action", action);
            values.put("data", data);
            //
            database.insert(DatastoreHelper.DBTABLE_REQUEST, null, values);
        }
        catch (SQLException e){
            Log.v("SIMPLESYNCHRONIZER", DatastoreHelper.DBTABLE_REQUEST, e);
            throw new DatastoreServiceException(e.getMessage());
        }
    }

    public JSONObject select(String action) throws DatastoreServiceException{
        //
        JSONObject result = null;
        Cursor c = null;
        //
        try {
            //
            String[] fields = new String[]{"data"};
            String filter = ("action = '" + action + "'");
            //
            c = database.query(DatastoreHelper.DBTABLE_REQUEST, fields, filter,
                    null, null, null, null);
            //
            if (c.getCount() > 0) {
                c.moveToFirst();
                String data = c.getString(0);
                if (data != null) {
                    result = new JSONObject(data);
                }
            }
        }
        catch (JSONException e) {
            Log.v("SIMPLESYNCHRONIZER", DatastoreHelper.DBTABLE_REQUEST, e);
            throw new DatastoreServiceException(e.getMessage());
        }
        catch (SQLException e) {
            Log.v("SIMPLESYNCHRONIZER", DatastoreHelper.DBTABLE_REQUEST, e);
            throw new DatastoreServiceException(e.getMessage());
        }
        finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
        return result;
    }
}
