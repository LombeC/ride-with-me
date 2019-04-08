package com.ridewithme.passenger;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Update_Profile extends Activity {

    Button btnUpdateProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        btnUpdateProfile =  (Button) findViewById(R.id.btnUpdateProfile);
        final EditText Update_name =
                (EditText) findViewById(R.id.EditTextUpdateName);
        final EditText Update_password =
                (EditText) findViewById(R.id.EditTextUpdatePassword);
        final EditText Update_PhoneNumber =
                (EditText) findViewById(R.id.EditTextUpdatePhoneNumber);
        final EditText Update_Address =
                (EditText) findViewById(R.id.EditTextUpdateAddress);
        final EditText Update_CreditCard =
                (EditText) findViewById(R.id.EditTextUpdateCreditCard);
        Button btnUpdatePicture =
                (Button) findViewById(R.id.btnUpdatePicture);

        btnUpdatePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ///call function to select picture here
                //TODO
                Toast.makeText(view.getContext(),
                        "SELECT PICTURE FRAGMENT", Toast.LENGTH_SHORT).show();
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO
//                ///call function to update profile on database here
                Profile profile = Register.profile;
                if (!Update_name.getText().toString().isEmpty())
                {
                    profile.setName(Update_name.getText().toString());
                }
                if (!Update_password.getText().toString().isEmpty())
                {
                    profile.setPassword(Update_password.getText().toString());
                }
                if (!Update_PhoneNumber.getText().toString().isEmpty())
                {
                    profile.setPhoneNumber(Update_PhoneNumber.getText().toString());
                }
                if (!Update_Address.getText().toString().isEmpty())
                {
                    profile.setAddress(Update_Address.getText().toString());
                }
                if (!Update_CreditCard.getText().toString().isEmpty())
                {
                    profile.setCreditCardNumber(Update_CreditCard.getText().toString());
                }
                Toast.makeText(getApplicationContext(),"Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.update__profile, menu);
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
}
