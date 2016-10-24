package com.github.dgsc_fav.beinteractivetest.weather.api;

import com.github.dgsc_fav.beinteractivetest.weather.api.model.CurrentWeather;
import com.github.dgsc_fav.beinteractivetest.weather.api.model.DailyWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by DG on 22.10.2016.
 */
public interface OpenWeatherMapApi {
    @GET("data/2.5/weather")
    Call<CurrentWeather> getCurrentWeather(@Query("lat") double lat, @Query("lon") double lon, @Query("units") String units, @Query("APPID") String appId);

    @GET("data/2.5/forecast/daily")
    Call<DailyWeather> getDailyWeather(@Query("lat") double lat, @Query("lon") double lon, @Query("units") String units, @Query("APPID") String appId);
}
