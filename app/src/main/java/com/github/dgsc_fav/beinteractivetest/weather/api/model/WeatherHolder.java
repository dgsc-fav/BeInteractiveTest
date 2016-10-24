package com.github.dgsc_fav.beinteractivetest.weather.api.model;

/**
 * Created by DG on 22.10.2016.
 */
@Deprecated // или базу
public class WeatherHolder {
    private CurrentWeather currentWeather;
    private DailyWeather   dailyWeather;

    private String dailyWeatherCacheKey;
    private String currentWeatherCacheKey;

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public DailyWeather getDailyWeather() {
        return dailyWeather;
    }

    public void setDailyWeather(DailyWeather dailyWeather) {
        this.dailyWeather = dailyWeather;
    }

    public String getDailyWeatherCacheKey() {
        return dailyWeatherCacheKey;
    }

    public void setDailyWeatherCacheKey(String dailyWeatherCacheKey) {
        this.dailyWeatherCacheKey = dailyWeatherCacheKey;
    }

    public String getCurrentWeatherCacheKey() {
        return currentWeatherCacheKey;
    }

    public void setCurrentWeatherCacheKey(String currentWeatherCacheKey) {
        this.currentWeatherCacheKey = currentWeatherCacheKey;
    }
}
