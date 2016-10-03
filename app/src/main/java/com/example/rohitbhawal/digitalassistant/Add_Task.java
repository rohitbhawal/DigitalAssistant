package com.example.rohitbhawal.digitalassistant;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

public class Add_Task extends AppCompatActivity {

    double range12,Lat,Long,defaultValue,range13;
    String addr,task,details12,date,date2,range11;
    Date date1;

    private Calendar calendar;
    private EditText dateView;
    private int year, month, day;
    String LOG_TAG = "Add_Task";
    String userID;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__task);
        Button searchLocation=(Button) findViewById(R.id.button22);



        searchLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText tid = (EditText) findViewById(R.id.editText7);
                EditText place = (EditText) findViewById(R.id.editText8);
                EditText details = (EditText) findViewById(R.id.editText9);
                EditText date = (EditText) findViewById(R.id.editText18);
                EditText range = (EditText) findViewById(R.id.editText19);
                String tid1 = tid.getText().toString();
                String place1 = place.getText().toString();
                String details1 = details.getText().toString();
                String date1 = date.getText().toString();
                String range1=range.getText().toString();
                CheckBox mon = (CheckBox) findViewById(R.id.mon);
                CheckBox tue = (CheckBox) findViewById(R.id.tue);
                CheckBox wed = (CheckBox) findViewById(R.id.wed);
                CheckBox thu = (CheckBox) findViewById(R.id.thu);
                CheckBox fri = (CheckBox) findViewById(R.id.fri);
                CheckBox sat = (CheckBox) findViewById(R.id.sat);
                CheckBox sun = (CheckBox) findViewById(R.id.sun);


                place.setError(null);
                if(place1.isEmpty()){
                    place.setError("Place Cannot be Blank");
                    place.requestFocus();
                    return;
                }

                try {
                    range12 = Double.parseDouble(range1);
                }
                catch(NumberFormatException e){
                    range12 = 5000;
                }
                Intent myIntent = new Intent(Add_Task.this, MapsActivity.class);
                myIntent.putExtra("text",tid1);
                myIntent.putExtra("text1",place1);
                myIntent.putExtra("text2",details1);
                myIntent.putExtra("text3",date1);
                myIntent.putExtra("text4",range12);
                startActivity(myIntent);
            }
        });

        Button selectLocation=(Button) findViewById(R.id.button6);
        selectLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText tid= (EditText) findViewById(R.id.editText7);

                EditText details= (EditText) findViewById(R.id.editText9);
                EditText date=(EditText) findViewById(R.id.editText18);
                EditText range=(EditText) findViewById(R.id.editText19);
                String tid1=tid.getText().toString();
                String details1=details.getText().toString();
                String date1=date.getText().toString();
                String range1=range.getText().toString();
                try {
                    range12 = Double.parseDouble(range1);
                }
                catch(NumberFormatException e) {
                    range12 = 5000;
                }
                Intent myIntent = new Intent(Add_Task.this, MapsActivity.class);
                myIntent.putExtra("userid", userID);
                myIntent.putExtra("username", username);
                myIntent.putExtra("text",tid1);
                myIntent.putExtra("text2",details1);
                myIntent.putExtra("text3",date1);
                myIntent.putExtra("text4",range12);
                startActivity(myIntent);
            }
        });


        dateView = (EditText) findViewById(R.id.editText18);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        Button setDate = (Button) findViewById(R.id.setDate);
        setDate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                showDialog(999);
            }
        });


        Intent int1=getIntent();
        addr=int1.getStringExtra("text5");
        EditText addr1=(EditText) findViewById(R.id.editText20);
        EditText tid= (EditText) findViewById(R.id.editText7);
        EditText details= (EditText) findViewById(R.id.editText9);
        EditText Date=(EditText) findViewById(R.id.editText18);
        EditText range=(EditText) findViewById(R.id.editText19);
        addr1.setText(addr);
        Lat=int1.getDoubleExtra("Lat",defaultValue);
        Long=int1.getDoubleExtra("Long",defaultValue);
        task=int1.getStringExtra("text3");
        details12=int1.getStringExtra("text4");
        date=int1.getStringExtra("date1");
        range13=int1.getDoubleExtra("Range",defaultValue);
        range11=String.valueOf(range13);
        /*try{
            SimpleDateFormat date11=new SimpleDateFormat("dd/MM/yyyy");
            date1=date11.parse(date);
            date2=date11.format(date1);

        }
        catch(ParseException e){

        }*/
        tid.setText(task);
        details.setText(details12);
        Date.setText(date);
        range.setText(range11);


        userID = int1.getStringExtra("userid");
        username = int1.getStringExtra("username");
        Log.v(LOG_TAG, "USERID & USERNAME: "+ userID+ "  &  "+username);


        Button addtask=(Button) findViewById(R.id.button14);

        addtask.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText addr1=(EditText) findViewById(R.id.editText20);
                EditText tid= (EditText) findViewById(R.id.editText7);
                EditText details= (EditText) findViewById(R.id.editText9);
                EditText Date=(EditText) findViewById(R.id.editText18);
                EditText range=(EditText) findViewById(R.id.editText19);
                CheckBox mon = (CheckBox) findViewById(R.id.mon);
                CheckBox tue = (CheckBox) findViewById(R.id.tue);
                CheckBox wed = (CheckBox) findViewById(R.id.wed);
                CheckBox thu = (CheckBox) findViewById(R.id.thu);
                CheckBox fri = (CheckBox) findViewById(R.id.fri);
                CheckBox sat = (CheckBox) findViewById(R.id.sat);
                CheckBox sun = (CheckBox) findViewById(R.id.sun);

                int rep = 0;
                String data[] = new String[10];

                tid.setError(null);
                addr1.setError(null);
                Date.setError(null);

                if(tid.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Task ID Cannot be Blank", Toast.LENGTH_SHORT).show();
                    tid.setError("Task ID Cannot be Blank");
                    tid.requestFocus();
                    return ;
                }

                if(Long == 0.0 && Lat == 0.0){
                    Toast.makeText(getApplicationContext(), "Address Not Found...Please Try Again !", Toast.LENGTH_SHORT).show();
                    addr1.setError("Address Not Found !");
                    addr1.requestFocus();
                    return ;
                }

