package com.xgj.app.xgj;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    private EditText nameText;
    private EditText emailText;
    private EditText passwordText;
    private Button signupButton;
    private TextView loginLink;

    private ProgressDialog progressDialog;

    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressDialog = new ProgressDialog(SignUpActivity.this, R.style.AppTheme_Dark_Dialog);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        nameText = (EditText) findViewById(R.id.signup_input_name);
        emailText = (EditText) findViewById(R.id.signup_input_email);
        passwordText = (EditText) findViewById(R.id.signup_input_password);

        signupButton = (Button) findViewById(R.id.signup_btn_signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink = (TextView) findViewById(R.id.signup_link_login);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupButton.setEnabled(false);


        showProgressDialog();
        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();


        final Map<String, String> params = new HashMap<String, String>();
        params.put("email",email);
        params.put("firstName",name);
        params.put("lastName",name);
        params.put("password",password);

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        String url = XgjConfigs.API_Domain + XgjConfigs.API_URL_SIGNUP;

                        XgjJSONObjectRequest req = new XgjJSONObjectRequest
                                (Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject res) {
                                        try {
                                            Log.d(TAG, res.toString());

                                            session.setLogin(true);

                                            // Now store the user in SQLite
                                            String token = res.getString("token");

                                            JSONObject user = res.getJSONObject("user");
                                            String name = user.getString("name");
                                            String email = user.getString("email");

                                            db.addUser(name, email, token);

                                            onSignupSuccess();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // TODO Auto-generated method stub
                                        onSignupFailed();
                                    }

                                });
                        req.setTag(TAG);

                        AppController.getInstance().addToRequestQueue(req, TAG);
                    }
                }, 3000);
    }


    public void showProgressDialog(){
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sign up...");
        progressDialog.show();
    }

    public void hideProgressDialog(){
        progressDialog.dismiss();
    }


    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        hideProgressDialog();
        setResult(RESULT_OK, null);

        // User is already logged in. Take him to main activity
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign up failed", Toast.LENGTH_LONG).show();

        signupButton.setEnabled(true);

        hideProgressDialog();
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }
        return valid;
    }
}