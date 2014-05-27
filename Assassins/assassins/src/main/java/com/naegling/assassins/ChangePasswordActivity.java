package com.naegling.assassins;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
public class ChangePasswordActivity extends Activity {

    EditText inputCurrentPassword;
    EditText inputPassword;
    EditText inputPasswordConfirmation;
    Button buttonChangePassword;
    Intent intent;
    String uuid;
    String email;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        intent = getIntent();
        uuid = intent.getStringExtra("UID");
        email = intent.getStringExtra("EMAIL");

        inputCurrentPassword = (EditText) findViewById(R.id.editTextCurrentPassword);
        inputPassword = (EditText) findViewById(R.id.editTextPassword);
        inputPasswordConfirmation = (EditText) findViewById(R.id.editTextPasswordConfirm);
        buttonChangePassword = (Button) findViewById(R.id.buttonChangePassword);

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context = getApplicationContext();

                String currentPassword = inputCurrentPassword.getText().toString();
                String password = inputPassword.getText().toString();
                String passwordConfirm = inputPasswordConfirmation.getText().toString();

                if (password.length() < 8) {
                    Toast.makeText(context, "Password needs to be at least 8 characters long", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(passwordConfirm)) {
                    Toast.makeText(context, "Passwords are not equal", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        UserFunctions userFunction = new UserFunctions();
                        JSONObject json = userFunction.loginUser(email, currentPassword);
                        int success = json.getInt("success");
                        if (success == 1) {
                            json = userFunction.changePassword(uuid, password);
                            int res = json.getInt("success");
                            if (res == 1) {
                                Toast.makeText(context, "Password has been changed.", Toast.LENGTH_SHORT).show();
                                inputCurrentPassword.setText("");
                                inputPassword.setText("");
                                inputPasswordConfirmation.setText("");
                            } else {
                                Toast.makeText(context, json.getString("error_msg"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, json.getString("error_msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }


}
