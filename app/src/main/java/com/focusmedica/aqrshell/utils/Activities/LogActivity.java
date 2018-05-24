package com.focusmedica.aqrshell.utils.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

public class LogActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister,btnCollection;
    private TextInputEditText inputCode;
    private TextInputEditText inputPassword;
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
        inputCode=(TextInputEditText)findViewById(R.id.coupon_id);

        inputPassword = (TextInputEditText)findViewById(R.id.password);
       btnLogin = (Button) findViewById(R.id.btnLogin);

       /* final ActionBar actionBar = getActionBar();
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
        actionBar.setCustomView(imageView);*/

        db = new SQLiteHandler(getApplicationContext());
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
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                String password = inputPassword.getText().toString().trim();
                String titleId=inputCode.getText().toString().trim();

                if ( !titleId.isEmpty()&!password.isEmpty()) {
                    if (!haveNetworkConnection()){
                        Toast.makeText(LogActivity.this, "No Connection", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                    }else{
                        login();
                        pDialog1 = new ProgressDialog(LogActivity.this);
                        pDialog1.setMessage("Please Wait...");
                        pDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        pDialog1.setCancelable(false);
                        pDialog1.show();
                    }

                } else {

                     Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();

                }
            }

        });

    /*      btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

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

                final Map<String, String> params = new HashMap<String, String>();
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
                                String name = jObj.getString("name");
                                String value = jObj.getString("value");
                                String appid = jObj.getString("app_folder");
                                String appInfo=jObj.getString("app_info");
                                String apptype=jObj.getString("app_type");
                                String appfolder=jObj.getString("app_folder");
                                int a00=Integer.parseInt(apptype);

                                db.addUser(name,value,appid,appInfo,apptype);

                                // Launch main activity
                                Intent intent = new Intent(LogActivity.this,
                                        CollectionsActivity.class);
                              //  intent.putExtra("name",name);
                             //   Toast.makeText(getApplicationContext(), "name=" +name, Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                              //finish();
                                hideDialog();
                            } catch (JSONException e) {
                                // JSON error
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Incorrect code", Toast.LENGTH_LONG).show();

                              }
                      }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
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

        }catch (Exception e){
            pDialog.dismiss();
            Toast.makeText(LogActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
