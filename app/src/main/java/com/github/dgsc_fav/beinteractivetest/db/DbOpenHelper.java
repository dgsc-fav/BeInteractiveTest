package com.github.dgsc_fav.beinteractivetest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.github.dgsc_fav.beinteractivetest.db.table.CitiesTable;

/**
 * Created by DG on 23.10.2016.
 */

public class DbOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "weather.sqlite";
    private static final int DATABASE_VERSION = 1;

    public DbOpenHelper(@NonNull Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(CitiesTable.getCreateTableQuery());
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        // no impl
    }
}
