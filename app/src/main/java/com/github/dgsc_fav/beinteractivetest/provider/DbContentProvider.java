package com.github.dgsc_fav.beinteractivetest.provider;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.dgsc_fav.beinteractivetest.db.DbOpenHelper;
import com.github.dgsc_fav.beinteractivetest.db.entity.CityEntity;
import com.github.dgsc_fav.beinteractivetest.db.entity.CityEntitySQLiteTypeMapping;
import com.github.dgsc_fav.beinteractivetest.db.table.CitiesTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;

/**
 * Provider для работы с sqlite db
 * Created by DG on 23.10.2016.
 */

public class DbContentProvider extends ContentProvider {
    /**
     * Константы для {@link DbContentProvider}
     * Created by DG on 23.10.2016.
     */

    interface DbContract {
        public static final String PATH_CITIES = CitiesTable.TABLE;
        public static final int URI_MATCHER_CODE_CITIES = 1;
        public static final String PATH_CURRENT_WEATHER = "cweather";
        public static final int URI_MATCHER_CODE_CURRENT_WEATHER = 2;
        public static final String PATH_DAILY_WEATHER = "dweather";
        public static final int URI_MATCHER_CODE_DAILY_WEATHER = 3;
    }

    @NonNull
    public static final String AUTHORITY = "com.github.dgsc_fav.beinteractivetest.provider.db_content_provider";

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, DbContract.PATH_CITIES, DbContract.URI_MATCHER_CODE_CITIES);
        URI_MATCHER.addURI(AUTHORITY, DbContract.PATH_CURRENT_WEATHER, DbContract.URI_MATCHER_CODE_CURRENT_WEATHER);
        URI_MATCHER.addURI(AUTHORITY, DbContract.PATH_DAILY_WEATHER, DbContract.URI_MATCHER_CODE_DAILY_WEATHER);
    }


    private static SQLiteOpenHelper sSQLiteOpenHelper;
    private static StorIOSQLite sStorIOSQLite;

    @Override
    public boolean onCreate() {
        Log.e("DB", "onCreate");
        assert getContext() != null;
        sSQLiteOpenHelper = new DbOpenHelper(getContext());
        // такая инитка, потому что не используем DI
        sStorIOSQLite = initStorIOSQLite();
        CitiesProvider.init();
        return false;
    }

    private StorIOSQLite initStorIOSQLite() {
        return DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sSQLiteOpenHelper)
                // CityEntitySQLiteTypeMapping is autogenerated
                .addTypeMapping(CityEntity.class, new CityEntitySQLiteTypeMapping())
                .build();
    }

    public static StorIOSQLite getStorIOSQLite() {
        return sStorIOSQLite;
    }

    public static SQLiteOpenHelper getSQLiteOpenHelper() {
        return sSQLiteOpenHelper;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        switch (URI_MATCHER.match(uri)) {
            case DbContract.URI_MATCHER_CODE_CITIES:
                return sSQLiteOpenHelper
                        .getReadableDatabase()
                        .query(
                                CitiesTable.TABLE,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder
                        );

            default:
                return null;
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final long insertedId;

        switch (URI_MATCHER.match(uri)) {
            case DbContract.URI_MATCHER_CODE_CITIES:
                insertedId = sSQLiteOpenHelper
                        .getWritableDatabase()
                        .insert(
                                CitiesTable.TABLE,
                                null,
                                values
                        );
                break;

            default:
                return null;
        }

        if (insertedId != -1) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return ContentUris.withAppendedId(uri, insertedId);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int numberOfRowsDeleted;

        switch (URI_MATCHER.match(uri)) {
            case DbContract.URI_MATCHER_CODE_CITIES:
                numberOfRowsDeleted = sSQLiteOpenHelper
                        .getWritableDatabase()
                        .delete(
                                CitiesTable.TABLE,
                                selection,
                                selectionArgs
                        );
                break;

            default:
                return 0;
        }

        if (numberOfRowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int numberOfRowsAffected;

        switch (URI_MATCHER.match(uri)) {
            case DbContract.URI_MATCHER_CODE_CITIES:
                numberOfRowsAffected = sSQLiteOpenHelper
                        .getWritableDatabase()
                        .update(
                                CitiesTable.TABLE,
                                values,
                                selection,
                                selectionArgs
                        );
                break;

            default:
                return 0;
        }

        if (numberOfRowsAffected > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsAffected;
    }
}
