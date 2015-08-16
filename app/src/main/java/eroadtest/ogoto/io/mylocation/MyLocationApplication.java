package eroadtest.ogoto.io.mylocation;

import android.app.Application;
import android.util.Log;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;

import eroadtest.ogoto.io.mylocation.restapi.GeoNamesApi;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * @author GT
 */
public class MyLocationApplication extends Application {
    private static String TAG = MyLocationApplication.class.getSimpleName();
    private static MyLocationApplication currentInstance = null;
    private RestAdapter restAdapter;
    public GeoNamesApi geoNamesApi;

    @Override
    public void onCreate() {
        super.onCreate();
        currentInstance = this;
        init();
    }

    public static synchronized MyLocationApplication getCurrentInstance(){
        return currentInstance;
    }

    private void init(){
        int cacheSize = 1024 * 1024; // 1 MiB
        //Setting some cache for http requests
        File cacheFile = new File(getCacheDir(), MyLocationConstant.HTTP_CACHE_DIR_NAME);
        Cache cache = null;
        try {
            cache = new Cache(cacheFile, cacheSize);
        } catch (IOException e) {
            Log.e(TAG, "Cache directory creation failed");
        }

        OkHttpClient client = new OkHttpClient();
        if(cache != null) client.setCache(cache);
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(MyLocationConstant.GEONAME_API_HOSE)
                .setClient(new OkClient(client));
        restAdapter = builder.build();
        geoNamesApi = restAdapter.create(GeoNamesApi.class);
    }



}
