package com.github.dgsc_fav.beinteractivetest.provider;

import com.github.dgsc_fav.beinteractivetest.db.table.CitiesTable;

/**
 * Константы для {@link DbContentProvider}
 * Created by DG on 23.10.2016.
 */

public class DbContract {
    public static final String PATH_CITIES = CitiesTable.TABLE;
    public static final int URI_MATCHER_CODE_CITIES = 1;
    public static final String PATH_CURRENT_WEATHER = "cweather";
    public static final int URI_MATCHER_CODE_CURRENT_WEATHER = 2;
    public static final String PATH_DAILY_WEATHER = "dweather";
    public static final int URI_MATCHER_CODE_DAILY_WEATHER = 3;
}
