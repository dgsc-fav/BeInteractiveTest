package com.github.dgsc_fav.beinteractivetest.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by DG on 22.10.2016.
 */

public class LocationUtils {

    public static Address getAddress(Context context, Location location) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1
                                                               /*maxResults*/);
            if(addresses != null && !addresses.isEmpty()) {
                return addresses.get(0);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAddressString(Address address) {
        if(address == null) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        int lineIndex = address.getMaxAddressLineIndex();
//        if(lineIndex > 2) {
//            lineIndex = 2;
//        }
        for(; lineIndex-- > 0;) {
            stringBuilder.append(address.getAddressLine(lineIndex)).append(",");
        }
        return stringBuilder.toString();
    }


}
