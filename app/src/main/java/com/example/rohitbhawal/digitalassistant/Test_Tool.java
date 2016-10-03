package com.example.rohitbhawal.digitalassistant;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Test_Tool extends AppCompatActivity {
    String LOG_TAG = "Test_Tool";
    String userid;
    String username;
    List<String> taskDetails = new ArrayList<String>();
    double latitude = 0.0;
    double longitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test__tool);

        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        username = intent.getStringExtra("username");

        Button getDetails=(Button) findViewById(R.id.getDetails);

        getDetails.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    EditText taskidText=(EditText) findViewById(R.id.taskid);
                    EditText curLat= (EditText) findViewById(R.id.curLat);
                    EditText curLong= (EditText) findViewById(R.id.curLong);
                    EditText taskLat=(EditText) findViewById(R.id.taskLat);
                    EditText taskLong=(EditText) findViewById(R.id.taskLong);
                    EditText range=(EditText) findViewById(R.id.range);
                    EditText distance=(EditText) findViewById(R.id.distance);

                    String taskid = taskidText.getText().toString();

                    GTrack gps = new GTrack(Test_Tool.this);
                    if (gps.canGetLocation()) {
                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();

                    }
                    else{
                        latitude = 0.0;
                        longitude = 0.0;
                    }
                    Log.v(LOG_TAG, userid + " Your Current Location is: Lat:" + latitude + "  Long:" + longitude);

                    curLat.setText(Double.toString(latitude));
                    curLong.setText(Double.toString(longitude));

                    String data[] = new String[5];
                    data[0] = userid;
                    data[1] = Double.toString(longitude);
                    data[2] = Double.toString(latitude);
                    data[3] = getTodaysDate();
                    data[4] = taskid;



                    RangeTest task = new RangeTest();
                    Boolean result = task.execute(data).get();
                    if (result) {
                        taskLat.setText(taskDetails.get(0));
                        taskLong.setText(taskDetails.get(1));
                        range.setText(taskDetails.get(2));
                        distance.setText(taskDetails.get(3));
                    }
                    taskDetails.clear();

                } catch (Exception e) {
                    Log.v(LOG_TAG, "Error in OnHandleIntent: " + e);
                }
            }
        });

        Intent myIntent = new Intent(Test_Tool.this, Menu.class);
        myIntent.putExtra("userid", userid);
        myIntent.putExtra("username", username);
    }

    public String getTodaysDate(){
        int year, month, day;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        StringBuilder todayDate = new StringBuilder().append(year).append("/").append(month + 1).append("/").append(day);

        return todayDate.toString();
    }

    public class RangeTest extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String resultJsonStr = "";

            String baseURL = "http://" + getString(R.string.aws_link) + "/validTask";
            String TASKID_PARAM = "taskid";
            String USERID_PARAM = "userid";
            String longitude_PARAM = "longitude";
            String latitude_PARAM = "latitude";
            //        String addr_PARAM = "addr";
            //        String desc_PARAM = "desc";
            //        String started_PARAM = "started";
            //        String range_PARAM = "range";
            String date_PARAM = "date";

            try {
                Uri testUri = Uri.parse(baseURL).buildUpon()
                        .appendQueryParameter(USERID_PARAM, params[0])
                        .appendQueryParameter(longitude_PARAM, params[1])
                        .appendQueryParameter(latitude_PARAM, params[2])
                        .appendQueryParameter(date_PARAM, params[3])
                        .appendQueryParameter(TASKID_PARAM, params[4])
                        //                        .appendQueryParameter(started_PARAM, params[5])
                        //                        .appendQueryParameter(range_PARAM, params[6])
                        //                        .appendQueryParameter(date_PARAM, params[7])
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
                    Log.v(LOG_TAG, "ERROR: " + resultJsonStr);
                    return Boolean.FALSE;
                } else {

                    if (!processJSON(resultJsonStr)) {
                        return Boolean.FALSE;
                    }

                    Log.v(LOG_TAG, "Executed Successfully");
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

        protected Boolean processJSON(String jsonStr) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject record = jsonArray.getJSONObject(i);
//                    taskDetails.add(record.getString("taskid"));
                    taskDetails.add(record.getString("latitude"));
                    taskDetails.add(record.getString("longitude"));
                    taskDetails.add(record.getString("rangeVal"));
                    taskDetails.add(record.getString("Distance"));
                }
                Log.v(LOG_TAG, "Records Found " + taskDetails);
            } catch (Exception e) {
                Log.v(LOG_TAG, "Process Json Error: " + e);
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        }
    }
}
