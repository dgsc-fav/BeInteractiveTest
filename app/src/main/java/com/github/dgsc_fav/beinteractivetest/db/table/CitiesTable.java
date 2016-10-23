package com.github.dgsc_fav.beinteractivetest.db.table;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.queries.Query;

/**
 * Хранит объект типа {@link com.github.dgsc_fav.beinteractivetest.weather.api.model.City}
 * Для простоты объект хранится в серилизованном виде.
 * В данном случае, в формате Json
 *
 * Created by DG on 23.10.2016.
 */

public class CitiesTable {



    @NonNull
    public static final String TABLE = "cities";

    @NonNull
    public static final String COLUMN_ID = "_id";

    @NonNull
    public static final String COLUMN_CITY = "city";

    public static final String COLUMN_ID_WITH_TABLE_PREFIX = TABLE + "." + COLUMN_ID;
    public static final String COLUMN_CITY_WITH_TABLE_PREFIX = TABLE + "." + COLUMN_CITY;

    // Yep, with StorIO you can safely store queries as objects and reuse them, they are immutable
    @NonNull
    public static final Query QUERY_ALL = Query.builder()
            .table(TABLE)
            .build();

    // This is just class with Meta Data, we don't need instances
    private CitiesTable() {
        throw new IllegalStateException("No instances please");
    }

    @NonNull
    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_CITY + " TEXT NOT NULL"
                + ");";
    }
}
