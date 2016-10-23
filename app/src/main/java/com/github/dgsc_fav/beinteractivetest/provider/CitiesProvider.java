package com.github.dgsc_fav.beinteractivetest.provider;

import android.support.annotation.NonNull;

import com.github.dgsc_fav.beinteractivetest.db.entity.CityEntity;
import com.github.dgsc_fav.beinteractivetest.db.table.CitiesTable;
import com.github.dgsc_fav.beinteractivetest.weather.api.model.City;
import com.github.dgsc_fav.beinteractivetest.weather.api.model.Coord;
import com.google.gson.Gson;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by DG on 23.10.2016.
 */

public class CitiesProvider {

    public interface CitiesProviderCallback {
        void onComplete(List<City> cities);

        void onFailure();
    }

    private static final String TAG = CitiesProvider.class.getSimpleName();
    // пусть будет с одним тредом
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private static final AtomicBoolean sIsInited = new AtomicBoolean();
    // assert DbContentProvider будет создан при старте приложения
    private static final StorIOSQLite storIOSQLite = DbContentProvider.getStorIOSQLite();

    /**
     * Инициализация
     */
    public static void init() {
        sIsInited.set(false);

        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                // выполняем в отдельном таске, потому что с блокировкой
                checkNoEmpty();

                sIsInited.compareAndSet(false, true);
            }
        });
    }

    /**
     * Контроль, что в базе есть хотя бы одна запись.
     * запись с id=1 используется как Моё местоположение, дл опредееения погоды
     */
    private static void checkNoEmpty() {
        List<CityEntity> cityEntities = getImpl();

        if (cityEntities.isEmpty()) {
            // если пусто в базе, то заполняем
            fillDefaultData();
        } else {
            // если не пусто, то обновляем список/ или не обновляем. пусть сами берут через
            // getCities
        }

    }

    public static void getCities(@NonNull final CitiesProviderCallback citiesProviderCallback) {
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                citiesProviderCallback.onComplete(fromBeanToWeather(getImpl()));
            }
        });
    }

    public static void getCity(final long cityId,
            @NonNull final CitiesProviderCallback citiesProviderCallback) {
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                citiesProviderCallback.onComplete(fromBeanToWeather(getImpl(cityId)));
            }
        });
    }

    public static void putCity(@NonNull final City city,
            @NonNull final CitiesProviderCallback citiesProviderCallback) {
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                putImpl(fromWeatherToBean(Collections.singletonList(city)));
                // TODO: 23.10.2016 возвращать из результата
                citiesProviderCallback.onComplete(null);
            }
        });
    }

    public static void deleteCity(@NonNull final City city,
            @NonNull final CitiesProviderCallback citiesProviderCallback) {
        EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                deleteImpl(fromWeatherToBean(Collections.singletonList(city)).get(0));
                // TODO: 23.10.2016 возвращать из результата
                citiesProviderCallback.onComplete(null);
            }
        });
    }

    /**
     * Заполняет базу записями по умолчанию
     */
    private static void fillDefaultData() {
        List<City> cities = new ArrayList<>();
        // запись будет как Моё местоположение
        City city = new City(0, "My Place", new Coord(Double.MIN_VALUE, Double.MAX_VALUE), "", 0);
        cities.add(city);
        // эти города из задания. добавлять через assets или ещё как нет времени
        // данные City взяты с OpenWeatherMap.Org
        city = new City(2643741, "City of London", new Coord(-0.091840, 51.512791), "GB", 0);
        cities.add(city);
        city = new City(1850147, "Tokyo", new Coord(139.691711, 35.689499), "JP", 0);
        cities.add(city);
        city = new City(5128581, "New York City", new Coord(-74.005966, 40.714272), "US", 0);
        cities.add(city);

        // из Weather City в Sqlite City. Можно потом одним типом сделать
        List<CityEntity> cityEntities = fromWeatherToBean(cities);
        // сохраним в базу


        putImpl(cityEntities);
    }


    @NonNull
    private static List<CityEntity> getImpl() {
        return storIOSQLite
                .get()
                .listOfObjects(CityEntity.class)
                .withQuery(CitiesTable.QUERY_ALL)
                .prepare()
                .executeAsBlocking();
    }

    @NonNull
    private static List<CityEntity> getImpl(long id) {
        return storIOSQLite
                .get()
                .listOfObjects(CityEntity.class)
                .withQuery(Query.builder()
                        .table(CitiesTable.TABLE)
                        .where(CitiesTable.COLUMN_ID + "=?")
                        .whereArgs(id)
                        .build())
                .prepare()
                .executeAsBlocking();
    }

    @NonNull
    private static PutResults<CityEntity> putImpl(List<CityEntity> cityEntities) {
        return storIOSQLite
                .put()
                .objects(cityEntities)
                .prepare()
                .executeAsBlocking();
    }

    @NonNull
    private static DeleteResult deleteImpl(CityEntity cityEntity) {
        return storIOSQLite
                .delete()
                .byQuery(DeleteQuery.builder()
                        .table(CitiesTable.TABLE)
                        .where(CitiesTable.COLUMN_CITY + "=?")
                        .whereArgs(cityEntity)
                        .build())
                .prepare()
                .executeAsBlocking();
    }


    @NonNull
    private static List<CityEntity> fromWeatherToBean(List<City> cities) {
        List<CityEntity> beans = new ArrayList<>();
        if (cities != null && !cities.isEmpty()) {
            Gson gson = new Gson();
            for (City wcity : cities) {
                beans.add(CityEntity.newCity(gson.toJson(wcity)));
            }
        }
        return beans;
    }

    @NonNull
    private static List<City> fromBeanToWeather(List<CityEntity> beans) {
        List<City> cityList = new ArrayList<>();
        if (beans != null && !beans.isEmpty()) {
            Gson gson = new Gson();
            for (CityEntity entity : beans) {
                cityList.add(gson.fromJson(entity.city(), City.class));
            }
        }
        return cityList;
    }
}
