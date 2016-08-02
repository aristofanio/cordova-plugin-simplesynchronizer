package ag.cordova.plugin.simplesynchronizer;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * How Do:
 *  - wrap in thread
 *
 * Prerequisites:
 *  - <uses-permission android:name="android.permission.INTERNET" />
 *  - <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 *
 */
public class SimpleSynchronizerImpl implements SimpleSynchronizer {

    private boolean isOnline(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        //Crio o objeto netInfo que recebe as informacoes da NEtwork
        NetworkInfo netInfo = manager.getActiveNetworkInfo();
        //Se o objeto for nulo ou nao tem conectividade retorna false
        if ((netInfo != null) && (netInfo.isConnectedOrConnecting()) && (netInfo.isAvailable()))
            return true;
        else
            return false;
    }

    @Override
    public JSONObject request(Context context, String action) throws SimpleSynchronizerException {
        //setup
        DatastoreHelper helper = new DatastoreHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        DatastoreService service = new DatastoreService(database);
        Mapper mapper = new Mapper(database);
        //
        JSONObject result = null;
        //
        database.beginTransaction();
        try {
            //
            result = service.select(action);
            if (result == null && isOnline(context)) {
                Synchronizer requestor = new Synchronizer(service, mapper);
                requestor.call(action);
                result = service.select(action);
            } else if (result == null && !isOnline(context)){
                result = new JSONObject();
                result.put("success", false);
                result.put("message", "Dispositivo sem conexão com internet.");
            }
            //
            database.setTransactionSuccessful();
        }
        catch (JSONException e){
            throw new SimpleSynchronizerException("Fail on build json object.");
        }
        catch (DatastoreServiceException e){
            throw new SimpleSynchronizerException(e.getMessage());
        }
        catch (SynchronizerException e) {
            throw new SimpleSynchronizerException(e.getMessage());
        }
        finally {
            database.endTransaction();
            database.close();
        }
        //
        return result;
    }

    @Override
    public void register(Context context, String action, String verb, String url) throws SimpleSynchronizerException {
        DatastoreHelper helper = new DatastoreHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        Mapper mapper = new Mapper(database);
        database.beginTransaction();
        try {
            mapper.put(action, new Mapper.Value(verb, url));
            database.setTransactionSuccessful();
        }
        catch (MapperException e){
            throw new SimpleSynchronizerException(e.getMessage());
        }
        finally {
            database.endTransaction();
            database.close();
        }
    }


}
