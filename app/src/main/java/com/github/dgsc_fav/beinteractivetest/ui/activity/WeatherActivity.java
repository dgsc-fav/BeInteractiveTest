package com.github.dgsc_fav.beinteractivetest.ui.activity;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dgsc_fav.beinteractivetest.R;
import com.github.dgsc_fav.beinteractivetest.util.LocationUtils;
import com.github.dgsc_fav.beinteractivetest.weather.WeatherService;
import com.github.dgsc_fav.beinteractivetest.weather.api.model.DailyWeather;
import com.github.dgsc_fav.beinteractivetest.weather.api.model.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DG on 22.10.2016.
 */
public class WeatherActivity extends AbstractPermissionsActivity implements LocationListener, Callback<DailyWeather> {
    private static final String TAG = WeatherActivity.class.getSimpleName();

    public static String EXTRA_LOCATION = "location";

    private static final long MINIMUM_DISTANCE_FOR_UPDATES = 10;   // 5km
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 2000; // 30m

    private LocationManager mLocationManager;
    private Location        mLocation;
    private Toolbar toolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView               mMyLocationMode;

    TextView mPlaceName;
    TextView textView;

    private boolean mIsLocationFixed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

//        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mMyLocationMode = (ImageView) findViewById(R.id.image_mylocation_mode);
        mMyLocationMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // если не фиксированная локация, то обновим через LOCATION_SERVICE
                if(!mIsLocationFixed) {
                    updateLocation();
                }
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateWeatherByLocation();
            }
        });
        mPlaceName = (TextView) findViewById(R.id.place_name);
        textView = (TextView) findViewById(R.id.text);

        if(savedInstanceState == null) {
            if(getIntent() != null && getIntent().getExtras() != null) {
                mLocation = getIntent().getExtras().getParcelable(EXTRA_LOCATION);
                if(mLocation != null) {
                    // если зауск activity с установленным местоположением,
                    // то не используем LOCATION_SERVICE
                    mIsLocationFixed = true;
                }
            }
        } else {
            mLocation = savedInstanceState.getParcelable("mLocation");
            mIsLocationFixed = savedInstanceState.getBoolean("mIsLocationFixed");
        }

        updateToolbarByLocation();

        // проверка наличия permissions
        checkLocationServicePermissions();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {

            case R.id.action_cities:
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("mLocation", mLocation);
        outState.putBoolean("mIsLocationFixed", mIsLocationFixed);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mLocationManager != null) {
            //noinspection ResourceType
            mLocationManager.removeUpdates(this);
        }
    }

    @Override
    public void processWithPermissionsGranted() {
        // права даны.
        if(!mIsLocationFixed) {
            // если не фиксированная локация, то обновим через LOCATION_SERVICE
            updateLocation();
        } else {
            // обновляем UI по заданному mLocation
            onLocationChanged(mLocation);
        }
    }

    @Override
    public void processWithPermissionsDenied() {
        finishWithDialog();
    }

    private void setTitle(String title) {
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    private void updateToolbarByLocation() {
        if(mIsLocationFixed) {
            toolbar.setBackgroundResource(R.color.fixLocationCity);
//            mCollapsingToolbarLayout.setBackgroundResource(R.color.fixLocationCity);
            mMyLocationMode.setVisibility(View.GONE);
        } else {
            toolbar.setBackgroundResource(R.color.useLocationCity);
//            mCollapsingToolbarLayout.setBackgroundResource(R.color.useLocationCity);
            mMyLocationMode.setVisibility(View.VISIBLE);
        }
    }

    private void updateTitleByLocation() {
        if(mLocation != null) {
            Address address = LocationUtils.getAddress(this, mLocation);
            Log.v(TAG, "address:" + address);

            String placeName = LocationUtils.getAddressString(address);
            Log.v(TAG, "placeName:" + placeName);
            if(placeName != null) {
                mPlaceName.setText(placeName);
            } else {
                mPlaceName.setText(mLocation.toString());
            }

        }
    }



    private void updateLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        String provider = mLocationManager.getBestProvider(criteria, true);

        //noinspection ResourceType
        mLocation = mLocationManager.getLastKnownLocation(provider);

        onLocationChanged(mLocation);

        //noinspection ResourceType
        mLocationManager.requestLocationUpdates(provider,
                                                MINIMUM_TIME_BETWEEN_UPDATES,
                                                MINIMUM_DISTANCE_FOR_UPDATES,
                                                this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        updateTitleByLocation();
        updateWeatherByLocation();
    }

    private void updateWeatherByLocation() {
        if(mLocation != null) {
            //WeatherService.getDailyWeather(this, mLocation, this);
        }
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

    /**
     * @see WeatherService#getDailyWeather(Context, Location, Callback)
     */
    @Override
    public void onResponse(Call<DailyWeather> call, Response<DailyWeather> response) {


        DailyWeather dailyWeather = response.body();
        if(dailyWeather != null) {
            List list = dailyWeather.getList().get(0);


            textView.setText(String.valueOf(list.getTemp().getEve()));
        } else {
            Toast
                    .makeText(this, getString(R.string.get_weather_empty_result), Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * @see WeatherService#getDailyWeather(Context, Location, Callback)
     */
    @Override
    public void onFailure(Call<DailyWeather> call, Throwable t) {
        Toast.makeText(this,
                       getString(R.string.get_weather_error, t.getLocalizedMessage()),
                       Toast.LENGTH_LONG).show();
    }
}
