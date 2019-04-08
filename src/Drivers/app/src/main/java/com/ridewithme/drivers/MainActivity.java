package com.ridewithme.drivers;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
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
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private GoogleMap map;
    private MarkerItem passenger;
    private AsyncTask<String,Void, List<MarkerItem>> passengers;
    private  String driverEmailIntent;
    EditText ETA;
    EditText fee;
    Button CancelBid;
    Button SubmitBid ;
    private String FeeIntent;
    private String sendBidResult;
    private Login login = new Login();
    private haveIBeenChosen haveI;


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private Button mainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();
        driverEmailIntent =  i.getStringExtra("driverEmail");
        mainButton = (Button) findViewById(R.id.mainActivityButton);
        mainButton.setText("Look for Passengers");

        mTitle = getTitle();

        map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.mainMap)).getMap();

        mapHelper.getMapWithCurrentLocation(this, map);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //TODO add what happens when the driver tap on the info window...
                map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        map.setInfoWindowAdapter(null);
                    }
                });
            }
        });

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainButton.getText().equals("We are there!")) {
                    weAreThere();
                }
                if (mainButton.getText().equals("Picked up the passenger")) {
                    pickedUpPassenger();
                }
                if (mainButton.getText().equals("Look for Passengers")) {
                     lookForPassengers();
                }
            }
        });
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

                Intent i = new Intent(getApplicationContext(),Login.class);
                startActivity(i);
                login.resetProfileOnLogout();