//                if(! Date.getText().toString().isEmpty()){
//                    Toast.makeText(getApplicationContext(), "Incorrect Date !", Toast.LENGTH_SHORT).show();
//                    Date.setError("Supported Date Format m/d/yyyy ");
//                    Date.requestFocus();
//                    mon.setChecked(false);
//                    tue.setChecked(false);
//                    wed.setChecked(false);
//                    thu.setChecked(false);
//                    fri.setChecked(false);
//                    sat.setChecked(false);
//                    sun.setChecked(false);
//                    return;
//                }

                if(mon.isChecked() || tue.isChecked() || wed.isChecked() || thu.isChecked() ||
                        fri.isChecked() || sat.isChecked() || sun.isChecked())
                {
//                    Date.setText(null);
                    if (mon.isChecked()){
                        rep = 1000000;
                    }
                    if (tue.isChecked()){
                        rep = rep + 100000;
                    }
                    if (wed.isChecked()){
                        rep = rep + 10000;
                    }
                    if (thu.isChecked()){
                        rep = rep + 1000;
                    }
                    if (fri.isChecked()){
                        rep = rep + 100;
                    }
                    if (sat.isChecked()){
                        rep = rep + 10;
                    }
                    if (sun.isChecked()){
                        rep = rep + 1;
                    }
//                    Toast.makeText(getApplicationContext(), "Days Choosen = "+ String.valueOf(rep), Toast.LENGTH_SHORT).show();
                }

//                Long = 10.123123123123;
//                Lat = 55.342424234324;

//                String rangeInKm;
                range13 = range13/1000;
                data[0] = userID;
                data[1] = tid.getText().toString();
                data[2] = Double.toString(Long);
                data[3] = Double.toString(Lat);
                data[4] = addr1.getText().toString();
                data[5] = details.getText().toString();
                data[6] = "no";
                data[7] = Double.toString(range13);
                data[8] = Date.getText().toString();
                data[9] = String.format("%07d", rep);
                CheckTaskid checkTask = new CheckTaskid();
                InsertTask insertTask = new InsertTask();
                try{
                    Boolean taskidFound = checkTask.execute(data).get();

                    if(! taskidFound){
                        tid.setError("Task ID Already Used");
                        tid.requestFocus();
                        return;
                    }

                    Boolean result = insertTask.execute(data).get();
                    if(result){
                        Toast.makeText(getApplicationContext(), "Task Added Successfully", Toast.LENGTH_SHORT).show();
                        Intent myIntent=new Intent(Add_Task.this,Menu.class);
                        myIntent.putExtra("userid", userID);
                        myIntent.putExtra("username", username);
                        startActivity(myIntent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Error in Adding Task", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Log.v(LOG_TAG, "Error Insert Task: " +e);
                }

            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(month).append("/")
                .append(day).append("/").append(year));
    }

    public class InsertTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String resultJsonStr = "";

            String baseURL = "http://" + getString(R.string.aws_link) + "/insertTask";
            String TASKID_PARAM = "taskid";
            String USERID_PARAM = "userid";
            String longitude_PARAM = "longitude";
            String latitude_PARAM = "latitude";
            String addr_PARAM = "addr";
            String desc_PARAM = "desc";
            String started_PARAM = "started";
            String range_PARAM = "range";
            String date_PARAM = "date";
            String rep_PARAM = "rep";

            try {
                Uri testUri = Uri.parse(baseURL).buildUpon()
                        .appendQueryParameter(USERID_PARAM, params[0])
                        .appendQueryParameter(TASKID_PARAM, params[1])
                        .appendQueryParameter(longitude_PARAM, params[2])
                        .appendQueryParameter(latitude_PARAM, params[3])
                        .appendQueryParameter(addr_PARAM, params[4])
                        .appendQueryParameter(desc_PARAM, params[5])
                        .appendQueryParameter(started_PARAM, params[6])
                        .appendQueryParameter(range_PARAM, params[7])
                        .appendQueryParameter(date_PARAM, params[8])
//                        .appendQueryParameter(rep_PARAM, params[9])
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

    public class CheckTaskid extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String resultJsonStr = "";


            String baseURL = "http://" + getString(R.string.aws_link) + "/executeQuery";
            String query = "select * from Task " +
                    "where userid = '" + params[0] + "'" +
                    "and taskid = '" + params[1]+ "'";
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
