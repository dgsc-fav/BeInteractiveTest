package com.github.dgsc_fav.beinteractivetest.db.entity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.dgsc_fav.beinteractivetest.db.table.DailyWeatherTable;
import com.github.dgsc_fav.beinteractivetest.provider.meta.DailyWeatherMeta;
import com.pushtorefresh.storio.contentresolver.annotations.StorIOContentResolverColumn;
import com.pushtorefresh.storio.contentresolver.annotations.StorIOContentResolverType;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by DG on 23.10.2016.
 */
// This annotation will trigger annotation processor
// Which will generate type mapping code in compile time,
// You just need to link it in your code.
@StorIOSQLiteType(table = DailyWeatherTable.TABLE)
@StorIOContentResolverType(uri = DailyWeatherMeta.URI_STRING)
public class DailyWeatherEntity {
    /**
     * If object was not inserted into db, id will be null
     */
    @Nullable
    @StorIOSQLiteColumn(name = DailyWeatherTable.COLUMN_ID, key = true)
    @StorIOContentResolverColumn(name = DailyWeatherTable.COLUMN_ID, key = true)
    Long id;

    @Nullable
    @StorIOSQLiteColumn(name = DailyWeatherTable.COLUMN_TIMESTAMP)
    @StorIOContentResolverColumn(name = DailyWeatherTable.COLUMN_TIMESTAMP)
    Long timestamp;

    @NonNull
    @StorIOSQLiteColumn(name = DailyWeatherTable.COLUMN_CACHE_KEY)
    @StorIOContentResolverColumn(name = DailyWeatherTable.COLUMN_CACHE_KEY)
    String cacheKey;

    @NonNull
    @StorIOSQLiteColumn(name = DailyWeatherTable.COLUMN_CONTENT)
    @StorIOContentResolverColumn(name = DailyWeatherTable.COLUMN_CONTENT)
    String content;


    // leave default constructor for AutoGenerated code!
    DailyWeatherEntity() {
    }

    private DailyWeatherEntity(@Nullable Long id, @NonNull Long timestamp, @NonNull String cacheKey, @NonNull String content) {
        this.id = id;
        this.timestamp = timestamp;
        this.cacheKey = cacheKey;
        this.content = content;
    }

    @NonNull
    public static DailyWeatherEntity newDailyWeather(@Nullable Long id, @NonNull Long timestamp,
            @NonNull String cacheKey, @NonNull String content) {
        return new DailyWeatherEntity(id, timestamp, cacheKey, content);
    }

    @NonNull
    public static DailyWeatherEntity newDailyWeather(@NonNull Long timestamp,
            @NonNull String cacheKey, @NonNull String content) {
        return new DailyWeatherEntity(null, timestamp, cacheKey, content);
    }

    @Nullable
    public Long id() {
        return id;
    }

    @NonNull
    public Long timestamp() {
        return timestamp;
    }

    @NonNull
    public String cacheKey() {
        return cacheKey;
    }

    @NonNull
    public String content() {
        return content;
    }

    // // TODO: 23.10.2016 разобраться с валидностью метода
    // BTW, you can use AutoValue/AutoParcel to get immutability and code generation for free
    // Check our tests, we have examples!
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DailyWeatherEntity cityEntity = (DailyWeatherEntity) o;

        if (id != null ? !id.equals(cityEntity.id) : cityEntity.id != null) {
            return false;
        }
        return this.content.equals(cityEntity.content);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + content.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CurrentWeather{" +
                "id=" + id +
                ", timestamp='" + timestamp + '\'' +
                ", cacheKey='" + cacheKey + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
