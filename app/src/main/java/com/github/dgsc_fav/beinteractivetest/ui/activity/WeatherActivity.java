package com.github.dgsc_fav.beinteractivetest.ui.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.github.dgsc_fav.beinteractivetest.R;
import com.github.dgsc_fav.beinteractivetest.provider.WeatherProvider;
import com.github.dgsc_fav.beinteractivetest.util.LocationUtils;
import com.github.dgsc_fav.beinteractivetest.util.TimeUtils;
import com.github.dgsc_fav.beinteractivetest.weather.api.Consts;
import com.github.dgsc_fav.beinteractivetest.weather.api.model.CurrentWeather;
import com.github.dgsc_fav.beinteractivetest.weather.api.model.DailyWeather;
import com.github.dgsc_fav.beinteractivetest.weather.api.model.Temp;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by DG on 22.10.2016.
 */
public class WeatherActivity extends AbstractPermissionsActivity implements LocationListener, WeatherProvider.WeatherCallback {
    private static final String TAG = WeatherActivity.class.getSimpleName();

    public static String EXTRA_LOCATION = "location";

    private static final long MINIMUM_DISTANCE_FOR_UPDATES = 10;   // 5km
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 2000; // 30m

    private LocationManager mLocationManager;
    private Location mLocation;

    private Toolbar mToolbar;
    private ImageView mRefreshLocation;
    private ImageView mRefreshWeather;
    private TextView mPlaceName;
    private TextView mWeatherDate;
    private ImageView mWeatherNowImage;
    private TextView mWeatherNowValue;
    private TextView mWeatherMornValue;
    private TextView mWeatherDayValue;
    private TextView mWeatherEveValue;
    private TextView mWeatherNightValue;

