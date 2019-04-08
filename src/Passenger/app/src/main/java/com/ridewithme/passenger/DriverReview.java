package com.ridewithme.passenger;

import android.app.Activity;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DriverReview extends Activity {

    Button btnSubmitReview;
    String email;
    android.widget.RatingBar RatingBar;
    TextView fee;
    String feeFromOtherActivity;
    Double rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_review);

        Intent i = getIntent();
        email = i.getStringExtra("user_email");
        feeFromOtherActivity = i.getStringExtra("fee_intent");
        RatingBar = (RatingBar) findViewById(R.id.ratingBar);
        fee = (TextView) findViewById(R.id.textViewFeeAmount);
        fee.setText(feeFromOtherActivity);



        btnSubmitReview = (Button) findViewById(R.id.btnSubmitReview);
        btnSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                // Add code that submits the review
                new submitReview().execute();

                Intent BackToMain = new Intent();
                boolean result = true;
                BackToMain.putExtra("result",result);
                setResult(RESULT_OK, BackToMain);



                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.driver_review, menu);
        return true;
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

    private class submitReview extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            final AndroidHttpClient client = AndroidHttpClient.newInstance("Rating");
            final HttpPost postRequest = new HttpPost("http://lombe.t.proxylocal.com/edoos/ratingAndViewFee");
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            try {


                rating = Double.valueOf(RatingBar.getRating());

                pairs.add(new BasicNameValuePair("Email", email));//email));
                pairs.add(new BasicNameValuePair("DriverRatingStarNumber", String.valueOf(rating)));
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
                        result = jo.getString("flag");
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



    }
}
