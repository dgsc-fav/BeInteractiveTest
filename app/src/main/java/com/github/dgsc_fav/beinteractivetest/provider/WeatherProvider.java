package com.github.dgsc_fav.beinteractivetest.provider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.github.dgsc_fav.beinteractivetest.weather.WeatherService;
import com.github.dgsc_fav.beinteractivetest.weather.api.model.CurrentWeather;
import com.github.dgsc_fav.beinteractivetest.weather.api.model.DailyWeather;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * WeatherActivity(WeatherCallback)<->WeatherProvider<->WeatherService<->"api.openweathermap.org"
 * Created by DG on 22.10.2016.
 */
public class WeatherProvider {

    public interface WeatherCallback {
        void onCurrentWeatherResponse(Call<CurrentWeather> call, Response<CurrentWeather> response);

        void onCurrentWeatherFailure(Call<CurrentWeather> call, Throwable t);

        void onDailyWeatherResponse(Call<DailyWeather> call, Response<DailyWeather> response);

        void onDailyWeatherFailure(Call<DailyWeather> call, Throwable t);
    }

    public static void getCurrentWeather(@NonNull Context context, Location location,
            @NonNull final WeatherCallback currentWeatherCallback) {
        // TODO: 22.10.2016 тест на наличие в базе
        // ...
        // в базе нет

        WeatherService.getCurrentWeather(context, location, new Callback<CurrentWeather>() {
            @Override
            public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                currentWeatherCallback.onCurrentWeatherResponse(call, response);
            }

            @Override
            public void onFailure(Call<CurrentWeather> call, Throwable t) {
                currentWeatherCallback.onCurrentWeatherFailure(call, t);
            }
        });
    }

    public static void getDailyWeather(@NonNull Context context, Location location,
            @NonNull final WeatherCallback dailyWeatherCallback) {
        // TODO: 22.10.2016 тест на наличие в базе
        // ...
        // в базе нет

        WeatherService.getDailyWeather(context, location, new Callback<DailyWeather>() {
            @Override
            public void onResponse(Call<DailyWeather> call, Response<DailyWeather> response) {
                dailyWeatherCallback.onDailyWeatherResponse(call, response);
            }

            @Override
            public void onFailure(Call<DailyWeather> call, Throwable t) {
                dailyWeatherCallback.onDailyWeatherFailure(call, t);
            }
        });
    }

    /**
     * Создание строки ключа для кэша.
     * Используется только два числа после запятой, потому что OpenWeatherMapApi
     * использует такой формат в своём кэше. Видимо, и данные хранит только с такой точностью
     * местоположения
     */
    @Nullable
    private String getCacheKeyLocationPart(Location location) {
        if (location == null) {
            return null;
        }

        @SuppressLint("DefaultLocale")
        String lat = String.format("%.2f", location.getLatitude());
        @SuppressLint("DefaultLocale")
        String lon = String.format("%.2f", location.getLongitude());

        String cacheKey = String.format("lat=%s&lon=%s", lat, lon);

        return cacheKey;
    }
}
