package com.example.rohitbhawal.digitalassistant;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    String LOG_TAG = "LoginActivity1";
    String username = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button signin = (Button) findViewById(R.id.button11);
        Button register = (Button) findViewById(R.id.button12);

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), New_User.class);
                startActivityForResult(myIntent, 0);
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                final EditText user =(EditText) findViewById(R.id.editText13);
                final EditText pass =(EditText) findViewById(R.id.editText14);
                final String[] cred = new String[2];

                cred[0] = user.getText().toString();
                cred[1] = pass.getText().toString();

                Context context = view.getContext();
                CharSequence message;

                int duration = Toast.LENGTH_SHORT;

                CheckLogin loginTask = new CheckLogin();
                try{
                    Boolean login = loginTask.execute(cred).get();

                    Log.v(LOG_TAG, "LoginSuccessVariable: "+ login);
                    if (login){
                        message = "Login Successful";
                        Toast.makeText(context, message, duration).show();
                        Intent myIntent = new Intent(view.getContext(), Menu.class);
                        myIntent.putExtra("userid", cred[0]);
                        myIntent.putExtra("username", username);
                        startActivityForResult(myIntent, 0);
                    }
                    else{
                        message = "Login Failed";
                        Toast.makeText(context, message, duration).show();
                    }
                }catch (Exception e)  {
                    Log.e(LOG_TAG,"Login Error: "+ e);
                }


            }
        });
    }

    public class CheckLogin extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
//            Log.v(LOG_TAG, "Cred Param: "+params[0] + "   "+params[1]);
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String resultJsonStr = "";


            String baseURL = "http://" + getString(R.string.aws_link) + "/executeQuery";
            String query = "select * from User " +
                    "where userid = '" + params[0] + "'"+
                    " and pasw = '" + params[1] + "'";
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
                    return Boolean.FALSE;
                } else {
                    Log.v(LOG_TAG, "RECORDS FOUND");
                    try{
                        JSONArray arr = new JSONArray(resultJsonStr);
                        username = arr.getJSONObject(0).getString("name");
                    }catch (JSONException j){
                        Log.e(LOG_TAG, "JSON Read Error ", j);
                        return Boolean.FALSE;
                    }

                    return Boolean.TRUE;
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
