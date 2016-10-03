package com.example.rohitbhawal.digitalassistant;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class New_User2 extends AppCompatActivity {
    String text2,text3,text4,text5,text6;
    String LOG_TAG = "New_User2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__user2);
        /*EditText tid=(EditText) findViewById(R.id.editText);
        EditText name=(EditText) findViewById(R.id.editText2);
        EditText mno=(EditText) findViewById(R.id.editText3);
        EditText eid=(EditText) findViewById(R.id.editText4);
        EditText pass=(EditText) findViewById(R.id.editText5);
        EditText pass1=(EditText) findViewById(R.id.editText6);*/
        TextView det=(TextView) findViewById(R.id.textView);
        Intent int2=getIntent();
        text2=int2.getStringExtra("text1");
        text3=int2.getStringExtra("text2");
        text4=int2.getStringExtra("text3");
        text5=int2.getStringExtra("text4");
        text6=int2.getStringExtra("text5");
        det.setText("User ID:"+text2+"\n\n"+"Name:"+text3+"\n\n"+"Mobile No:"+text4+"\n\n"+"Email ID:"+text5);

        Button conf=(Button) findViewById(R.id.button9);
        conf.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String data[] = new String[5];
                data[0] = text2;
                data[1] = text3;
                data[2] = text4;
                data[3] = text5;
                data[4] = text6;

                Context context = view.getContext();
                CharSequence message;
                int duration = Toast.LENGTH_SHORT;

                InsertUser insertData = new InsertUser();
                try{
                    Boolean success = insertData.execute(data).get();

                    Log.v(LOG_TAG, "LoginSuccessVariable: "+ success);
                    if (success){
                        message = "User Created Successfully";
                        Toast.makeText(context, message, duration).show();

                        Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                        startActivityForResult(myIntent, 0);
                    }
                    else{
                        message = "User Creation Failed...Try Again";
                        Toast.makeText(context, message, duration).show();
                    }
                }catch (Exception e)  {
                    Log.e(LOG_TAG,"Login Error: "+ e);
                }

            }
        });
        Button back=(Button) findViewById(R.id.button10);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), New_User.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    public class InsertUser extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String resultJsonStr = "";

            String baseURL = "http://" + getString(R.string.aws_link) + "/insertUser";
            String USERID_PARAM = "userid";
            String NAME_PARAM = "name";
            String PASW_PARAM = "pasw";
            String MOBILE_PARAM = "mobile";
            String EMAIL_PARAM = "email";

            try {
                Uri testUri = Uri.parse(baseURL).buildUpon()
                        .appendQueryParameter(USERID_PARAM, params[0])
                        .appendQueryParameter(NAME_PARAM, params[1])
                        .appendQueryParameter(PASW_PARAM, params[4])
                        .appendQueryParameter(MOBILE_PARAM, params[2])
                        .appendQueryParameter(EMAIL_PARAM, params[3])
                        .build();
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
                    Log.v(LOG_TAG, "ERROR: "+ resultJsonStr );
                    return Boolean.FALSE;
                } else {
                    Log.v(LOG_TAG, "INSERT SUCCESSFUL");
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