    // флаг, что локацию нельзя менять - жёстко установленныое расположение
    private boolean mIsLocationFixed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        mRefreshLocation = (ImageView) findViewById(R.id.image_refresh_location);
        mRefreshLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // если не фиксированная локация, то обновим через LOCATION_SERVICE
                if (!mIsLocationFixed) {
                    updateLocation();
                }
            }
        });
        mRefreshWeather = (ImageView) findViewById(R.id.image_refresh_weather);
        mRefreshWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRefreshWeather.setEnabled(false);
                updateWeatherByLocation();
            }
        });

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mPlaceName = (TextView) findViewById(R.id.place_name);
        mWeatherDate = (TextView) findViewById(R.id.weather_date);
        mWeatherNowImage = (ImageView) findViewById(R.id.weather_now_image);
        mWeatherNowValue = (TextView) findViewById(R.id.weather_now_value);
        mWeatherMornValue = (TextView) findViewById(R.id.weather_morn_value);
        mWeatherDayValue = (TextView) findViewById(R.id.weather_day_value);
        mWeatherEveValue = (TextView) findViewById(R.id.weather_eve_value);
        mWeatherNightValue = (TextView) findViewById(R.id.weather_night_value);

        if (savedInstanceState == null) {
            if (getIntent() != null && getIntent().getExtras() != null) {
                mLocation = getIntent().getExtras().getParcelable(EXTRA_LOCATION);
                if (mLocation != null) {
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

        switch (id) {

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
        if (mLocationManager != null) {
            //noinspection ResourceType
            mLocationManager.removeUpdates(this);
        }
    }

    @Override
    public void processWithPermissionsGranted() {
        // права даны.
        if (!mIsLocationFixed) {
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    private void updateToolbarByLocation() {
        if (mIsLocationFixed) {
            mToolbar.setBackgroundResource(R.color.fixLocationCity);
            mRefreshLocation.setVisibility(View.GONE);
        } else {
            mToolbar.setBackgroundResource(R.color.useLocationCity);
            mRefreshLocation.setVisibility(View.VISIBLE);
        }
    }

    private void updateTitleByLocation() {
        if (mLocation != null) {
            Address address = LocationUtils.getAddress(this, mLocation);
            Log.v(TAG, "address:" + address);

            String placeName = LocationUtils.getAddressString(address);
            Log.v(TAG, "placeName:" + placeName);
            if (placeName != null) {
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
        if (mLocation != null) {
            WeatherProvider.getCurrentWeather(this, mLocation, this);
            WeatherProvider.getDailyWeather(this, mLocation, this);
        }
    }

    private void updateCurrentWeather(@Nullable CurrentWeather currentWeather) {
        if (currentWeather != null) {
            mWeatherDate.setText(TimeUtils.toIso(currentWeather.getDt() * 1000));
            mWeatherNowValue.setText(String.valueOf(currentWeather.getMain().getTemp()));
            String iconId = correctIconId(currentWeather.getWeather().get(0).getIcon());
            downloadIcon(iconId, mWeatherNowImage);
        } else {
            mWeatherDate.setText(R.string.n_a);
            mWeatherNowValue.setText(R.string.n_a);
            mWeatherNowImage.setImageDrawable(null);
        }

        mRefreshWeather.setEnabled(true);
    }

    private void updateDailyWeather(@Nullable DailyWeather dailyWeather) {
        if (dailyWeather != null) {
            Temp temp = dailyWeather.getList().get(0).getTemp();
            mWeatherMornValue.setText(String.valueOf(temp.getMorn()));
            mWeatherDayValue.setText(String.valueOf(temp.getDay()));
            mWeatherEveValue.setText(String.valueOf(temp.getEve()));
            mWeatherNightValue.setText(String.valueOf(temp.getNight()));
        } else {
            mWeatherMornValue.setText(R.string.n_a);
            mWeatherDayValue.setText(R.string.n_a);
            mWeatherEveValue.setText(R.string.n_a);
            mWeatherNightValue.setText(R.string.n_a);
        }

        mRefreshWeather.setEnabled(true);
    }

    private void downloadIcon(String iconId, ImageView imageView) {
        String url = String.format(Consts.API_ICON_URL, iconId);
        Glide.with(this).load(url).into(imageView);
    }

    private void downloadIcon(String iconId, TextView textView) {
        String url = String.format(Consts.API_ICON_URL, iconId);
        Glide.with(this).load(url).into(new TextViewDrawableTarget(textView));
    }

    /**
     * https://openweathermap.org/weather-conditions
     *
     * @param iconId
     * @return
     */
    private static String correctIconId(String iconId) {
        return "501".equals(iconId) ? "10d" : iconId;
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
     * @see WeatherProvider#getCurrentWeather(Context, Location, WeatherProvider.WeatherCallback)
     */
    @Override
    public void onCurrentWeatherResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
        CurrentWeather currentWeather = response.body();
        updateCurrentWeather(currentWeather);
        if (currentWeather == null) {
            Toast.makeText(this, getString(R.string.get_weather_empty_result), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @see WeatherProvider#getCurrentWeather(Context, Location, WeatherProvider.WeatherCallback)
     */
    @Override
    public void onCurrentWeatherFailure(Call<CurrentWeather> call, Throwable t) {
        Toast.makeText(this, getString(R.string.get_weather_error, t.getLocalizedMessage()), Toast.LENGTH_LONG).show();
        updateCurrentWeather(null);
    }

    /**
     * @see WeatherProvider#getDailyWeather(Context, Location, WeatherProvider.WeatherCallback)
     */
    @Override
    public void onDailyWeatherResponse(Call<DailyWeather> call, Response<DailyWeather> response) {
        DailyWeather dailyWeather = response.body();
        updateDailyWeather(dailyWeather);
        if (dailyWeather == null) {
            Toast.makeText(this, getString(R.string.get_weather_empty_result), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @see WeatherProvider#getDailyWeather(Context, Location, WeatherProvider.WeatherCallback)
     */
    @Override
    public void onDailyWeatherFailure(Call<DailyWeather> call, Throwable t) {
        Toast.makeText(this, getString(R.string.get_weather_error, t.getLocalizedMessage()), Toast.LENGTH_LONG).show();
        updateDailyWeather(null);
    }


    private static class TextViewDrawableTarget implements Target<GlideDrawable> {

        private TextView mTextView;

        TextViewDrawableTarget(TextView textView) {
            mTextView = textView;
        }

        @Override
        public void onLoadStarted(Drawable placeholder) {
            mTextView.setCompoundDrawablesWithIntrinsicBounds(placeholder, null, null, null);
        }

        @Override
        public void onLoadFailed(Exception e, Drawable errorDrawable) {
            mTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        @Override
        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
            mTextView.setCompoundDrawablesWithIntrinsicBounds(resource, null, null, null);
        }

        @Override
        public void onLoadCleared(Drawable placeholder) {

        }

        @Override
        public void getSize(SizeReadyCallback cb) {

        }

        @Override
        public void setRequest(Request request) {

        }

        @Override
        public Request getRequest() {
            return null;
        }

        @Override
        public void onStart() {

        }

        @Override
        public void onStop() {

        }

        @Override
        public void onDestroy() {

        }
    }

}
