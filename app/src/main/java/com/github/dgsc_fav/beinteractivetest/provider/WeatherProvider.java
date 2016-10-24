package com.github.dgsc_fav.beinteractivetest.provider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.dgsc_fav.beinteractivetest.db.entity.CurrentWeatherEntity;
import com.github.dgsc_fav.beinteractivetest.db.entity.DailyWeatherEntity;
import com.github.dgsc_fav.beinteractivetest.db.table.CurrentWeatherTable;
import com.github.dgsc_fav.beinteractivetest.db.table.DailyWeatherTable;
import com.github.dgsc_fav.beinteractivetest.util.NetUtils;
import com.github.dgsc_fav.beinteractivetest.weather.WeatherException;
import com.github.dgsc_fav.beinteractivetest.weather.WeatherService;
import com.github.dgsc_fav.beinteractivetest.weather.api.Consts;
import com.github.dgsc_fav.beinteractivetest.weather.api.model.CurrentWeather;
import com.github.dgsc_fav.beinteractivetest.weather.api.model.DailyWeather;
import com.google.gson.Gson;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * WeatherActivity(WeatherCallback)<->WeatherProvider<->WeatherService<->"api.openweathermap.org"
 * Created by DG on 22.10.2016.
 */
public class WeatherProvider {

    public interface WeatherCallback {
        void onCurrentWeatherResponse(CurrentWeather currentWeather, boolean fromServer);

        void onCurrentWeatherFailure(Throwable t);

        void onDailyWeatherResponse(DailyWeather dailyWeather, boolean fromServer);

        void onDailyWeatherFailure(Throwable t);
    }

    // пусть будет с одним тредом
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    // assert DbContentProvider будет создан при старте приложения
    private static final StorIOSQLite IOSQ_LITE = DbContentProvider.getStorIOSQLite();

    public static void init() {
        // stub
    }

    // погода для моей локации вынесена из базы
    private static CurrentWeatherEntity sMyPlaceCurrentWeatherEntity;
    private static DailyWeatherEntity sMyPlaceDailyWeatherEntity;