//                 haveI.cancel(true);

                MainActivity.this.finish();

                break;
        }
    }


    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause(){
        super.onPause();
       // haveI.cancel(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //login.resetProfileOnLogout();
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

    public void lookForPassengers() {

        passengers = new getPassengerList().execute();
    }

    public void pickedUpPassenger() {
        new IPickedThePassenger().execute();
        map.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(map.addMarker(new MarkerOptions().position(passenger.getMarkerLatLngOrigin()).title("Picked the passenger here")).getPosition());
        builder.include(map.addMarker(new MarkerOptions().position(passenger.getMarkerLatLngDestination()).title("Destination")).getPosition());
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 230);
        map.animateCamera(cu);

        mainButton.setText("We are there!");
    }

    public void weAreThere() {
        Intent startReview = new Intent(getApplicationContext(), PassengerReview.class);
        FeeIntent = fee.getText().toString();
        startReview.putExtra("fee_intent",FeeIntent);
        startReview.putExtra("driver_email",driverEmailIntent);
        startActivity(startReview);
        finish();
    }


    public void AfterBidOnPassenger(MarkerItem passenger)
    {
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
            }
        });

        map.clear();
        map.setInfoWindowAdapter(null);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include((mapHelper.setMarker(passenger.getMarkerLatLngOrigin(), map, passenger.getMarkerProfileName())).getPosition());
        builder.include(mapHelper.getCurrentLocation(getApplicationContext()));
        LatLngBounds bounds = builder.build();
        int padding = 230;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        map.animateCamera(cu);
        Toast.makeText(getApplicationContext(), passenger.getMarkerProfileName() +
                " is waiting for you", Toast.LENGTH_SHORT).show();
        mainButton.setText("Picked up the passenger");
    }

    private class getPassengerList extends AsyncTask<String, Void, List<MarkerItem>> {


        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "We are searching for a list of available passengers. Please Hold", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected List<MarkerItem> doInBackground(String... strings) {
            return getAvailablePassengers();
        }

        @Override
        protected void onPostExecute(List<MarkerItem> passengers) {

            int size = passengers.size();
            if (size > 0) {
            map.clear();
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            mapHelper.restoreOnMapClickListener(map);



            final HashMap<Marker, MarkerItem> markers = new HashMap<Marker, MarkerItem>();

                for (MarkerItem markerItem : passengers) {
                    markers.put(
                            mapHelper.setMarker(markerItem.getMarkerLatLngOrigin(), map, markerItem.
                                    getMarkerProfileName()), markerItem
                    );
                }

                for (Marker marker : markers.keySet()) {
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
                        passenger = markers.get(marker);

                        //Bidding
                        final Dialog biddingDialog = new Dialog(MainActivity.this);
                        biddingDialog.setContentView(R.layout.bid_on_passenger_dialog);
                        // Sets the size of the dialog
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(biddingDialog.getWindow().getAttributes());
                        lp.width = 500;
                        lp.height = 345;


                        biddingDialog.setTitle("Bidding");

                        fee = (EditText) biddingDialog.findViewById(R.id.bidding_fee);
                        ETA = (EditText) biddingDialog.findViewById(R.id.bidding_time_to_arrival);
                        CancelBid = (Button) biddingDialog.findViewById(R.id.btnBiddingCancel);
                       SubmitBid = (Button) biddingDialog.findViewById(R.id.btnBiddingSubmit);

                        CancelBid.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                biddingDialog.dismiss();
                            }
                        });

                        SubmitBid.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                new SendBidToPassenger().execute(passenger);
                                biddingDialog.dismiss();
                            }
                        });
                        biddingDialog.show();
                        biddingDialog.getWindow().setAttributes(lp);
                    }
                });

            } else {
                Toast.makeText(getApplicationContext(),
                        "No passengers available, please try again later", Toast.LENGTH_LONG).show();
                mainButton.setText("Look for Passengers");
            }

        }

        private List<MarkerItem> getAvailablePassengers() {
            List<MarkerItem> result = new ArrayList<MarkerItem>();
            final AndroidHttpClient client = AndroidHttpClient.newInstance("get passengers");
            final HttpPost postRequest = new HttpPost("http://lombe.t.proxylocal.com/edoos/lookForPassengers");
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            try {
                pairs.add(new BasicNameValuePair("driverEmail", driverEmailIntent));//email));
                postRequest.setEntity(new UrlEncodedFormEntity(pairs));
                HttpResponse response = client.execute(postRequest);

                final int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    Log.w("Login", "Error " + statusCode);
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

                        JSONArray passengersArray = jo.getJSONArray("Available Passengers");
                        for (int i = 0; i < passengersArray.length(); i++) {
                            JSONObject object = passengersArray.getJSONObject(i);
                            LatLng hereLatLng = new LatLng(object.getDouble("originlat"), object.getDouble("originlng"));
                            LatLng thereLatLng = new LatLng(object.getDouble("destinationlat"), object.getDouble("destinationlng"));

                            double hereLat  = hereLatLng.latitude;
                            double hereLng = hereLatLng.longitude;

                            double thereLat =thereLatLng.latitude;
                            double thereLng = thereLatLng.longitude;


                            String here = mapHelper.getAddress(MainActivity.this.getApplicationContext(),hereLat,hereLng);
                            String there = mapHelper.getAddress(MainActivity.this.getApplicationContext(),thereLat,thereLng);
                            MarkerItem mi = new MarkerItem(R.drawable.minion,
                                    object.getString("email"),
                                    (float) object.getDouble("rating"),here, there,
                                    new LatLng(object.getDouble("originlat"), object.getDouble("originlng")),
                                    new LatLng(object.getDouble("destinationlat"), object.getDouble("destinationlng")));
                            result.add(mi);
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
            } catch (Exception e) {
                e.printStackTrace();
                postRequest.abort();
            } finally {
                if (client != null) {
                    client.close();
                }
            }
          return result;
        }
    }


    private class SendBidToPassenger extends AsyncTask<MarkerItem, Void, String>{

        @Override
        protected void onPreExecute(){
            Toast.makeText(getApplicationContext(),"Please wait whilst we attempt to send your bid to the passenger",Toast.LENGTH_SHORT).show();
        }


        @Override
        protected String doInBackground(MarkerItem... markerItems) {
            return sendBidToPassenger(markerItems);
        }

        @Override
        protected void onPostExecute(String result)
        {
            sendBidResult = result;
            if (sendBidResult.equals("success")){
                Toast.makeText(getApplicationContext(),"Bid successfully delivered. Wait a few moments for the passenger's response", Toast.LENGTH_SHORT).show();
                //AfterBidOnPassenger(passenger);
                mainButton.setEnabled(false);
                mainButton.setText("Waiting for Passenger response...");
                map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                    }
                });
                haveI = new haveIBeenChosen();
                haveI.execute();
//
//                if (MainActivity.this.isFinishing()){
//                    haveI.cancel(true);
//                }
//                if (MainActivity.this.isDestroyed()){
//                    haveI.cancel(true);
//                }
//


            }
            else if (sendBidResult.equals("failed: driver already has a bid.")){
                Toast.makeText(getApplicationContext(),"You have already bid for another passenger. Wait for their response", Toast.LENGTH_SHORT).show();
                mainButton.setEnabled(false);
                mainButton.setText("Waiting for Passenger response...");
                map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                    }
                });
                map.clear();
                haveI = new haveIBeenChosen();
                haveI.execute();
