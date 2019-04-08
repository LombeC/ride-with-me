package com.ridewithme.passenger;

import android.app.Activity;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class Register extends Activity {


    public static Profile profile = new Profile();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        final EditText registration_name =
                (EditText) findViewById(R.id.registration_name);
        final EditText registration_email =
                (EditText) findViewById(R.id.registration_email);
        final EditText registration_password =
                (EditText) findViewById(R.id.registration_password);
        final EditText registration_phone_number =
                (EditText) findViewById(R.id.registration_phone_number);
        final EditText registration_address =
                (EditText) findViewById(R.id.registration_address);
        final EditText registration_credit_card =
                (EditText) findViewById(R.id.registration_credit_card);
        final EditText registration_confirm_password =
                (EditText) findViewById(R.id.registration_Confirm_Password);
        Button registration_select_picture_button =
                (Button) findViewById(R.id.registration_select_picture_button);
        Button registration_submit_button =
                (Button) findViewById(R.id.btn_submit_registration);


        registration_select_picture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///call function to select picture here
                //TODO
                Toast.makeText(view.getContext(),
                        "SELECT PICTURE FRAGMENT", Toast.LENGTH_SHORT).show();
            }
        });

        registration_submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (registration_confirm_password.getText().toString().equals(registration_password.getText().toString()))
                {
                    profile.setName(registration_name.getText().toString());
                    profile.setEmail(registration_email.getText().toString());
                    profile.setPassword(registration_password.getText().toString());
                    profile.setPhoneNumber(registration_phone_number.getText().toString());
                    profile.setAddress(registration_address.getText().toString());
                    profile.setCreditCardNumber(registration_credit_card.getText().toString());
                    new serverRegister().execute();

                }
                else
                {
                    Toast.makeText(view.getContext(),"Please make sure the two passwords match",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
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

    private class serverRegister extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            return doRegistration();
        }

        @Override
        protected void onPostExecute(String result){
            if (result.equals("success")) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            } else if (result.equals("email Already exists. Please enter another one.")) {
                Toast.makeText(getApplicationContext(),
                        "This email Already exists in our System. Either login or enter another email.",
                        Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(getApplicationContext(),
                        "Registration Failed",
                        Toast.LENGTH_SHORT).show();
            }
        }

        private String doRegistration (){
            String result = "";
            final AndroidHttpClient client = AndroidHttpClient.newInstance("Log In");
            final HttpPost postRequest = new HttpPost("http://lombe.t.proxylocal.com/edoos/register");
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();

            try{
                pairs.add(new BasicNameValuePair("email", profile.getEmail()));
                pairs.add(new BasicNameValuePair("password", profile.getPassword()));
                pairs.add(new BasicNameValuePair("Name", profile.getName()));
                pairs.add(new BasicNameValuePair("Phone", profile.getPhoneNumber()));
                pairs.add(new BasicNameValuePair("Address", profile.getAddress()));
                pairs.add(new BasicNameValuePair("CreditCardNo", profile.getCreditCardNumber()));
                pairs.add(new BasicNameValuePair("Picture", null));
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
                        result = (jo.getString("flag")).toString();
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