    /**
     * Запрашивает в базе или с сервера текущую погоду по location
     *
     * @param location по какому местополжению запрашивать погоду
     * @param needUpdate true - если требуется обязательно запросить с сервера
     * @param ignorePersist true - хранить данные только в классе. не надо DB
     * @param currentWeatherCallback callback метода
     */
    public static void getCurrentWeather(@NonNull final Context context, final Location location,
            final boolean needUpdate, final boolean ignorePersist,
            @NonNull final WeatherCallback currentWeatherCallback) {

        final String cacheKey = getCacheKeyLocationPart(location);
        if (cacheKey != null) {
            EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {
                    // проверка наличия в базе или в статич поле,если моя локация
                    CurrentWeatherEntity currentWeatherEntity =
                            ignorePersist ? sMyPlaceCurrentWeatherEntity : getCurrentWeatherImpl(cacheKey);

                    if (needUpdate) {
                        // обязательно с сервера
                        delCurrentWeatherImpl(cacheKey);
                        getCurrentWeatherFromServer(context, location, ignorePersist,
                                currentWeatherCallback);
                    } else if (currentWeatherEntity == null) {
                        // в базе c таким cacheKey нет
                        getCurrentWeatherFromServer(context, location, ignorePersist,
                                currentWeatherCallback);
                    } else if (isInvalidatedByTime(currentWeatherEntity.timestamp())
                            && NetUtils.isNetworkAvailable(context)) {
                        // invalid по времени и есть сеть
                        delCurrentWeatherImpl(cacheKey);
                        getCurrentWeatherFromServer(context, location, ignorePersist,
                                currentWeatherCallback);
                    } else {
                        currentWeatherCallback.onCurrentWeatherResponse(
                                fromBeanToWeather(currentWeatherEntity), false);
                    }
                }
            });
        } else {
            currentWeatherCallback.onCurrentWeatherFailure(
                    new WeatherException("location == null"));
        }
    }

    /**
     * Запрашивает в базе или с сервера дневную погоду по location
     *
     * @param location по какому местополжению запрашивать погоду
     * @param needUpdate true - если требуется обязательно запросить с сервера
     * @param ignorePersist true - хранить данные только в классе. не надо DB
     * @param dailyWeatherCallback callback метода
     */
    public static void getDailyWeather(@NonNull final Context context, final Location location,
            final boolean needUpdate, final boolean ignorePersist,
            @NonNull final WeatherCallback dailyWeatherCallback) {
        final String cacheKey = getCacheKeyLocationPart(location);
        if (cacheKey != null) {
            EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {
                    // проверка наличия в базе или в статич поле,если моя локация
                    DailyWeatherEntity dailyWeatherEntity =
                            ignorePersist ? sMyPlaceDailyWeatherEntity : getDailyWeatherImpl(cacheKey);

                    if (needUpdate) {
                        // обязательно с сервера
                        delDailyWeatherImpl(cacheKey);
                        getDailyWeatherFromServer(context, location, ignorePersist,
                                dailyWeatherCallback);
                    } else if (dailyWeatherEntity == null) {
                        // в базе c таким cacheKey нет
                        getDailyWeatherFromServer(context, location, ignorePersist,
                                dailyWeatherCallback);
                    } else if (isInvalidatedByTime(dailyWeatherEntity.timestamp())
                            && NetUtils.isNetworkAvailable(context)) {
                        // invalid по времени и есть сеть
                        delDailyWeatherImpl(cacheKey);
                        getDailyWeatherFromServer(context, location, ignorePersist,
                                dailyWeatherCallback);
                    } else {
                        dailyWeatherCallback.onDailyWeatherResponse(
                                fromBeanToWeather(dailyWeatherEntity), false);
                    }
                }
            });
        } else {
            dailyWeatherCallback.onDailyWeatherFailure(new WeatherException("location == null"));
        }
    }

    private static void getCurrentWeatherFromServer(@NonNull Context context,
            final Location location, final boolean ignorePersist,
            @NonNull final WeatherCallback currentWeatherCallback) {
        WeatherService.getCurrentWeather(context, location, new Callback<CurrentWeather>() {
            @Override
            public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                if(ignorePersist) {
                    sMyPlaceCurrentWeatherEntity = fromWeatherToBean(location, response.body());
                } else {
                    putCurrentWeather(location, response.body());
                }
                currentWeatherCallback.onCurrentWeatherResponse(response.body(), true);
            }

            @Override
            public void onFailure(Call<CurrentWeather> call, Throwable t) {
                currentWeatherCallback.onCurrentWeatherFailure(t);
            }
        });
    }

    private static void getDailyWeatherFromServer(@NonNull Context context,
            final Location location, final boolean ignorePersist,
            @NonNull final WeatherCallback dailyWeatherCallback) {
        WeatherService.getDailyWeather(context, location, new Callback<DailyWeather>() {
            @Override
            public void onResponse(Call<DailyWeather> call, Response<DailyWeather> response) {
                if (ignorePersist) {
                    sMyPlaceDailyWeatherEntity = fromWeatherToBean(location, response.body());
                } else {
                    putDailyWeather(location, response.body());
                }
                dailyWeatherCallback.onDailyWeatherResponse(response.body(), true);
            }

            @Override
            public void onFailure(Call<DailyWeather> call, Throwable t) {
                dailyWeatherCallback.onDailyWeatherFailure(t);
            }
        });
    }

    private static CurrentWeatherEntity getCurrentWeatherImpl(String cacheKey) {
        return IOSQ_LITE.get()
                .object(CurrentWeatherEntity.class)
                .withQuery(Query.builder()
                        .table(CurrentWeatherTable.TABLE)
                        .where(CurrentWeatherTable.COLUMN_CACHE_KEY + "=?")
                        .whereArgs(cacheKey)
                        .build())
                .prepare()
                .executeAsBlocking();
    }


    private static void putCurrentWeather(final Location location,
            final CurrentWeather currentWeather) {
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                putCurrentWeatherImpl(fromWeatherToBean(location, currentWeather));
            }
        });
    }

    private static DeleteResult delCurrentWeatherImpl(String cacheKey) {
        return IOSQ_LITE.delete().byQuery(DeleteQuery.builder()
                .table(CurrentWeatherTable.TABLE)
                .where(CurrentWeatherTable.COLUMN_CACHE_KEY + "=?")
                .whereArgs(cacheKey)
                .build())
                .prepare()
                .executeAsBlocking();
    }

    private static PutResult putCurrentWeatherImpl(CurrentWeatherEntity currentWeatherEntity) {
        return IOSQ_LITE.put().object(currentWeatherEntity).prepare().executeAsBlocking();
    }

    private static DailyWeatherEntity getDailyWeatherImpl(String cacheKey) {
        return IOSQ_LITE.get()
                .object(DailyWeatherEntity.class)
                .withQuery(Query.builder()
                        .table(DailyWeatherTable.TABLE)
                        .where(DailyWeatherTable.COLUMN_CACHE_KEY + "=?")
                        .whereArgs(cacheKey)
                        .build())
                .prepare()
                .executeAsBlocking();
    }


    private static void putDailyWeather(final Location location, final DailyWeather dailyWeather) {
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                putDailyWeatherImpl(fromWeatherToBean(location, dailyWeather));
            }
        });
    }

    private static PutResult putDailyWeatherImpl(DailyWeatherEntity dailyWeatherEntity) {
        return IOSQ_LITE.put().object(dailyWeatherEntity).prepare().executeAsBlocking();
    }

    private static DeleteResult delDailyWeatherImpl(String cacheKey) {
        return IOSQ_LITE.delete()
                .byQuery(DeleteQuery.builder()
                        .table(DailyWeatherTable.TABLE)
                        .where(DailyWeatherTable.COLUMN_CACHE_KEY + "=?")
                        .whereArgs(cacheKey)
                        .build())
                .prepare()
                .executeAsBlocking();
    }

    /**
     * Создание строки ключа для кэша.
     * Используется только два числа после запятой, потому что OpenWeatherMapApi
     * использует такой формат в своём кэше. Видимо, и данные хранит только с такой точностью
     * местоположения
     */
    @Nullable
    private static String getCacheKeyLocationPart(Location location) {
        if (location == null) {
            return null;
        }

        return getCacheKeyLocationPart(location.getLatitude(), location.getLongitude());
    }

    private static String getCacheKeyLocationPart(double latitude, double longitude) {
        @SuppressLint("DefaultLocale") String lat = String.format("%.2f", latitude);
        @SuppressLint("DefaultLocale") String lon = String.format("%.2f", longitude);

        String cacheKey = String.format("lat=%s&lon=%s", lat, lon);

        return cacheKey;
    }


    private static CurrentWeather fromBeanToWeather(
            @NonNull CurrentWeatherEntity currentWeatherEntity) {
        Gson gson = new Gson();
        return gson.fromJson(currentWeatherEntity.content(), CurrentWeather.class);
    }

    private static DailyWeather fromBeanToWeather(@NonNull DailyWeatherEntity dailyWeatherEntity) {
        Gson gson = new Gson();
        return gson.fromJson(dailyWeatherEntity.content(), DailyWeather.class);
    }

    private static CurrentWeatherEntity fromWeatherToBean(@NonNull Location location,
            @NonNull CurrentWeather currentWeather) {

        Gson gson = new Gson();
        String content = gson.toJson(currentWeather);

        return CurrentWeatherEntity.newCurrentWeather(System.currentTimeMillis(),
                getCacheKeyLocationPart(location), content);
    }

    private static DailyWeatherEntity fromWeatherToBean(@NonNull Location location,
            @NonNull DailyWeather dailyWeather) {

        Gson gson = new Gson();
        String content = gson.toJson(dailyWeather);

        return DailyWeatherEntity.newDailyWeather(System.currentTimeMillis(),
                getCacheKeyLocationPart(location), content);
    }

    /**
     * Оффлайн данные действительны {@link Consts#CACHED_TIME} мс.
     *
     * @param timestamp время в мс последнего обновления в базе
     */
    private static boolean isInvalidatedByTime(@NonNull Long timestamp) {
        return (System.currentTimeMillis() - timestamp) > Consts.CACHED_TIME;
    }
}
