package com.github.dgsc_fav.beinteractivetest.provider;

import android.content.Context;

import com.github.dgsc_fav.beinteractivetest.weather.api.model.City;
import com.github.dgsc_fav.beinteractivetest.weather.api.model.Coord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DG on 23.10.2016.
 */

public class CitiesProvider {

    Context mContext;
    // потом будет с базы
    List<City> mCityList;

    static volatile CitiesProvider sCitiesProvider;

    private CitiesProvider(Context context) {
        mContext = context;
        mCityList = new ArrayList<>();
        // generate mock
        fillMockData();
    }


    public static CitiesProvider getInstanse(Context context) {
        if (sCitiesProvider == null) {
            synchronized (CitiesProvider.class) {
                if (sCitiesProvider == null) {
                    sCitiesProvider = new CitiesProvider(context);
                }
            }
        }

        return sCitiesProvider;
    }

    public List<City> getCityList() {
        return mCityList;
    }

    public void setCityList(
            List<City> cityList) {
        mCityList = cityList;
    }


    private void fillMockData() {
        City city = new City(0, "My Place", new Coord(Double.NaN, Double.NaN), "", 0);
        mCityList.add(city);
        city = new City(2643741, "City of London", new Coord(-0.091840, 51.512791), "GB", 0);
        mCityList.add(city);
        city = new City(1850147, "Tokyo", new Coord(139.691711, 35.689499), "JP", 0);
        mCityList.add(city);
        city = new City(5128581, "New York City", new Coord(-74.005966, 40.714272), "US", 0);
        mCityList.add(city);
    }
}
