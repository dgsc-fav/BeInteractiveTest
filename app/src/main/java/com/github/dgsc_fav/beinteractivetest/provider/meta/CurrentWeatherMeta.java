package com.github.dgsc_fav.beinteractivetest.provider.meta;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.github.dgsc_fav.beinteractivetest.provider.DbContentProvider;

/**
 * Created by DG on 23.10.2016.
 */

public class CurrentWeatherMeta {
    @NonNull
    public static final String URI_STRING =
            "content://" + DbContentProvider.AUTHORITY + "/cweather";

    @NonNull
    public static final Uri CONTENT_URI = Uri.parse(URI_STRING);
}
