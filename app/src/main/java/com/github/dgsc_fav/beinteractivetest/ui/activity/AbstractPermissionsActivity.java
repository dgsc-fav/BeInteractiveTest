package com.github.dgsc_fav.beinteractivetest.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.dgsc_fav.beinteractivetest.R;


/**
 * Created by DG on 22.10.2016.
 */
public abstract class AbstractPermissionsActivity extends AppCompatActivity {
    public static final int PERMISSIONS_REQUEST_ACCESS_LOCATION = 100;

    /**
     * Продолжение работы, когда permission даны
     */
    public abstract void processWithPermissionsGranted();

    /**
     * Продолжение работы, когда permission не даны
     */
    public abstract void processWithPermissionsDenied();

    public void checkLocationServicePermissions() {
        if(ActivityCompat.checkSelfPermission(this,
                                              Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat
                                                                                                                                        .checkSelfPermission(
                                                                                                                                                this,
                                                                                                                                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                                                                   Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat
                                                                                                                        .shouldShowRequestPermissionRationale(
                                                                                                                                this,
                                                                                                                                Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Display UI and wait for user interaction
                showDialog(R.string.dialog_location_permission_message,
                           new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   requestLocationPermission();
                               }
                           },
                           new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   processWithPermissionsDenied();
                               }
                           });
            } else {
                requestLocationPermission();
            }
        } else {
            processWithPermissionsGranted();
        }
    }

    public void showDialog(int messageId, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(messageId);
        alertDialog.setPositiveButton(android.R.string.ok, okListener);
        if(cancelListener != null) {
            alertDialog.setNegativeButton(android.R.string.cancel, cancelListener);
        }
        alertDialog.show();
    }

    protected void finishWithDialog() {
        showDialog(R.string.about_location_permissions_info, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }, null);
    }

    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        }, PERMISSIONS_REQUEST_ACCESS_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSIONS_REQUEST_ACCESS_LOCATION) {
            if(grantResults.length == 2 &&
                       grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                       grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // права есть. продолжаем работу
                processWithPermissionsGranted();
            } else {
                // прав нет. завершение работы
                Toast.makeText(this,
                               getString(R.string.permission_location_denied),
                               Toast.LENGTH_SHORT).show();

                processWithPermissionsDenied();
            }

            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
