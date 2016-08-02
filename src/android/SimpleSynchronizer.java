package ag.cordova.plugin.simplesynchronizer;


import android.content.Context;

import org.json.JSONObject;

public interface SimpleSynchronizer {
    JSONObject request(Context ctx, String action) throws SimpleSynchronizerException;
    void register(Context ctx, String action, String verb, String url) throws SimpleSynchronizerException;
}
