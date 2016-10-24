package com.github.dgsc_fav.beinteractivetest.db.table;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.queries.Query;

/**
 * Хранит объект типа {@link com.github.dgsc_fav.beinteractivetest.weather.api.model.DailyWeather}
 * под маской {@link com.github.dgsc_fav.beinteractivetest.db.entity.DailyWeatherEntity}
 * Для простоты объект хранится в серилизованном виде
 * В данном случае, в формате Json
 *
 * Created by DG on 23.10.2016.
 */

public class DailyWeatherTable {
    @NonNull
    public static final String TABLE = "d_weather";

    @NonNull
    public static final String COLUMN_ID = "_id";

    @NonNull
    public static final String COLUMN_TIMESTAMP = "timestamp";
    @NonNull
    public static final String COLUMN_CACHE_KEY = "cacheKey";
    @NonNull
    public static final String COLUMN_CONTENT = "content";

    // Yep, with StorIO you can safely store queries as objects and reuse them, they are immutable
    @NonNull
    public static final Query QUERY_ALL = Query.builder()
            .table(TABLE)
            .build();

    // This is just class with Meta Data, we don't need instances
    private DailyWeatherTable() {
        throw new IllegalStateException("No instances please");
    }

    @NonNull
    public static String getCreateTableQuery() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_TIMESTAMP + " INTEGER NOT NULL, "
                + COLUMN_CACHE_KEY + " TEXT NOT NULL UNIQUE, "
                + COLUMN_CONTENT + " TEXT NOT NULL"
                + ");";
    }
}
