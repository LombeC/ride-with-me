package com.ridewithme.passenger;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Sebastian on 7/14/2014.
 */
public class MarkerItem {
    private int markerPictureResId;
    private String markerProfileName;
    private Float markerRating;
    private String markerArrivalTime;
    private String markerFee;
    private LatLng markerLatLng;
    private String markerCarInformation;


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
    public String getMarkerArrivalTime() {
        return markerArrivalTime;
    }
    public String getMarkerFee() {
        return markerFee;
    }
    public LatLng getMarkerLatLng() {
        return markerLatLng;
    }
    public String getMarkerCarInformation() {
        return markerCarInformation;
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
    public void setMarkerArrivalTime(String markerArrivalTime) {
        this.markerArrivalTime = markerArrivalTime;
    }
    public void setMarkerFee(String markerFee) {
        this.markerFee = markerFee;
    }
    public void setMarkerLatLng(LatLng markerLatLng) {
        this.markerLatLng = markerLatLng;
    }
    public void setMarkerCarInformation(String markerCarInformation) {
        this.markerCarInformation = markerCarInformation;
    }
    //Constructor


    public MarkerItem(int markerPictureResId, String markerProfileName, Float markerRating,
                      String markerArrivalTime, String markerFee, LatLng latLng, String carInformation) {
        super();
        this.markerPictureResId = markerPictureResId;
        this.markerProfileName = markerProfileName;
        this.markerRating = markerRating;
        this.markerArrivalTime = markerArrivalTime;
        this.markerFee = markerFee;
        this.markerLatLng = latLng;
        this.markerCarInformation = carInformation;
    }
}
