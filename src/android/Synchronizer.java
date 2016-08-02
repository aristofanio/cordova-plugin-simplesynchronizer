package ag.cordova.plugin.simplesynchronizer;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class Synchronizer {
    private final DatastoreService service;
    private final Mapper mapper;

    public Synchronizer(DatastoreService service, Mapper mapper){
        this.service = service;
        this.mapper = mapper;
    }

    public void call(String action) throws SynchronizerException {
        try {
            //
            Mapper.Value value = mapper.get(action);
            //
            URL url = new URL(value.getPath());
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod(value.getVerb());
            connection.setRequestProperty("Content-type", "application/json");
            InputStream inputStream = connection.getInputStream();
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(streamReader);
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null){
                sb.append(line);
            }
            streamReader.close();
            connection.disconnect();
            String data = sb.toString();
            //inserir resultado em database
            if (!"".equals(data) && data.startsWith("{") && data.endsWith("}")){
                service.insert(action, sb.toString());
            }
            else {
                Log.v("SIMPLESYNCHRONIZER", "Error on Synchronizer.call(): "+
                        "Request result no wrapped with {}."
                );
            }
        }
        catch (ProtocolException e){
            Log.v("SIMPLESYNCHRONIZER", "error on Synchronizer.call()", e);
            throw new SynchronizerException(e.getMessage());
        }
        catch (IOException e){
            Log.v("SIMPLESYNCHRONIZER", "error on Synchronizer.call()", e);
            throw new SynchronizerException(e.getMessage());
        }
        catch (MapperException e) {
            Log.v("SIMPLESYNCHRONIZER", "error on Synchronizer.call()", e);
            throw new SynchronizerException(e.getMessage());
        }
        catch (DatastoreServiceException e) {
            Log.v("SIMPLESYNCHRONIZER", "error on Synchronizer.call()", e);
            throw new SynchronizerException(e.getMessage());
        }
    }

}
