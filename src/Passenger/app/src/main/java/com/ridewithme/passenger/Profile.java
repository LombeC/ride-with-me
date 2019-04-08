package com.ridewithme.passenger;

/**
 * Created by Sebastian on 7/15/2014.
 */
public class Profile {
    private String name;
    private String email;
    private String Password;
    private String PhoneNumber;
    private String address;
    private String creditCardNumber;
    private float rating;
    private int profilePictureRefID;

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
}
