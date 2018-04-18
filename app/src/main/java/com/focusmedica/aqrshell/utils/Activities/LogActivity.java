package com.focusmedica.aqrshell.utils.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.focusmedica.aqrshell.ApiCaller;
import com.focusmedica.aqrshell.R;
import com.focusmedica.aqrshell.ServiceApi;
import com.focusmedica.aqrshell.dbHandler.SQLiteHandler;
import com.focusmedica.aqrshell.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class LogActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister,btnCollection;
    private EditText inputEmail,inputCode;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    String email,password,titleId;
    ProgressDialog pDialog1;
      String  name,value,appid,appInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        inputCode=(EditText)findViewById(R.id.coupon_id);
        inputPassword = (EditText) findViewById(R.id.password);
       btnLogin = (Button) findViewById(R.id.btnLogin);
      /*  btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
       btnLinkToRegister.setVisibility(View.INVISIBLE);*/
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(actionBar.getDisplayOptions() | ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.fmlogo);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        findViewById(R.id.collbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),CollectionsActivity.class);
                startActivity(i);
            }
        });
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LogActivity.this, CollectionsActivity.class);
            startActivity(intent);
            finish();
        }
        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String titleId=inputCode.getText().toString().trim();

                if ( !titleId.isEmpty()&!password.isEmpty()) {
                    login();
                    pDialog1 = new ProgressDialog(LogActivity.this);
                    pDialog1.setMessage("Please Wait...");
                    pDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pDialog1.setCancelable(false);
                    pDialog1.show();

                } else {

                     Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();

                }
            }

        });
/*
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });*/
    }
    void login(){
        try{

            titleId=inputCode.getText().toString().trim();
            password=inputPassword.getText().toString().trim();
            //  if (!titleId.isEmpty() && !password.isEmpty() && !titleId.isEmpty()){
            //
            if (!titleId.isEmpty()&!password.isEmpty()){
                ServiceApi serviceApi = ApiCaller.getInstance().getServicesApi();

                Map<String, String> params = new HashMap<String, String>();
                //  params.put("code", voucher);
                params.put("username", titleId);
                params.put("password",password);


                Call<String> call = serviceApi.getLogingResult(params);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                        String result = response.body();
                        Log.d("@@@", "res=" + result);
                        //Toast.makeText(getApplicationContext(), "res=" +result, Toast.LENGTH_SHORT).show();


                        if (result.equalsIgnoreCase(""+result)) {
                            pDialog1.dismiss();
                            Log.d(TAG, result);

                            try {
                                JSONObject jObj = new JSONObject(result);
                                // boolean error = jObj.getBoolean("error");

                                // Check for error node in json
                                // if (!error) {
                                // user successfully logged in
                                //    session.setLogin(true);

                                // Now store the user in SQLite
                                //  String uid = jObj.getString("uid");

                                // JSONObject user = jObj.getJSONObject("res");
                                String name = jObj.getString("name");
                                String value = jObj.getString("value");
                                String appid = jObj.getString("app_id");
                                String appInfo=jObj.getString("app_info");
                                String apptype=jObj.getString("app_type");

                                  db.addUser(name,value,appid,appInfo,apptype);
                                // Launch main activity
                                Intent intent = new Intent(LogActivity.this,
                                        CollectionsActivity.class);
                                intent.putExtra("name",name);
                                Toast.makeText(getApplicationContext(), "name=" +name, Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                finish();
                                hideDialog();

                            } catch (JSONException e) {
                                // JSON error
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Incorrect code", Toast.LENGTH_LONG).show();

                                //Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                      } else {

                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                        //   Toast.makeText(getApplicationContext(), "onFailure=fail", Toast.LENGTH_SHORT).show();

                    }
                });
            }
            else {
                pDialog1.dismiss();
                Toast.makeText(LogActivity.this, "username or password is not entered", Toast.LENGTH_SHORT).show();
            }

        }catch (NullPointerException e){
            pDialog1.dismiss();
            e.printStackTrace();
            Toast.makeText(LogActivity.this, "code is incorrect", Toast.LENGTH_SHORT).show();

        }
    }

    private void callRetrieve() {
        ServiceApi serviceApi = ApiCaller.getInstance().getServicesApi();
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", titleId);
        params.put("password",password);
        //params.put("email",email);
        Call<String> call = serviceApi.getLogOut(params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    String res = response.errorBody().string();
                    if (res.equalsIgnoreCase("0 results")) {
                        pDialog1.dismiss();
                        Toast.makeText(LogActivity.this, "please enter valid credential", Toast.LENGTH_LONG).show();
                    } else {
                        session.setLogin(true);
                        //response(name,value,appid,appInfo);
                        /*Intent intent = new Intent(LogActivity.this, CollectionsActivity.class);
                        intent.putExtra("titleId",res);
                        Log.d("@@@", "title=" + res);
                        startActivity(intent);*/
                        finish();
                        pDialog1.dismiss();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (NullPointerException e){

                    e.printStackTrace();
                    pDialog1.dismiss();
                    Toast.makeText(LogActivity.this, "incorrect code", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {


            }
        });
    }


    /**
     * function to verify login details in mysql db
     * */

    /*
 private void response(String name,String value,String appid,String appInfo ) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                       // JSONObject user = jObj.getJSONObject("res");
                          String name = jObj.getString("name");
                        String value = jObj.getString("value");
                        String appid = jObj.getString("app_id");
                        String appInfo=jObj.getString("app_info");

                            db.addUser(name,value,appid,appInfo);
                        // Launch main activity
                        Intent intent = new Intent(LogActivity.this,
                                CollectionsActivity.class);
                        Toast.makeText(LogActivity.this, "value==="+name+value+appid+appInfo, Toast.LENGTH_SHORT).show();
                        intent.putExtra("name",name);
                        startActivity(intent);
                        finish();
                        hideDialog();
                    } else {
                        hideDialog();
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Login Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", email);
                params.put("password", password);
               // params.put("titleId",titleId);
                return params;
            }

        };

        // Adding request to request queue
        ApplicationController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
*/
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}

