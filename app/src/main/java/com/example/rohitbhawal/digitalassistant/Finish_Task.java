package com.example.rohitbhawal.digitalassistant;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Finish_Task extends AppCompatActivity {
    String LOG_TAG = "Finish_Task";
    String task_Place;
    String task_Details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish__task);

        Intent fromMenu=getIntent();
        final String user_ID = fromMenu.getStringExtra("userid");
        final String user_name = fromMenu.getStringExtra("username");

        Button search=(Button) findViewById(R.id.button7);
        Button finish=(Button) findViewById(R.id.button8);


        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final EditText taskid =(EditText) findViewById(R.id.editText10);
                final EditText place = (EditText) findViewById(R.id.editText11);
                final EditText details = (EditText) findViewById(R.id.editText12);

                String task_ID = taskid.getText().toString();

                task_Place = "";
                task_Details = "";
                taskid.setError(null);
                FinishTask searchTask = new FinishTask();
                try{
                    Boolean result = searchTask.execute(user_ID, task_ID, "search").get();

                    if(result){
                        place.setText(task_Place);
                        details.setText(task_Details);
                    }
                    else{
                        taskid.setError("Task ID not Found");
                        taskid.requestFocus();
                    }

                }catch (Exception e){
                    Log.e(LOG_TAG, "Search Task Failed: "+e);
                }
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final EditText taskid =(EditText) findViewById(R.id.editText10);
                String task_ID = taskid.getText().toString();
                FinishTask searchTask = new FinishTask();
                Boolean result = Boolean.FALSE;
                Context context = view.getContext();
                CharSequence message;
                int duration = Toast.LENGTH_SHORT;

                try{
                    if (! task_ID.isEmpty()) {
                        Boolean taskFound = searchTask.execute(user_ID, task_ID, "search").get();

                        if(taskFound){
                            FinishTask deleteTask = new FinishTask();
                            result = deleteTask.execute(user_ID, task_ID, "delete").get();
                        }
                        else{
                            message = "Task Not Found !";
                            Toast.makeText(context, message, duration).show();
                        }
                    }

                    if(result){
                        message = "Task: " + task_ID+ " Finished Successfully";
                    }
                    else{
//                        message = "Task Not Found !";
//                        Toast.makeText(context, message, duration).show();
                        message = "No Task was Deleted";
                    }
                    Toast.makeText(context, message, duration).show();
                }catch (Exception e){
                    Log.e(LOG_TAG, "Search Task Failed: "+e);
                }
                Intent myIntent = new Intent(view.getContext(), Menu.class);
                myIntent.putExtra("userid", user_ID);
                myIntent.putExtra("username", user_name);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    public class FinishTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
//            Log.v(LOG_TAG, "Cred Param: "+params[0] + "   "+params[1]);
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String resultJsonStr = "";
            String baseURL = "";
            String query = "";


            if (params[2].contains("search")){
                baseURL = "http://" + getString(R.string.aws_link) + "/executeQuery";
                query = "select * from Task " +
                        "where userid = '" + params[0] + "'"+
                        " and taskid = '" + params[1] + "'";
            }
            else
            {
                baseURL = "http://" + getString(R.string.aws_link) + "/commitQuery";
                query = "delete from Task " +
                        "where userid = '" + params[0] + "'"+
                        " and taskid = '" + params[1] + "'";
            }

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
                    Log.v(LOG_TAG, "RECORDS FOUND: "+resultJsonStr);
                    try{
                        if (params[2].contains("search")) {
                            JSONArray result = new JSONArray(resultJsonStr);
                            task_Place = result.getJSONObject(0).getString("addr");
                            task_Details = result.getJSONObject(0).getString("desc");
                        }
                        else
                        {
                            task_Place = "";
                            task_Details = "";
                        }
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
