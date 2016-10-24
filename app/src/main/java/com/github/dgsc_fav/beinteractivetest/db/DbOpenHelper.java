package com.github.dgsc_fav.beinteractivetest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.github.dgsc_fav.beinteractivetest.db.table.CitiesTable;
import com.github.dgsc_fav.beinteractivetest.db.table.CurrentWeatherTable;
import com.github.dgsc_fav.beinteractivetest.db.table.DailyWeatherTable;

/**
 * Created by DG on 23.10.2016.
 */

public class DbOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "weather.sqlite";
    private static final int DATABASE_VERSION = 5;

    public DbOpenHelper(@NonNull Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(CitiesTable.getCreateTableQuery());
        db.execSQL(CurrentWeatherTable.getCreateTableQuery());
        db.execSQL(DailyWeatherTable.getCreateTableQuery());
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
