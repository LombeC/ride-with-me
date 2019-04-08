package com.ridewithme.drivers;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
* Created by Sebastian on 7/14/2014.
*/
public class mapHelper {


    static public void restoreOnMapClickListener(GoogleMap map){
        map.setOnMapClickListener(null);
    }

    static public LatLng getCurrentLocation(Context context)
    {
        try
        {
            LocationManager locMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String locProvider = locMgr.getBestProvider(criteria, false);
            Location location = locMgr.getLastKnownLocation(locProvider);

            // getting GPS status
            boolean isGPSEnabled = locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            boolean isNWEnabled = locMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNWEnabled)
            {
                // no network provider is enabled
                return null;
            }
            else
            {
                // First get location from Network Provider
                if (isNWEnabled)
                    if (locMgr != null)
                        location = locMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled)
                    if (location == null)
                        if (locMgr != null)
                            location = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            return new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (NullPointerException ne)
        {
            Log.e("Current Location", "Current Lat Lng is Null");
            return new LatLng(0, 0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new LatLng(0, 0);
        }
    }

    static Marker getMapWithCurrentLocation(Context context, GoogleMap map){
        Marker marker;

        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(false);

        LatLng currentLocation = getCurrentLocation(context);
        marker = setMarker(currentLocation, map, "YOU ARE HERE");
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14));

        return marker;
    }

    public static Marker setMarker(LatLng marker, GoogleMap map, String title) {
        return (map.addMarker(new MarkerOptions().position(marker).title(title)));
    }

    public static Address getWhatsAround(Context context , Marker marker){
        List<Address> list;
        Geocoder geocoder;
        try {
            geocoder = new Geocoder(context, Locale.getDefault());
            list = geocoder.getFromLocation(marker.getPosition()
                    .latitude,marker.getPosition().longitude, 10);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getAddress(Context ctx, double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(ctx, Locale.getAvailableLocales()[20]);
            List addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = (Address) addresses.get(0);

                String exactAddress = address.getAddressLine(0);
                String locality = address.getLocality();

                result.append(exactAddress).append(", ").append(locality).append(".");
            }
            else
            {
                result.append("Address Not Available");
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
           // String error = "tag " + e.getMessage();
            result.append("Address Not Available");
            e.printStackTrace();
        }
        return result.toString();
    }

}
