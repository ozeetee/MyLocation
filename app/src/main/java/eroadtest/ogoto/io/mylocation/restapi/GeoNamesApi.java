package eroadtest.ogoto.io.mylocation.restapi;

import eroadtest.ogoto.io.mylocation.models.TimeZone;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @author GT
 */
public interface GeoNamesApi {

    // Example url : http://api.geonames.org/timezoneJSON?formatted=true&lat=40.75649&lng=-73.98626&username=demo
    @GET("/timezoneJSON")
    void getTimeZoneJson(@Query("formatted") boolean formatted, @Query("lat") double lat, @Query("lng") double lng, @Query("username") String username, Callback<TimeZone> cb);

}