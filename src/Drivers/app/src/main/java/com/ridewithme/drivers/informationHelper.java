package com.ridewithme.drivers;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian on 7/15/2014.
 */
public class informationHelper extends Activity {



    public Profile getProfile() {

             //TODO
        Profile profile = new Profile();
        Login login = new Login();
        if (login.getProfileTest() != null)
        {
            profile = login.getProfileTest();
        }
        else{
            profile.setEmail(Login.driverEmailIntent);
            profile.setRating(4.5F);
            profile.setProfilePictureRefID(R.drawable.minion);
        }
        return profile;
    }

    public static boolean sendProfileInformation(Profile profile) {
        return (true);
    }


    public static boolean sendTripInfo(Marker destination) {

        return (true);
    }

    public static List<MarkerItem> getPassengers(){
        List<MarkerItem> bidders = new ArrayList<MarkerItem>();

        bidders.add(new MarkerItem(R.drawable.profile_picture, "cheviz", 4.55F, "Here", "There", new LatLng(39.949792, 116.334695), new LatLng(39.949792, 116.434695)));
        bidders.add(new MarkerItem(R.drawable.minion, "Lomberto", 1.55F, "There", "Here", new LatLng(39.949792, 116.434695), new LatLng(39.949792, 116.334695)));

        return bidders;
    }

    public static void notifyBidder(MarkerItem driver) {
        //TODO
    }

}