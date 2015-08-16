package eroadtest.ogoto.io.mylocation.services;

import eroadtest.ogoto.io.mylocation.models.TimeZone;
import retrofit.Callback;

/**
 * @author GT
 */
public interface IGeoNameService {
    void getTimeZoneJson(double lat,double lng, Callback<TimeZone> cb);
}
