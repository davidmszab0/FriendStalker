package com.naegling.assassins;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.naegling.assassins.lib.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Johan Nilsson
 */

public class ForgotPasswordActivity extends Activity {

    EditText editTextEmail;
    Button buttonNewPassword;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        buttonNewPassword = (Button) findViewById(R.id.buttonNewPassword);

        buttonNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editTextEmail.getText().toString();
                context = getApplicationContext();

                try {
                    UserFunctions userFunctions = new UserFunctions();
                    JSONObject json = userFunctions.newPassword(email);
                    int success = json.getInt("success");
                    if (success == 1) {
                        Toast.makeText(context, "A new password has been sent to your e-mail", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, json.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}