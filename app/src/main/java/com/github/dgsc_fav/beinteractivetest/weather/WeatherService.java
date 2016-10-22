package com.github.dgsc_fav.beinteractivetest.weather;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.dgsc_fav.beinteractivetest.R;
import com.github.dgsc_fav.beinteractivetest.weather.api.Consts;
import com.github.dgsc_fav.beinteractivetest.weather.api.OpenWeatherMapApi;
import com.github.dgsc_fav.beinteractivetest.weather.api.model.CurrentWeather;
import com.github.dgsc_fav.beinteractivetest.weather.api.model.DailyWeather;
import com.github.dgsc_fav.beinteractivetest.weather.api.model.WeatherHolder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DG on 22.10.2016.
 */
public class WeatherService {

    private static final String TAG = WeatherService.class.getSimpleName();

    public static void getWeather(@NonNull Context context, Location location, @NonNull final Callback<WeatherHolder> callback) {
        final WeatherHolder weatherHolder = new WeatherHolder();

        getCurrentWeather(context, location, new Callback<CurrentWeather>() {
            @Override
            public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                String x_cache_key = response.headers().get(Consts.X_CACHE_KEY);
                Log.v(TAG, "getCurrentWeather x_cache_key:" + x_cache_key);
                weatherHolder.setCurrentWeatherCacheKey(x_cache_key);
                weatherHolder.setCurrentWeather(response.body());
            }

            @Override
            public void onFailure(Call<CurrentWeather> call, Throwable t) {
                callback.onFailure(null, t);
            }
        });

        getDailyWeather(context, location, new Callback<DailyWeather>() {
            @Override
            public void onResponse(Call<DailyWeather> call, Response<DailyWeather> response) {
                String x_cache_key = response.headers().get(Consts.X_CACHE_KEY);
                Log.v(TAG, "getDailyWeather x_cache_key:" + x_cache_key);
                weatherHolder.setDailyWeatherCacheKey(x_cache_key);
                weatherHolder.setDailyWeather(response.body());
            }

            @Override
            public void onFailure(Call<DailyWeather> call, Throwable t) {
                callback.onFailure(null, t);
            }
        });

        //callback.onResponse(null, weatherHolder);
    }

    public static void getCurrentWeather(@NonNull Context context, Location location, @NonNull Callback<CurrentWeather> callback) {
        if(location == null) {
            callback.onFailure(null, new WeatherException("Location is null"));
            return;
        }

        // // TODO: 22.10.2016 проверка периодичности запросов. если есть валидные данные, то отдать их

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Consts.API_BASE_URL).addConverterFactory(
                GsonConverterFactory.create()).build();

        // prepare call
        OpenWeatherMapApi openWeatherMapApi = retrofit.create(OpenWeatherMapApi.class);

        Call<CurrentWeather> call = openWeatherMapApi.getCurrentWeather(location.getLatitude(),
                                                                        location.getLongitude(),
                                                                        "metric",
                                                                        context
                                                                                .getResources()
                                                                                .getString(R.string.owm_api_key));
        // asynchronous call
        call.enqueue(callback);
    }

    public static void getDailyWeather(@NonNull Context context, Location location, @NonNull Callback<DailyWeather> callback) {
        if(location == null) {
            callback.onFailure(null, new WeatherException("Location is null"));
            return;
        }

        // // TODO: 22.10.2016 проверка периодичности запросов. если есть валидные данные, то отдать их

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Consts.API_BASE_URL).addConverterFactory(
                GsonConverterFactory.create()).build();

        // prepare call
        OpenWeatherMapApi openWeatherMapApi = retrofit.create(OpenWeatherMapApi.class);

        Call<DailyWeather> call = openWeatherMapApi.getDailyWeather(location.getLatitude(),
                                                                    location.getLongitude(),
                                                                    "metric",
                                                                    context
                                                                            .getResources()
                                                                            .getString(R.string.owm_api_key));
        // asynchronous call
        call.enqueue(callback);
    }
}
