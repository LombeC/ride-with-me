package com.ridewithme.passenger;

import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Sebastian on 7/15/2014.
 */
public class informationHelper {
    public static Profile getProfile() {
        //TODO
        Profile profile = new Profile();

        profile.setEmail("cheviz@gmail.com");
        profile.setRating(4.5F);
        profile.setProfilePictureRefID(R.drawable.minion);
        return (profile);
    }

    public static boolean sendProfileInformation(Profile profile) {
        return (true);
    }


    public static boolean sendTripInfo(Marker destination) {

        return (true);
    }

    public static List<MarkerItem> getBidders(){
        List<MarkerItem> bidders = new ArrayList<MarkerItem>();

        bidders.add(new MarkerItem(R.drawable.profile_picture, "cheviz", 4.55F, "30", "50", new LatLng(39.949792, 116.334695), "Tesla model S"));
        bidders.add(new MarkerItem(R.drawable.minion, "Lomberto", 1.55F, "5", "100", new LatLng(39.949792, 116.434695), "Crappy car"));

        return bidders;
    }

    public static void notifyBidder(MarkerItem driver, String email) {

    }

    public static Boolean bookARide(String email, LatLng position, LatLng position1) {


        return null;
    }


}