package com.myraxsoft.cashless;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import static android.view.View.GONE;

public class Register extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    EditText first_name, last_name, email, pwd, confirm_pwd;
    Button btnRegister ;//= (Button) findViewById(R.id.btnRegister);
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btnToLogin = (Button) findViewById(R.id.btnToLogin);
        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });

        /*++++++++++ CREATE USER +++++++++*/
        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        email = (EditText) findViewById(R.id.email);
        pwd = (EditText) findViewById(R.id.pwd);
        confirm_pwd = (EditText) findViewById(R.id.confirm_pwd);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               createUser();
            }
        });
        /*++++++++++ END CREATE USER +++++++++*/
    }

    //mula
    //inner class to perform network request extending an AsyncTask
    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        //the url where we need to send the request
        String url;

        //the parameters
        HashMap<String, String> params;

        //the request code to define whether it is a GET or POST
        int requestCode;

        //constructor to initialize values
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        //when the task started displaying a progressbar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }


        //this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(GONE);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    //refreshing the herolist after every operation
                    //so we get an updated list
                    //we will create this method right now it is commented
                    //because we haven't created it yet --> ok dah unkomen!
                    //refreshHeroList(object.getJSONArray("heroes"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //the network operation will be performed in background
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }
    //tamat

    public void goToLogin(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }




    private void createUser() {
        String create_fname = first_name.getText().toString().trim();
        String create_lname = last_name.getText().toString().trim();
        String create_email = email.getText().toString().trim();
        String create_pwd   = pwd.getText().toString().trim();
        String create_cpwd  = confirm_pwd.getText().toString().trim();

        //validating the inputs
        if (TextUtils.isEmpty(create_fname)) {
            first_name.setError("Please enter first name");
            first_name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(create_lname)) {
            last_name.setError("Please enter last name");
            last_name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(create_email)) {
            email.setError("Please enter valid email");
            email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(create_pwd)) {
            pwd.setError("Please enter valid email");
            pwd.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(create_cpwd)) {
            confirm_pwd.setError("Please confirm email");
            confirm_pwd.requestFocus();
            return;
        }

        //if validation passes

        HashMap<String, String> params = new HashMap<>();
        params.put("create_fname", create_fname);
        params.put("create_lname", create_lname);
        params.put("create_email", create_email);
        params.put("create_pwd", create_pwd);
        params.put("create_cpwd", create_cpwd);


        //Calling the create hero API
        PerformNetworkRequest request = new PerformNetworkRequest(ApiConfig.URL_REGISTER_USER, params, CODE_POST_REQUEST);
        request.execute();
    }

}
