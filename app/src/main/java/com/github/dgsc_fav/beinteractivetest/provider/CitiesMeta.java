package com.github.dgsc_fav.beinteractivetest.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.github.dgsc_fav.beinteractivetest.db.entity.CityEntity;
import com.github.dgsc_fav.beinteractivetest.db.table.CitiesTable;
import com.pushtorefresh.storio.contentresolver.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.contentresolver.operations.delete.DeleteResolver;
import com.pushtorefresh.storio.contentresolver.operations.get.DefaultGetResolver;
import com.pushtorefresh.storio.contentresolver.operations.get.GetResolver;
import com.pushtorefresh.storio.contentresolver.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.contentresolver.operations.put.PutResolver;
import com.pushtorefresh.storio.contentresolver.queries.DeleteQuery;
import com.pushtorefresh.storio.contentresolver.queries.InsertQuery;
import com.pushtorefresh.storio.contentresolver.queries.UpdateQuery;


/**
 * Created by DG on 23.10.2016.
 */

public class CitiesMeta {
    @NonNull
    public static final String URI_STRING =
            "content://" + DbContentProvider.AUTHORITY + "/tweets";

    @NonNull
    public static final Uri CONTENT_URI = Uri.parse(URI_STRING);

    @NonNull
    public static final PutResolver<CityEntity> PUT_RESOLVER = new DefaultPutResolver<CityEntity>() {
        @NonNull
        @Override
        protected InsertQuery mapToInsertQuery(@NonNull CityEntity object) {
            return InsertQuery.builder()
                    .uri(CONTENT_URI)
                    .build();
        }

        @NonNull
        @Override
        protected UpdateQuery mapToUpdateQuery(@NonNull CityEntity tweet) {
            return UpdateQuery.builder()
                    .uri(CONTENT_URI)
                    .where(CitiesTable.COLUMN_ID + " = ?")
                    .whereArgs(tweet.id())
                    .build();
        }

        @NonNull
        @Override
        protected ContentValues mapToContentValues(@NonNull CityEntity object) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(CitiesTable.COLUMN_ID, object.id());
            contentValues.put(CitiesTable.COLUMN_CITY, object.city());

            return contentValues;
        }
    };

    @NonNull
    public static final GetResolver<CityEntity> GET_RESOLVER = new DefaultGetResolver<CityEntity>() {
        @NonNull
        @Override
        public CityEntity mapFromCursor(@NonNull Cursor cursor) {
            return CityEntity.newCity(
                    cursor.getLong(cursor.getColumnIndexOrThrow(CitiesTable.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CitiesTable.COLUMN_CITY))
            );
        }
    };

    @NonNull
    public static final DeleteResolver<CityEntity> DELETE_RESOLVER = new DefaultDeleteResolver<CityEntity>() {
        @NonNull
        @Override
        protected DeleteQuery mapToDeleteQuery(@NonNull CityEntity tweet) {
            return DeleteQuery.builder()
                    .uri(CONTENT_URI)
                    .where(CitiesTable.COLUMN_ID + " = ?")
                    .whereArgs(tweet.id())
                    .build();
        }
    };
}
