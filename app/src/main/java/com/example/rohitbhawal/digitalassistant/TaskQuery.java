package com.example.rohitbhawal.digitalassistant;

import android.app.IntentService;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

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

/**
 * Created by Rohit Bhawal on 7/6/2016.
 */

public class TaskQuery extends IntentService {      //IntentService
    String LOG_TAG = "TaskQuery";
    int WAIT_TIME = 10; //In Seconds
    int QUERY_TIMES = 3; // Times
    List<String> task = new ArrayList<String>();
    List<String> taskDescrip = new ArrayList<String>();


    public TaskQuery() {
        super("TaskQuery");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        Boolean start;
        String userid = intent.getStringExtra("userid");
//        String StartService = intent.getStringExtra("StartService");
        double latitude = 0.0;
        double longitude = 0.0;

//        if (StartService.contains("true")){
//            start = Boolean.TRUE;
//        }
//        else
//        {
//            start = Boolean.FALSE;
//        }
        while(Boolean.TRUE){
            try {
                GTrack gps = new GTrack(TaskQuery.this);
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    Log.v(LOG_TAG, userid + " Your Location is: Lat:" + latitude + "  Long:" + longitude);
                }
                gps = null;
                Log.v(LOG_TAG, userid + " Your Location is: Lat:" + latitude + "  Long:" + longitude);

                int year, month, day;
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                StringBuilder todayDate = new StringBuilder().append(year).append("/").append(month+1).append("/").append(day);
                String data[] = new String[4];
                data[0] = userid;
                data[1] = Double.toString(longitude);
                data[2] = Double.toString(latitude);
                data[3] = todayDate.toString();

//                if (!task.isEmpty()){
                task.clear();
                taskDescrip.clear();
//                }

//                CheckTask task = new CheckTask();
                Boolean result = executeQuery(data);
                if(result){
                    for(int i=0; i< task.size();i++){
                        NotificationHandle notify=new NotificationHandle(TaskQuery.this,i,
                                task.get(i), taskDescrip.get(i));
                        notify = null;

                        Uri alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone ring = RingtoneManager.getRingtone(getApplicationContext(), alarm);
                        ring.play();

                        alarm =null;
                        ring = null;
                    }

                }
                Thread.sleep(WAIT_TIME *1000);
                QUERY_TIMES--;
                if (QUERY_TIMES <= 0){
                    Log.v(LOG_TAG, "Service Breaked !");
                    break;
                }
            }catch (Exception e){
                Log.v(LOG_TAG, "Error in OnHandleIntent: " + e);
            }
        }


//        Notification_Tryout1 notify=new Notification_Tryout1(TaskQuery.this,0, userid, "Your Location is: Lat:"+latitude+"  Long:"+longitude);
    }

//    public class CheckTask extends AsyncTask<String, Void, Boolean> {

    //        @Override
    protected Boolean executeQuery(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJsonStr = "";

        String baseURL = "http://" + getString(R.string.aws_link) + "/validTask";
//            String TASKID_PARAM = "taskid";
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
//                        .appendQueryParameter(desc_PARAM, params[4])
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
                Log.v(LOG_TAG, "ERROR: "+ resultJsonStr );
                return Boolean.FALSE;
            } else {

                if (! processJSON(resultJsonStr)){
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

    protected Boolean processJSON(String jsonStr){
        try{
            JSONArray jsonArray = new JSONArray(jsonStr);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject record = jsonArray.getJSONObject(i);
                task.add(record.getString("taskid"));
                taskDescrip.add(record.getString("desc"));
            }
            Log.v(LOG_TAG, "Records Found "+ task);
        }catch (Exception e){
            Log.v(LOG_TAG, "Process Json Error: "+ e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
//    }

//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        // TODO do something useful
//
//        String userid = intent.getStringExtra("userid");
//        double latitude = 0.0;
//        double longitude = 0.0;
//
//
//        GTrack gps= new GTrack(TaskQuery.this);
//        if(gps.canGetLocation()) {
//            latitude = gps.getLatitude();
//            longitude = gps.getLongitude();
//            Log.v(LOG_TAG, "Your Location is: Lat:"+latitude+"  Long:"+longitude);
//        }
//        else{
//            gps.showSettingsAlert();
//        }
//        Notification_Tryout1 notify=new Notification_Tryout1(TaskQuery.this,0, userid, "Your Location is: Lat:"+latitude+"  Long:"+longitude);
//
//
//        return START_STICKY;
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO for communication return IBinder implementation
//        return null;
//    }

}