package ag.cordova.plugin.simplesynchronizer;

import android.content.Context;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Simple Synchronizer (client-server-readonly) on RESTful-based server.
 */
public class SimpleSynchronizerPlugin extends CordovaPlugin {
    public static final String ACTION_REQUEST = "REQUEST";
    public static final String ACTION_REGISTER = "REGISTER";

    private JSONObject request(String action) throws SimpleSynchronizerException{
        Context ctx = cordova.getActivity();
        SimpleSynchronizer simpleSynchronizer = new SimpleSynchronizerImpl();
        return simpleSynchronizer.request(ctx, action);
    }

    private void register(String action, String method, String path) throws SimpleSynchronizerException{
        Context ctx = cordova.getActivity();
        SimpleSynchronizer simpleSynchronizer = new SimpleSynchronizerImpl();
        simpleSynchronizer.register(ctx, action, method, path);
    }

    @Override
    public boolean execute(String jsaction, JSONArray args, CallbackContext callbackContext)
            throws JSONException {
        try {
            if (ACTION_REQUEST.equals(jsaction)){
                //args
                String action = args.getString(0);
                //request
                JSONObject json = request(action);
                //callback
                callbackContext.success(json);
                //
                return true;
            }
            else if (ACTION_REGISTER.equals(jsaction)){
                //args
                String action = args.getString(0);
                String method = args.getString(1);
                String path = args.getString(2);
                //register
                register(action, method, path);
                //callback
                callbackContext.success();
                //
                return true;
            }
        }
        catch(SimpleSynchronizerException e){
            JSONObject json = new JSONObject();
            json.put("success", false);
            json.put("message", e.getMessage());
            callbackContext.error(json);
        }
        //
        return false;
    }


}
