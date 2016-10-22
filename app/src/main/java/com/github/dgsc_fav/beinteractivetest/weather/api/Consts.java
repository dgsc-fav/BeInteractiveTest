package com.github.dgsc_fav.beinteractivetest.weather.api;

/**
 * Created by DG on 22.10.2016.
 */

public interface Consts {
    /**
     * Базовый URL сервиса
     *
     * @see OpenWeatherMapApi#getDailyWeather(double, double, String, String)
     */
    String API_BASE_URL = "http://api.openweathermap.org/";

    /**
     * http://openweathermap.org/appid
     * Do not send requests more then 1 time per 10 minutes from one device/one API key. Normally the weather is not changing so frequently.
     */
    int CACHED_TIME = 10 * 60 * 1000; // 10 minutes

    String X_CACHE_KEY = "X-Cache-Key";
}
