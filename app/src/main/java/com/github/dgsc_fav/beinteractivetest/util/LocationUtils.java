package com.github.dgsc_fav.beinteractivetest.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.List;
import java.util.Locale;

/**
 * Created by DG on 22.10.2016.
 */

public class LocationUtils {
    public static final int WHAT_FAIL = 0;
    public static final int WHAT_SUCCESS = 1;
    public static final String KEY_SUCCESS_RESULT = "address";
    public static final String KEY_LOCATION = "location";

    public static void getAddress(final Context context, final Location location,
            final Handler handler) {

        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                Address address = null;
                try {
                    List<Address> list = geocoder.getFromLocation(
                            location.getLatitude(), location.getLongitude(), 1/*maxResults*/);
                    if (list != null && !list.isEmpty()) {
                        address = list.get(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Message msg = Message.obtain();
                    msg.setTarget(handler);
                    Bundle bundle = new Bundle();
                    // вернём запрошенную локацию
                    bundle.putParcelable(KEY_LOCATION, location);

                    if (address != null) {
                        // если определилось
                        msg.what = WHAT_SUCCESS;
                        // отправим адрес
                        bundle.putParcelable(KEY_SUCCESS_RESULT, address);
                    } else {
                        // иначе fail
                        msg.what = WHAT_FAIL;
                    }

                    msg.setData(bundle);

                    msg.sendToTarget();
                }
            }
        };
        thread.start();
    }

    public static String getAddressString(Address address) {
        if (address == null) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        int lineIndex = address.getMaxAddressLineIndex();
//        if(lineIndex > 2) {
//            lineIndex = 2;
//        }
        for (; lineIndex-- > 0; ) {
            stringBuilder.append(address.getAddressLine(lineIndex)).append(",");
        }

        //return address.getAddressLine(0) + ", " + address.getLocality();
        return stringBuilder.toString();
    }
}
