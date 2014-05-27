package com.naegling.assassins;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.*;
import com.naegling.assassins.lib.DatabaseHandler;
import com.naegling.assassins.lib.UserFunctions;

/**
 * @author Johan Nilsson
 */

public class RegisterActivity extends Activity {

    Button btnRegister;
    Button btnLinkToLogin;
    EditText inputFullName;
    EditText inputEmail;
    EditText inputPassword;
    EditText inputConfirmPassword;
    TextView registerErrorMsg;

    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Importing all assets like buttons, text fields
        inputFullName = (EditText) findViewById(R.id.registerName);
        inputEmail = (EditText) findViewById(R.id.registerEmail);
        inputPassword = (EditText) findViewById(R.id.registerPassword);
        inputConfirmPassword = (EditText) findViewById(R.id.registerConfirmPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String name = inputFullName.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                String passwordConfirm = inputConfirmPassword.getText().toString();

                if (password.length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password needs to be at least 8 characters long", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(passwordConfirm)) {
                    Toast.makeText(getApplicationContext(), "Passwords are not equal", Toast.LENGTH_SHORT).show();
                } else {
                    UserFunctions userFunction = new UserFunctions();
                    JSONObject json = userFunction.registerUser(name, email, password);


                    // check for login response
                    try {
                        if (json.getString(KEY_SUCCESS) != null) {
                            String res = json.getString(KEY_SUCCESS);
                            if (Integer.parseInt(res) == 1) {
                                // user successfully registred
                                // Store user details in SQLite Database
                                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                JSONObject json_user = json.getJSONObject("user");

                                // Clear all previous data in database
                                userFunction.logoutUser(getApplicationContext());
                                db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL),
                                        json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));
                                HashMap<String, String> user = db.getUserDetails();
                                JSONObject jsonTar = userFunction.userInTarget(user.get(KEY_UID));
                                // Launch Dashboard Screen
                                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                                // Close all views before launching Dashboard
                                mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(mainActivity);
                                // Close Registration Screen
                                finish();
                            } else {
                                // Error in registration
                                Toast.makeText(getApplicationContext(), json.getString(KEY_ERROR_MSG), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
