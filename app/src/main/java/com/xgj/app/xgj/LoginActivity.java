package com.xgj.app.xgj;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int REQUEST_SIGNUP = 0;

    private static final String GET_TOKEN = "GET_TOKEN";
    private static final String VERFIFY_TOKEN = "VERIFY_TOKEN";

    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;
    private TextView signupLink;

    private SessionManager session;
    private SQLiteHandler db;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);


        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());


        emailText = (EditText) findViewById(R.id.login_email);
        passwordText = (EditText) findViewById(R.id.login_password);

        loginButton = (Button) findViewById(R.id.login_btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink = (TextView) findViewById(R.id.login_link_signup);
        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            String email = emailText.getText().toString();
            Map<String, String> params = db.getUserDetails(email);
            getOrVerifyToken(VERFIFY_TOKEN, params);
        }
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        showProgressDialog();

        final String email = emailText.getText().toString();
        final String password = passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("userIdentity",email);
                        params.put("password",password);

                        getOrVerifyToken(GET_TOKEN, params);
                    }
                }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }


    public void getOrVerifyToken(String method, Map<String, String> params){
        String url = "";
        if(method.equals(GET_TOKEN)){
            url = XgjConfigs.API_Domain + XgjConfigs.API_URL_LOGIN;
        }else if(method.equals(VERFIFY_TOKEN)){
            url = XgjConfigs.API_Domain + XgjConfigs.API_URL_AUTHORIZE;
        }else{
            onLoginFailed();
        }

        XgjJSONObjectRequest req = new XgjJSONObjectRequest
                (Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject res) {
                        //No need ot add user into the local database again?
                        Log.d(TAG, res.toString());
                        session.setLogin(true);
                        onLoginSuccess();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        onLoginFailed();
                    }

                });
        req.setTag(TAG);

        AppController.getInstance().addToRequestQueue(req, TAG);

    }

    public void showProgressDialog(){
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
    }

    public void hideProgressDialog(){
        progressDialog.dismiss();
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        hideProgressDialog();

        // User is already logged in. Take him to main activity, put the email address tranvers to the main activity page
        Bundle bundle = new Bundle();
        bundle.putString("email", emailText.getText().toString());
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
        hideProgressDialog();
    }

    public boolean validate() {
        boolean valid = true;
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

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