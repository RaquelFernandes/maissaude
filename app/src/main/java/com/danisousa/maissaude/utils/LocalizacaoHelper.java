package com.danisousa.maissaude.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

public class LocalizacaoHelper {

    public static final int REQUEST_LOCATION = 0;

    public static void getLocalizacao(Activity activity, Fragment fragment) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = { Manifest.permission.ACCESS_FINE_LOCATION };
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fragment.requestPermissions(permissions, REQUEST_LOCATION);
            }

        } else {
            LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setHorizontalAccuracy(Criteria.ACCURACY_MEDIUM);
            locationManager.requestSingleUpdate(criteria, (LocationListener) fragment, null);
        }
    }

    public static Double calcularDistancia(LatLng origem, LatLng destino) {
        return SphericalUtil.computeDistanceBetween(origem, destino);
    }

    public static String formatarMetros(double metros) {
            if (metros < 1000) {
                return ((int) metros) + "m";
            } else if (metros < 10000) {
                return formatarDecimal(metros / 1000, 1) + "km";
            } else {
                return ((int) (metros / 1000)) + "km";
            }
    }

    private static String formatarDecimal(double val, int dec) {
        int factor = (int) Math.pow(10, dec);

        int front = (int) (val);
        int back = (int) Math.abs(val * (factor)) % factor;

        return front + "," + back;
    }
}
