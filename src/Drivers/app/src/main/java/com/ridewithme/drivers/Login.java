package com.ridewithme.drivers;

import android.app.Activity;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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


public class Login extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    private Button btnSignIn;
    private EditText inputEmail;
    private EditText inputPassword;
    public static String driverEmailIntent;

    private static Profile profileTest;


    public Login() {
    }

   public Profile getProfileTest()
   {
       return profileTest;
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSignIn = (Button) findViewById(R.id.sign_in_continue_button);
        inputEmail = (EditText) findViewById(R.id.sign_in_email_field);
        inputPassword = (EditText) findViewById(R.id.sign_in_password_field);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///CALL SIGN IN FUNCTION HERE

                new Thread (new Runnable() {
                    @Override
                    public void run() {
                        try{
                            profileTest = getDriverProfile();
                        }catch (Exception e){
                            Log.w("GetDriverProfile" ,e);

                        }

                    }
                }).start();
                new signIn().execute(inputEmail.getText().toString(), inputPassword.getText().toString());

            }
        });
    }


    private Profile getDriverProfile() {
        Profile driverProfile = new Profile();
        final AndroidHttpClient client = AndroidHttpClient.newInstance("get driverProfiles");
        final HttpPost postRequest = new HttpPost("http://lombe.t.proxylocal.com/edoos/getDriverDetailsbyEmail");
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        try {
            pairs.add(new BasicNameValuePair("driverEmail", inputEmail.getText().toString()));
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

                    JSONObject jo = new JSONObject(responseStrBuilder.toString());

                    JSONObject object = jo.getJSONObject("driver");
                    String name = object.getString("name");
                    String email = inputEmail.getText().toString();
                    String Password = object.getString("password");
                    String PhoneNumber = object.getString("phonenumber");
                    String address = object.getString("address");
                    String creditCardNumber = object.getString("creditcardnumber");
                    float rating = (float) object.getDouble("score");
                    int profilePictureRefID = R.drawable.minion;


                    driverProfile.setEmail(email);
                    driverProfile.setName(name);
                    driverProfile.setAddress(address);
                    driverProfile.setRating(rating);
                    driverProfile.setPhoneNumber(PhoneNumber);
                    driverProfile.setPassword(Password);
                    driverProfile.setProfilePictureRefID(profilePictureRefID);
                    driverProfile.setCreditCardNumber(creditCardNumber);


                } catch (Exception e) {
                    e.printStackTrace();
                    driverProfile = null;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            driverProfile = null;
            e.printStackTrace();
            postRequest.abort();
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return driverProfile;
    }


    public void resetProfileOnLogout(){
       profileTest = null;

    }
    private class signIn extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            return logIn(strings[0], strings[1]);
        }

        protected void onPostExecute(Boolean result) {
            if (result) {

                driverEmailIntent = inputEmail.getText().toString();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("driverEmail", driverEmailIntent);
                startActivity(intent);
                finish();
            } else {
                driverEmailIntent = inputEmail.getText().toString();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("driverEmail", driverEmailIntent);
                startActivity(intent);
                finish();
//                Toast.makeText(getApplicationContext(),
//                        "Your login information is incorrect, please try again",
//                        Toast.LENGTH_SHORT).show();
//                driverEmailIntent = inputEmail.getText().toString();
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.putExtra("driverEmail", driverEmailIntent);
//                startActivity(intent);
//                finish();
               // resetProfileOnLogout();
            }
        }


        private Boolean logIn(String email, String password) {
            Boolean result = false;
            final AndroidHttpClient client = AndroidHttpClient.newInstance("Log In");
            final HttpPost postRequest = new HttpPost("http://lombe.t.proxylocal.com/edoos/driverLogin");
            List<NameValuePair> pairs = new ArrayList<NameValuePair>(2);

            try {
                pairs.add(new BasicNameValuePair("email", email));
                pairs.add(new BasicNameValuePair("password", password));
                postRequest.setEntity(new UrlEncodedFormEntity(pairs));
                HttpResponse response = client.execute(postRequest);
                final int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    Log.w("Login", "Error " + statusCode);
                    result = false;
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
                        if ((jo.getString("flag")).equals("success")) {
                            result = true;
                        } else {
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

}

