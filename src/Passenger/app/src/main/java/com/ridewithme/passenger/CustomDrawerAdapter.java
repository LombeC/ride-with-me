package com.ridewithme.passenger;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sebastian on 7/13/2014.
 */
public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem> {
    Context context;
    List<DrawerItem> drawerItemList;
    int layoutResID;

    public CustomDrawerAdapter(Context context, int layoutResID,
                               List<DrawerItem> listItems){
        super(context, layoutResID, listItems);
        this.context = context;
        this.drawerItemList = listItems;
        this.layoutResID = layoutResID;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        DrawerItemHolder drawerHolder;
        View view = convertView;

        if(view == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            drawerHolder = new DrawerItemHolder();
            view = inflater.inflate(layoutResID, parent, false);

            drawerHolder.itemName = (TextView) view.
                    findViewById(R.id.itemName);
            drawerHolder.profileRating = (RatingBar) view.
                    findViewById(R.id.profileRating);
            drawerHolder.profilePicture = (ImageView) view.
                    findViewById(R.id.profilePicture);

            view.setTag(drawerHolder);
        } else {
            drawerHolder = (DrawerItemHolder) view.getTag();
        }

        DrawerItem dItem = (DrawerItem) this.drawerItemList.get(position);

        if (dItem.isProfile()){
            drawerHolder.itemName.setText(dItem.getItemName());
            drawerHolder.profileRating.setNumStars(5);
            drawerHolder.profileRating.setRating(dItem.getItemStartRating());
            drawerHolder.profilePicture.setImageDrawable(view.getResources().
                    getDrawable(dItem.getItemPictureResID()));

            drawerHolder.itemName.setVisibility(TextView.VISIBLE);
            drawerHolder.profileRating.setVisibility(RatingBar.VISIBLE);
            drawerHolder.profilePicture.setVisibility(ImageView.VISIBLE);
        } else {
            drawerHolder.itemName.setText(dItem.getItemName());

            drawerHolder.itemName.setVisibility(TextView.VISIBLE);
            drawerHolder.profileRating.setVisibility(RatingBar.INVISIBLE);
            drawerHolder.profilePicture.setVisibility(ImageView.INVISIBLE);
        }
        return view;
    }

    private static class DrawerItemHolder{
        TextView itemName;
        RatingBar profileRating;
        ImageView profilePicture;
    }
}