//                if (MainActivity.this.isFinishing()){
//                    haveI.cancel(true);
//                }
//
//                if (MainActivity.this.isDestroyed()){
//                    haveI.cancel(true);
//                }

            }
            else {
                Toast.makeText(getApplicationContext(),"Bidding failed.Try again in a few minutes", Toast.LENGTH_SHORT).show();
            }
        }

        private String sendBidToPassenger(MarkerItem[] markerItems)
        {
            String result = "";
            final AndroidHttpClient client = AndroidHttpClient.newInstance("Send bid");
            final HttpPost postRequest = new HttpPost("http://lombe.t.proxylocal.com/edoos/choosingPassengersAndBid");
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            try {
                String timeLeft = ETA.getText().toString();
                String proposedFee = fee.getText().toString();
                String driverEmail = driverEmailIntent;
                LatLng driverLocation = mapHelper.getCurrentLocation(getApplicationContext());
                double driverLocationLat = driverLocation.latitude;
                double driverLocationLng = driverLocation.longitude;
                pairs.add(new BasicNameValuePair("driverEmail", driverEmail));
                pairs.add(new BasicNameValuePair("passengerEmail", markerItems[0].getMarkerProfileName().toString()));//email));
                pairs.add(new BasicNameValuePair("timeTillArrival",timeLeft));
                pairs.add(new BasicNameValuePair("fee", proposedFee));
                pairs.add(new BasicNameValuePair("driverLocationLat",Double.toString(driverLocationLat)));
                pairs.add(new BasicNameValuePair("driverLocationLng", Double.toString(driverLocationLng)));
                postRequest.setEntity(new UrlEncodedFormEntity(pairs));


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
                        result = jo.getString("flag");

                    } catch (Exception e) {
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
    }


    private class haveIBeenChosen extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String result ="";
            int responseFromServerLoopControl = 1;
            int responseFromServer;
            final AndroidHttpClient client = AndroidHttpClient.newInstance("Log In");
            final HttpPost postRequest = new HttpPost("http://lombe.t.proxylocal.com/edoos/checkIfPassenerPickedMe");
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            try {
                pairs.add(new BasicNameValuePair("driverEmail", driverEmailIntent));//email));
                postRequest.setEntity(new UrlEncodedFormEntity(pairs));
                while(responseFromServerLoopControl == 1)
                {
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
                            JSONObject jo = new JSONObject(responseStrBuilder.toString());
                            result = jo.getString("flag");
                            responseFromServer = Integer.parseInt(result);

                            if (responseFromServer == 2){
                                responseFromServerLoopControl = 2;
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"You have been selected by the passenger. Congratulations", Toast.LENGTH_SHORT).show();
                                        AfterBidOnPassenger(passenger);
                                        mainButton.setEnabled(true);
                                    }
                                });


                            } else if(responseFromServer == 3) {
                                responseFromServerLoopControl = 3;
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"The Passenger Selected Another Driver", Toast.LENGTH_SHORT).show();
                                        map.clear();
                                        mapHelper.getMapWithCurrentLocation(getApplicationContext(),map);
                                        mainButton.setEnabled(true);
                                        mainButton.setText("Look for Passengers");
                                    }
                                });


                            }else {
                                responseFromServerLoopControl = 1;
                                synchronized (this){
                                    this.wait(20000);
                                    if( MainActivity.this.isFinishing())
                                    {
                                        responseFromServerLoopControl = 4;
                                    }
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

    }




    private class IPickedThePassenger extends AsyncTask<String, Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean result = false;
            final AndroidHttpClient client = AndroidHttpClient.newInstance("Passenger picked");
            final HttpPost postRequest = new HttpPost("http://lombe.t.proxylocal.com/edoos/passPicked");
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            try {
                pairs.add(new BasicNameValuePair("driverEmail", driverEmailIntent));//email));
                postRequest.setEntity(new UrlEncodedFormEntity(pairs));


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
                            if(jo.getString("flag").toString().equals("success")){
                                result = true;
                            }else {
                                result = false;
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

