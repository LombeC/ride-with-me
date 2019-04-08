package com.ridewithme.drivers;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sebastian on 7/15/2014.
 * Modified by Lombe 01/08/2014
 */
public class Profile implements Parcelable {
    private String name;
    private String email;
    private String Password;
    private String PhoneNumber;
    private String address;
    private String creditCardNumber;
    private float rating;
    private int profilePictureRefID;


    public Profile() {

    }

    //GETTERS

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return Password;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public Float getRating() {
        return rating;
    }

    public int getProfilePictureRefID() {
        return profilePictureRefID;
    }

    //SETTERS

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setProfilePictureRefID(int profilePictureRefID) {
        this.profilePictureRefID = profilePictureRefID;
    }

    //Parcel part

    public Profile(Parcel in)
    {
        String[] data = new String[8];

        in.readStringArray(data);

        this.name = data[0];
        this.email = data[1];
        this.Password = data[2];
        this.PhoneNumber = data[3];
        this.address = data[4];
        this.creditCardNumber = data[5];
        this.rating = Float.parseFloat(data[6]);
        this.profilePictureRefID = Integer.parseInt(data[7]);

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{this.name, this.email,this.Password , this.PhoneNumber,this.address,this.creditCardNumber, String.valueOf(this.rating),String.valueOf(this.profilePictureRefID)});
    }

    public static final Parcelable.Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel parcel) {
            return new Profile(parcel);
        }

        @Override
        public Profile[] newArray(int i) {
            return new Profile[i];
        }
    };
}
