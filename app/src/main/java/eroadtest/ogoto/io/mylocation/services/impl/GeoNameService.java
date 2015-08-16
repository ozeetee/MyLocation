package eroadtest.ogoto.io.mylocation.services.impl;

import eroadtest.ogoto.io.mylocation.MyLocationApplication;
import eroadtest.ogoto.io.mylocation.models.TimeZone;
import eroadtest.ogoto.io.mylocation.restapi.GeoNamesApi;
import eroadtest.ogoto.io.mylocation.services.IGeoNameService;
import retrofit.Callback;

/**
 * @author GT
 */
public class GeoNameService implements IGeoNameService{

    private final GeoNamesApi geoNamesApi;

    public GeoNameService() {
        this.geoNamesApi = MyLocationApplication.getCurrentInstance().geoNamesApi;
    }

    public void getTimeZoneJson(double lat,double lng,Callback<TimeZone> cb){
        geoNamesApi.getTimeZoneJson(true,lat,lng,"demo",cb);
    }
}
