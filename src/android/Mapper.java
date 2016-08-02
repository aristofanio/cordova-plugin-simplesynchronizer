package ag.cordova.plugin.simplesynchronizer;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Mapper {
    private final SQLiteDatabase database;

    private void remove(String action){
        database.delete(DatastoreHelper.DBTABLE_MAPPER, "action='" + action + "'", null);
    }

    public Mapper(SQLiteDatabase database){
        this.database = database;
    }

    public void put(String action, Value value) throws MapperException{
        //values
        ContentValues values = new ContentValues();
        values.put("action", action);
        values.put("verb", value.getVerb());
        values.put("path", value.getPath());
        //if exists, remove
        Value old = get(action);
        if (old != null && old.equals(value)) remove(action);
        //inser new
        database.insert(DatastoreHelper.DBTABLE_MAPPER, null, values);
    }

    public Value get(String action) throws MapperException{
        //
        Value result = null;
        Cursor c = null;
        //
        try {
            //
            String[] fields = new String[]{"verb", "path"};
            String filter = ("action = '" + action + "'");
            //
            c = database.query(DatastoreHelper.DBTABLE_MAPPER,
                    fields, filter, null, null, null, null);
            //
            if (c.getCount() > 0) {
                c.moveToFirst();
                String verb = c.getString(0);
                String path = c.getString(1);
                result = new Value(verb, path);
            }
        }
        catch (SQLException e) {
            Log.v("SIMPLESYNCHRONIZER", DatastoreHelper.DBTABLE_MAPPER, e);
            throw new MapperException(e.getMessage());
        }
        finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
        return result;
    }

    public void clean() throws MapperException{
        database.delete(DatastoreHelper.DBTABLE_MAPPER, null, null);
    }

    public static class Value{
        private final String verb;
        private final String path;

        public Value(String verb, String path){
            this.verb = verb;
            this.path = path;
        }

        public String getVerb() {
            return verb;
        }

        public String getPath() {
            return path;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Value value = (Value) o;
            if (verb != null ? !verb.equals(value.verb) : value.verb != null)
                return false;
            return path != null ? path.equals(value.path) : value.path == null;

        }

        @Override
        public int hashCode() {
            int result = verb != null ? verb.hashCode() : 0;
            result = 31 * result + (path != null ? path.hashCode() : 0);
            return result;
        }
    }


}
