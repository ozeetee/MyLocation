package eroadtest.ogoto.io.mylocation.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import eroadtest.ogoto.io.mylocation.models.TimeZone;

/**
 * @author GT
 */
public class ResponseTranslator {
    private static ResponseTranslator sharedInstance;
    private Gson plainGsonInstance;
    private Gson gson;

    private ResponseTranslator() {
        if (plainGsonInstance == null) {
            plainGsonInstance = new GsonBuilder().create();
        }
        if (gson == null) {
            gson = new GsonBuilder().create();
        }
    }

    public static synchronized ResponseTranslator getSharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new ResponseTranslator();
        }
        return sharedInstance;
    }

    public TimeZone translateTimeZone(JsonObject jsonObject){
        return gson.fromJson(jsonObject, TimeZone.class);
    }

}
