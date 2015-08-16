package eroadtest.ogoto.io.mylocation;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.ButterKnife;
import butterknife.InjectView;
import eroadtest.ogoto.io.mylocation.models.TimeZone;
import eroadtest.ogoto.io.mylocation.services.IGeoNameService;
import eroadtest.ogoto.io.mylocation.services.impl.GeoNameService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity implements LocationListener {
    private static String TAG = MainActivity.class.getSimpleName();

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.vs_main_view_switcher) ViewSwitcher mMainViewSwitcher;

    private IGeoNameService geoNameService;
    private TimeZone currentTimeZone;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        setTitle("Location");
        geoNameService = new GeoNameService();
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onLocationChanged(Location loc) {
        double latitude = loc.getLatitude();
        double longitude = loc.getLongitude();
        currentLocation = loc;
        geoNameService.getTimeZoneJson(latitude, longitude, new Callback<TimeZone>() {
            @Override
            public void success(TimeZone timeZone, Response response) {
                Log.d(TAG,"TIMEZONE : " + timeZone.timezoneId);
                Log.d(TAG,"");

                MainActivity.this.currentTimeZone = timeZone;
                initilizeBubble();
                hideLoading();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG,"Time Zone fetching failed");
                Toast.makeText(MainActivity.this,"Location data fetch failed from GeoName",Toast.LENGTH_LONG).show();
                hideLoading();
            }
        });
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void initilizeBubble(){
        double latitude = currentLocation.getLatitude();
        double longitude = currentLocation.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        StringBuilder locStringBuilder = new StringBuilder("Lat :" + latitude + ", Lng :" + longitude);
        StringBuilder additionalData = new StringBuilder("TZ :");

        if(currentTimeZone != null){
            additionalData.append(currentTimeZone.timezoneId);
            additionalData.append(", Time: ");
            additionalData.append(currentTimeZone.time);
        }
        MarkerOptions markerOptions =
                new MarkerOptions()
                        .position(latLng)
                        .title(locStringBuilder.toString())
                        .snippet(additionalData.toString())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    private void showLoading(){
        if(mMainViewSwitcher.getNextView() instanceof LinearLayout){
            mMainViewSwitcher.showNext();
        }
    }

    private void hideLoading(){
        if(!(mMainViewSwitcher.getNextView() instanceof LinearLayout)){
            mMainViewSwitcher.showNext();
        }
    }


}
