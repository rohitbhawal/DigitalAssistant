package com.example.rohitbhawal.digitalassistant;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class New_User extends AppCompatActivity {
    String LOG_TAG = "New_User";

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button fab = (Button) findViewById(R.id.button);

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText tid=(EditText) findViewById(R.id.editText);
                EditText name=(EditText) findViewById(R.id.editText2);
                EditText mno=(EditText) findViewById(R.id.editText3);
                EditText eid=(EditText) findViewById(R.id.editText4);
                EditText pass=(EditText) findViewById(R.id.editText5);
                EditText pass1=(EditText) findViewById(R.id.editText6);
                String tid1=tid.getText().toString();
                String name1=name.getText().toString();
                String mno1=mno.getText().toString();
                String eid1=eid.getText().toString();
                String pass2=pass.getText().toString();
                String pass3=pass1.getText().toString();
                if(!pass2.equals(pass3)){
                    pass1.setError("Password not matching");
                    pass1.requestFocus();
                }
                else if(pass2.isEmpty()){
                    pass1.setError("Password Cannot be Empty");
                    pass1.requestFocus();
                }
                else {
                    checkUser loginTask = new checkUser();
                    try{
                        Boolean login = loginTask.execute(tid1).get();
                        if(login){
                            Intent int1 = new Intent(New_User.this, New_User2.class);
                            int1.putExtra("text1", tid1);
                            int1.putExtra("text2", name1);
                            int1.putExtra("text3", mno1);
                            int1.putExtra("text4", eid1);
                            int1.putExtra("text5", pass2);
                            startActivity(int1);
                        }
                        else{
                            tid.setError("User ID already Taken");
                            tid.requestFocus();
                        }
                    }catch (Exception e){
                        Log.e(LOG_TAG,"Login Error: "+ e);
                    }
                }
            }
        });
    }

    public class checkUser extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String resultJsonStr = "";


            String baseURL = "http://" + getString(R.string.aws_link) + "/executeQuery";
            String query = "select * from User " +
                    "where userid = '" + params[0] + "'";
            String Query_PARAM = "query";
            try {
                Uri testUri = Uri.parse(baseURL).buildUpon()
                        .appendQueryParameter(Query_PARAM, query).build();
                Log.v(LOG_TAG, "URL: " + testUri.toString());
                URL url = new URL(testUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return Boolean.FALSE;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return Boolean.FALSE;
                }

                resultJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Result: " + resultJsonStr);

                if (resultJsonStr.contains("Error")) {
                    Log.v(LOG_TAG, "EMPTY");
                    return Boolean.TRUE;
                } else {
                    Log.v(LOG_TAG, "RECORDS FOUND");
                    return Boolean.FALSE;
                }

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return Boolean.FALSE;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
        }
    }
}
