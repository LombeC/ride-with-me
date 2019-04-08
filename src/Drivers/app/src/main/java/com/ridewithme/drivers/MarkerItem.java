package com.ridewithme.drivers;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Sebastian on 7/14/2014.
 */
public class MarkerItem {
    private int markerPictureResId;
    private String markerProfileName;
    private Float markerRating;
    private String markerOrigin;
    private String markerDestination;
    private LatLng markerLatLngOrigin;
    private LatLng markerLatLngDestination;

    //GETTERS

    public int getMarkerPictureResId() {
        return markerPictureResId;
    }
    public String getMarkerProfileName() {
        return markerProfileName;
    }
    public Float getMarkerRating() {
        return markerRating;
    }
    public String getMarkerOrigin() {
        return markerOrigin;
    }
    public String getMarkerDestination() {
        return markerDestination;
    }
    public LatLng getMarkerLatLngOrigin() {
        return markerLatLngOrigin;
    }
    public LatLng getMarkerLatLngDestination() {
        return markerLatLngDestination;
    }

    //SETTERS

    public void setMarkerPictureResId(int markerPictureResId) {
        this.markerPictureResId = markerPictureResId;
    }
    public void setMarkerProfileName(String markerProfileName) {
        this.markerProfileName = markerProfileName;
    }
    public void setMarkerRating(Float markerRating) {
        this.markerRating = markerRating;
    }
    public void setMarkerOrigin(String markerOrigin) {
        this.markerOrigin = markerOrigin;
    }
    public void setMarkerDestination(String markerDestination) {
        this.markerDestination = markerDestination;
    }
    public void setMarkerLatLngOrigin(LatLng markerLatLngOrigin) {
        this.markerLatLngOrigin = markerLatLngOrigin;
    }
    public void setMarkerLatLngDestination(LatLng markerLatLngDestination) {
        this.markerLatLngDestination = markerLatLngDestination;
    }

    //Constructor

    public MarkerItem(int markerPictureResId, String markerProfileName, Float markerRating,
                      String markerArrivalTime, String markerFee, LatLng latLngOrigin, LatLng latLngDestination) {
        super();
        this.markerPictureResId = markerPictureResId;
        this.markerProfileName = markerProfileName;
        this.markerRating = markerRating;
        this.markerOrigin = markerArrivalTime;
        this.markerDestination = markerFee;
        this.markerLatLngOrigin = latLngOrigin;
        this.markerLatLngDestination = latLngDestination;
    }
}
