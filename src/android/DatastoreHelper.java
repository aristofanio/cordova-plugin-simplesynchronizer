package ag.cordova.plugin.simplesynchronizer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatastoreHelper extends SQLiteOpenHelper {
    private static final int DBVERSION = 3;
    private static final String DBNAME = "dbsync";
    public static final String DBTABLE_REQUEST = "tb_request";
    public static final String DBTABLE_MAPPER = "tb_mapper";

    public DatastoreHelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //
        String sql_req = "CREATE TABLE " + DBTABLE_REQUEST +  "(" +
                "action TEXT UNIQUE NOT NULL, " +
                "data TEXT NOT NULL, " +
                "PRIMARY KEY (action));";
        db.execSQL(sql_req);
        //
        String sql_map = "CREATE TABLE " + DBTABLE_MAPPER +  "(" +
                "action TEXT UNIQUE NOT NULL, " +
                "verb TEXT NOT NULL, " +
                "path TEXT NOT NULL, " +
                "PRIMARY KEY (action));";
        db.execSQL(sql_map);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBTABLE_REQUEST);
        db.execSQL("DROP TABLE IF EXISTS " + DBTABLE_MAPPER);
        onCreate(db);
    }

}
