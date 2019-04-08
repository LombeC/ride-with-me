package com.ridewithme.drivers;

public class DrawerItem {
    String itemName;
    int itemPictureResID;
    float itemStartRating;
    Boolean isProfile;

    //SETTERS
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public void setItemPictureResID(int itemPictureResID) {
        this.itemPictureResID = itemPictureResID;
    }
    public void setItemStartRating(float itemStartRating) {
        this.itemStartRating = itemStartRating;
    }

    //GETTERS
    public String getItemName() {
        return itemName;
    }
    public int getItemPictureResID() {
        return itemPictureResID;
    }
    public float getItemStartRating() {
        return itemStartRating;
    }
    public Boolean isProfile() {
        return isProfile;
    }

    //CONSTRUCTORS
    public DrawerItem(String itemName){
        super();
        this.setItemName(itemName);
        this.isProfile = Boolean.FALSE;
    }

    public DrawerItem(String itemName, float itemStarRating, int itemPictureResID){
        super();
        this.setItemName(itemName);
        this.setItemStartRating(itemStarRating);
        this.setItemPictureResID(itemPictureResID);
        this.isProfile = Boolean.TRUE;
    }

}
