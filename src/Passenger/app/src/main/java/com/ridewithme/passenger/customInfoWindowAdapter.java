package com.ridewithme.passenger;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

public class customInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    LayoutInflater inflater=null;
    HashMap<Marker, MarkerItem> markerList;

    customInfoWindowAdapter(LayoutInflater inflater, HashMap<Marker, MarkerItem> markerList) {
        this.inflater=inflater;
        this.markerList = markerList;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = inflater.inflate(R.layout.display_window,null);

        ImageView markerImage =
                (ImageView) view.findViewById(R.id.markerImage);
        TextView markerProfileName =
                (TextView) view.findViewById(R.id.markerProfileName);
        RatingBar markerRating =
                (RatingBar) view.findViewById(R.id.markerRating);
        TextView markerCar =
                (TextView) view.findViewById(R.id.markerCarInformation);
        TextView markerArrivalTime =
                (TextView) view.findViewById(R.id.markerArrivalTime);
        TextView markerFee =
                (TextView) view.findViewById(R.id.markerFee);
        MarkerItem markerItem = (MarkerItem) this.markerList.get(marker);

        markerImage.setImageDrawable(view.getResources().
                getDrawable(markerItem.getMarkerPictureResId()));
        markerProfileName.setText(markerItem.getMarkerProfileName());
        markerRating.setNumStars(5);
        markerRating.setRating(markerItem.getMarkerRating());
        markerCar.setText(markerItem.getMarkerCarInformation());
        markerArrivalTime.setText(markerItem.getMarkerArrivalTime());
        markerFee.setText(markerItem.getMarkerFee());

        return view;
    }
}
