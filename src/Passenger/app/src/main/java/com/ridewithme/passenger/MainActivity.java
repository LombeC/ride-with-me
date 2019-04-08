package com.ridewithme.passenger;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks{

    private GoogleMap map;
    private Marker destination;
    private Marker origin;
    private String email;
    private MarkerItem driver;
    private String bookRideResult;
    private bookARide ride;
    private AsyncTask<String, Void, List<MarkerItem>> drivers;
   // private String emailIntent;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;
    private Button mainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainButton = (Button) findViewById(R.id.mainActivityButton);
        mainButton.setText("Pick me up!");
        mainButton.setMinHeight(0);

        Intent i = getIntent();
        email = i.getStringExtra("userEmail");

        mTitle = getTitle();

        map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.mainMap)).getMap();
        //mapHelper.getMapWithCurrentLocation(getApplicationContext(),map);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                onMapClickTakeMeThere(latLng);
            }
        });

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                mainButton.callOnClick();
            }
        });
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainButton.getText() == "I'm There!") {

                    goToReview();
                }
                if (mainButton.getText() == "Hurry Up!") {
                    Toast.makeText(getApplicationContext(),
                            mainButton.getText(), Toast.LENGTH_SHORT).show();
                    hurryUp();
                }
                if (mainButton.getText().equals("Pick me up!")) {
                    if (destination != null) {
                        Toast.makeText(getApplicationContext(), "Please wait while we process your request", Toast.LENGTH_SHORT).show();

                        ride = new bookARide();
                        ride.execute(email, origin.getPosition(), destination.getPosition());

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please select your destination.", Toast.LENGTH_SHORT).show();
                    }
                }
                if (mainButton.getText().equals("Check for driver bids")){
                    pickMeUp();
                }
            }
        });
        (origin = mapHelper.getMapWithCurrentLocation(this, map)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        origin.showInfoWindow();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                break;
            case 2:
                Intent i = new Intent(getApplicationContext(),Update_Profile.class);
                startActivity(i);
                break;
            case 3:
                MainActivity.this.finish();
                break;
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_main, container, false);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

   public void goToReview()
   {
       new IGotThere().execute(email);//email);
       Intent i = new Intent(getApplicationContext(),DriverReview.class);
       String feeIntent = driver.getMarkerFee().toString();
       i.putExtra("fee_intent", feeIntent);
       i.putExtra("user_email", email);
       startActivityForResult(i,1);

    }



    // Performed when the program comes back from the Review page
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data)
    {
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                imThere();
            }
            if (resultCode == RESULT_CANCELED)
            {
                imThere();
            }
        }
    }

    public void imThere(){
        mainButton.setText("Pick me up!");
        map.clear();
        mapHelper.getMapWithCurrentLocation(this, map);
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Take me here.");
                map.clear();
                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                (destination = map.addMarker(markerOptions)).showInfoWindow();
            }
        });
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                mainButton.callOnClick();
            }
        });
        (origin = mapHelper.getMapWithCurrentLocation(getApplicationContext(), map))
                .setIcon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        origin.showInfoWindow();
    }

    public void hurryUp(){
        Toast.makeText(this, "Hurry up sent.", Toast.LENGTH_SHORT).show();
    }

    public void pickMeUp(){
        ride.cancel(true);
        drivers = new getDrivers().execute(email);
    }

    public void onMapClickTakeMeThere(LatLng latLng){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Take me here.");
        map.clear();
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        (destination = map.addMarker(markerOptions)).showInfoWindow();
    }


    private class bookARide extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute() {
            mainButton.setEnabled(false);
            map.setOnInfoWindowClickListener(null);
            map.setOnMapClickListener(null);
        }

        @Override
        protected String doInBackground(Object... objects) {
            return bookRide((String) objects[0], (LatLng) objects[1], (LatLng) objects[2]);
        }

        //@Override
        protected void onPostExecute(String result){
            bookRideResult = result;
            mainButton.setEnabled(true);
            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    mainButton.callOnClick();
                }
            });
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Take me here.");
                    map.clear();
                    map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    (destination = map.addMarker(markerOptions)).showInfoWindow();
                }
            });
            if (bookRideResult.equals("success")) {
               // pickMeUp();
                mainButton.setText("Check for driver bids");
                map.clear();
                Toast.makeText(getApplicationContext(),
                        "Check for Driver bids in a few moments please..",
                        Toast.LENGTH_SHORT).show();
            }else if (bookRideResult.equals("failed: error1")){
                map.clear();
                mainButton.setText("Check for driver bids");
                Toast.makeText(getApplicationContext(),
                        "You are Already on the waiting List",
                        Toast.LENGTH_SHORT).show();

            }
            else if (bookRideResult.equals("failed: error2")){
                Toast.makeText(getApplicationContext(),
                        "You are not in the system. Please register",
                        Toast.LENGTH_SHORT).show();
            } else
            {
                Toast.makeText(getApplicationContext(),
                        "Booking Failed",
                        Toast.LENGTH_SHORT).show();
            }
        }

        private String bookRide(String mail, LatLng origin, LatLng destination){
            String result = "";
            final AndroidHttpClient client = AndroidHttpClient.newInstance("Log In");
            final HttpPost postRequest = new HttpPost("http://lombe.t.proxylocal.com/edoos/bookARide");
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            try{
                pairs.add(new BasicNameValuePair("email", mail));
                pairs.add(new BasicNameValuePair("originLat", Double.toString(origin.latitude)));
                pairs.add(new BasicNameValuePair("originLng", Double.toString(origin.longitude)));
                pairs.add(new BasicNameValuePair("destinationLat", Double.toString(destination.latitude)));
                pairs.add(new BasicNameValuePair("destinationLng", Double.toString(destination.longitude)));
                postRequest.setEntity(new UrlEncodedFormEntity(pairs));
                HttpResponse response = client.execute(postRequest);
                final int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    Log.w("Login", "Error " + statusCode);
                }
                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = null;
                    try{
                        inputStream = entity.getContent();
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder responseStrBuilder = new StringBuilder();
                        String inputStr;
                        while ((inputStr = reader.readLine()) != null){
                            responseStrBuilder.append(inputStr);
                        }
                        JSONObject jo =
                                new JSONObject(responseStrBuilder.toString());
                        result = jo.getString("flag").toString();
                    }catch (Exception e){
                        e.printStackTrace();
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            }catch  (Exception e){
                e.printStackTrace();
                postRequest.abort();
            }finally {
                client.close();
            }
            return result;
        }
    }

    private class getDrivers extends AsyncTask<String, Void, List<MarkerItem>>{

        @Override
        protected void onPreExecute(){
            Toast.makeText(getApplicationContext(), "A trained monkey is retrieving the list of drivers available", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected List<MarkerItem> doInBackground(String... strings) {
            return getBiddingDrivers(strings[0]);
        }

        @Override
        protected void onPostExecute(List<MarkerItem> bidders){
            int size =  bidders.size();
            if (size > 0){
            map.clear();
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            mapHelper.restoreOnMapClickListener(map);

            final HashMap<Marker, MarkerItem> markers = new HashMap<Marker, MarkerItem>();
            for (MarkerItem markerItem : bidders) {
                markers.put(
                            mapHelper.setMarker(markerItem.getMarkerLatLng(), map, markerItem.
                                    getMarkerProfileName()), markerItem);
                }

                for (Marker marker : markers.keySet()){
                    builder.include(marker.getPosition());
                }
                LatLngBounds bounds = builder.build();
                int padding = 230;
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                map.animateCamera(cu);

                map.setInfoWindowAdapter(new customInfoWindowAdapter(getLayoutInflater(), markers));
                map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        driver = markers.get(marker);
                        Toast.makeText(getApplicationContext(), driver.getMarkerProfileName() +
                                " will pick you up in " + driver.getMarkerArrivalTime() +
                                " minutes", Toast.LENGTH_SHORT).show();


                        new notifyDriver().execute(email, driver.getMarkerProfileName());


                        mainButton.setText("Hurry Up!");
                        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {

                            }
                        });
                        map.clear();
                        map.setInfoWindowAdapter(null);
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include((mapHelper.setMarker(origin.getPosition(), map, origin.getTitle())).getPosition());
                        builder.include((mapHelper.setMarker(destination.getPosition(), map, destination.getTitle())).getPosition());
                        builder.include((mapHelper.setMarker(driver.getMarkerLatLng(), map, driver.getMarkerProfileName())).getPosition());
                        LatLngBounds bounds = builder.build();
                        int padding = 230;
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                        map.animateCamera(cu);
                    }
                });
            }else{
                Toast.makeText(getApplicationContext(),
                        "No drivers available, please try again later", Toast.LENGTH_LONG).show();
               // mainButton.setText("Pick me up!");
            }
        }

        private List<MarkerItem> getBiddingDrivers(String address) {
            List<MarkerItem> result = new ArrayList<MarkerItem>();
            final AndroidHttpClient client = AndroidHttpClient.newInstance("get drivers");
            final HttpPost postRequest = new HttpPost("http://lombe.t.proxylocal.com/edoos/seeDriverDetails");
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            try{
                pairs.add(new BasicNameValuePair("Email", email));//address));
                postRequest.setEntity(new UrlEncodedFormEntity(pairs));
                HttpResponse response = client.execute(postRequest);

                final int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    Log.w("Login", "Error " + statusCode);
                }

                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = null;
                    try{
                        inputStream = entity.getContent();
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder responseStrBuilder = new StringBuilder();

                        String inputStr;
                        while ((inputStr = reader.readLine()) != null){
                            responseStrBuilder.append(inputStr);
                        }
                        JSONObject jo =
                                new JSONObject(responseStrBuilder.toString());

                        JSONArray driversArray = jo.optJSONArray("Driver");
                        for(int i = 0; i < driversArray.length(); i++ ){
                            JSONObject object = driversArray.getJSONObject(i);
                            MarkerItem mi = new MarkerItem(R.drawable.minion,
                                    object.getString("email"),
                                    (float) object.getDouble("rating"),
                                    object.getString("pickuptime"),
                                    object.getString("price"),
                                    new LatLng(object.getDouble("driverlocationlat"), object.getDouble("driverlocationlng")),
                                    object.getString("carmodel"));

                            result.add(mi);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                postRequest.abort();
            }finally {
                if (client != null) {
                    client.close();
                }
            }

            return result;
        }
    }

    private class notifyDriver extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            final AndroidHttpClient client = AndroidHttpClient.newInstance("Log In");
            final HttpPost postRequest = new HttpPost("http://lombe.t.proxylocal.com/edoos/choosingADriver");
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            try {
                pairs.add(new BasicNameValuePair("Email", strings[0]));//email));
                pairs.add(new BasicNameValuePair("DriverEmail", strings[1]));
                postRequest.setEntity(new UrlEncodedFormEntity(pairs));
                HttpResponse response = client.execute(postRequest);
                final int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                }

                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = null;
                    try{
                        inputStream = entity.getContent();
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder responseStrBuilder = new StringBuilder();

                        String inputStr;
                        while ((inputStr = reader.readLine()) != null){
                            responseStrBuilder.append(inputStr);
                        }
                        JSONObject jo =
                                new JSONObject(responseStrBuilder.toString());
                        result = jo.getString("flag").toString();
                    }catch (Exception e){
                        e.printStackTrace();
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            }catch  (Exception e){
                e.printStackTrace();
                postRequest.abort();
            }finally {
                if (client != null) {
                    client.close();
                }
            }
        return result;
        }

        @Override
        protected void onPostExecute(String result){
            Log.w("Calling:", "Was I picked Up?");
            new wasIPickedUp().execute(email);//email);
        }
    }

    private class wasIPickedUp extends AsyncTask<String, Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean result = false;
            final AndroidHttpClient client = AndroidHttpClient.newInstance("Log In");
            final HttpPost postRequest = new HttpPost("http://lombe.t.proxylocal.com/edoos/checkIfTripHasStarted");
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            try {
                pairs.add(new BasicNameValuePair("passengerEmail", strings[0]));//email));
                postRequest.setEntity(new UrlEncodedFormEntity(pairs));

                while(!result) {
                    HttpResponse response = client.execute(postRequest);
                    final int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode != HttpStatus.SC_OK) {
                    }
                    final HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream inputStream = null;
                        try {
                            inputStream = entity.getContent();
                            BufferedReader reader =
                                    new BufferedReader(new InputStreamReader(inputStream));
                            StringBuilder responseStrBuilder = new StringBuilder();

                            String inputStr;
                            while ((inputStr = reader.readLine()) != null) {
                                responseStrBuilder.append(inputStr);
                            }
                            JSONObject jo =
                                    new JSONObject(responseStrBuilder.toString());
                            if(jo.getString("flag").toString().equals("started")){
                                result = true;
                            }else {
                                this.wait(5000);
                                if (MainActivity.this.isFinishing())
                                {
                                    this.cancel(true);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            entity.consumeContent();
                        }
                    }
                }
            }catch  (Exception e){
                e.printStackTrace();
                postRequest.abort();
            }finally {
                if (client != null) {
                    client.close();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){
            mainButton.setText("I'm There!");
            this.cancel(true);
        }
    }

    private class IGotThere extends AsyncTask<String, Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean result = false;
            final AndroidHttpClient client = AndroidHttpClient.newInstance("Log In");
            final HttpPost postRequest = new HttpPost("http://lombe.t.proxylocal.com/edoos/checkIfTripHasStarted");
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            try {
                pairs.add(new BasicNameValuePair("passengerEmail", strings[0]));//email));
                postRequest.setEntity(new UrlEncodedFormEntity(pairs));

                while(!result) {
                    HttpResponse response = client.execute(postRequest);
                    final int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode != HttpStatus.SC_OK) {
                    }
                    final HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream inputStream = null;
                        try {
                            inputStream = entity.getContent();
                            BufferedReader reader =
                                    new BufferedReader(new InputStreamReader(inputStream));
                            StringBuilder responseStrBuilder = new StringBuilder();

                            String inputStr;
                            while ((inputStr = reader.readLine()) != null) {
                                responseStrBuilder.append(inputStr);
                            }
                            JSONObject jo =
                                    new JSONObject(responseStrBuilder.toString());
                            if(jo.getString("flag").toString().equals("started")){
                                result = true;
                            }else {
                                this.wait(500);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            entity.consumeContent();
                        }
                    }
                }
            }catch  (Exception e){
                e.printStackTrace();
                postRequest.abort();
            }finally {
                if (client != null) {
                    client.close();
                }
            }
            return result;
        }
    }
}
