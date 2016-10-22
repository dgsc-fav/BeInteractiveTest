package com.github.dgsc_fav.beinteractivetest.weather;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import com.github.dgsc_fav.beinteractivetest.R;
import com.github.dgsc_fav.beinteractivetest.weather.api.Consts;
import com.github.dgsc_fav.beinteractivetest.weather.api.OpenWeatherMapApi;
import com.github.dgsc_fav.beinteractivetest.weather.api.model.CurrentWeather;
import com.github.dgsc_fav.beinteractivetest.weather.api.model.DailyWeather;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DG on 22.10.2016.
 */
public class WeatherService {

    public static void getCurrentWeather(@NonNull Context context, Location location,
            @NonNull Callback<CurrentWeather> callback) {
        if (location == null) {
            callback.onFailure(null, new WeatherException("Location is null"));
            return;
        }

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Consts.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // prepare call
        OpenWeatherMapApi openWeatherMapApi = retrofit.create(OpenWeatherMapApi.class);

        Call<CurrentWeather> call =
                openWeatherMapApi.getCurrentWeather(location.getLatitude(), location.getLongitude(),
                        "metric", context
                                .getResources()
                                .getString(R.string.owm_api_key));
        // asynchronous call
        call.enqueue(callback);
    }

    public static void getDailyWeather(@NonNull Context context, Location location,
            @NonNull Callback<DailyWeather> callback) {
        if (location == null) {
            callback.onFailure(null, new WeatherException("Location is null"));
            return;
        }

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Consts.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // prepare call
        OpenWeatherMapApi openWeatherMapApi = retrofit.create(OpenWeatherMapApi.class);

        Call<DailyWeather> call =
                openWeatherMapApi.getDailyWeather(location.getLatitude(), location.getLongitude(),
                        "metric",
                        context
                                .getResources()
                                .getString(R.string.owm_api_key));
        // asynchronous call
        call.enqueue(callback);
    }
}
