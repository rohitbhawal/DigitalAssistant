package com.example.rohitbhawal.digitalassistant;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Show_All_Task extends Activity {
    String LOG_TAG = "Show_All_Task";
    List<String> task = new ArrayList<String>();
//    List<String> taskStatus = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__all__task);

        Intent myIntent = getIntent();
        String userid = myIntent.getStringExtra("userid");
        String username = myIntent.getStringExtra("username");
        String user[] = new String[1];
        user[0] = userid;
        ShowTask taskQuery = new ShowTask();
        task.clear();
//        taskStatus.clear();

        try{
            Boolean result = taskQuery.execute(user).get();

            if(! result){
                Toast.makeText(getApplicationContext(), "No Task Found !", Toast.LENGTH_SHORT).show();
                task.add("No Task Found");
            }
        }catch (Exception e){
            Log.v(LOG_TAG, "Error: "+e);
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.task_list, task);

        ListView listView = (ListView) findViewById(R.id.listview_task);
        listView.setAdapter(adapter);
    }

    public class ShowTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
//            Log.v(LOG_TAG, "Cred Param: "+params[0] + "   "+params[1]);
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String resultJsonStr = "";


            String baseURL = "http://" + getString(R.string.aws_link) + "/executeQuery";
            String query = "select * from Task " +
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
                    return Boolean.FALSE;
                } else {
                    Log.v(LOG_TAG, "RECORDS FOUND");
                    try{
                        if (! processJSON(resultJsonStr)){
                            return Boolean.FALSE;
                        }
                    }catch (Exception j){
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

    protected Boolean processJSON(String jsonStr){
        try{
            JSONArray jsonArray = new JSONArray(jsonStr);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject record = jsonArray.getJSONObject(i);
                String status = record.getString("taskid") + " ("+record.getString("dateVal")+ ")";
                if (record.getString("started").contains("no")){
                    status = status.trim() + " -> Not Started";
                }
                else
                {
                    status = status.trim() + " -> Started";
                }
                task.add(status);
//                taskStatus.add(record.getString("started"));
            }
            Log.v(LOG_TAG, "Records Found "+ task);
        }catch (Exception e){
            Log.v(LOG_TAG, "Process Json Error: "+ e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
