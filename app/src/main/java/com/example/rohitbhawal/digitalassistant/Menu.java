package com.example.rohitbhawal.digitalassistant;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Menu extends AppCompatActivity {
    String LOG_TAG = "Menu";

    //    @Override
    int notId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent int1=getIntent();
        notId=int1.getIntExtra("notifId2",0);
        Button finish=(Button) findViewById(R.id.button4);

        Intent fromLoginActivity1=getIntent();
        final String userid = fromLoginActivity1.getStringExtra("userid");
        final String username = fromLoginActivity1.getStringExtra("username");
        TextView usernameText = (TextView) findViewById(R.id.usernameText);
        usernameText.setText("Welcome "+ username);


        finish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Finish_Task.class);
                myIntent.putExtra("userid", userid);
                myIntent.putExtra("username", username);
                startActivityForResult(myIntent, 0);
            }
        });

        Button logout=(Button) findViewById(R.id.button5);
        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

//                this.startService(serviceIntent);
                Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        Button addtask=(Button) findViewById(R.id.button2);
        addtask.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Add_Task.class);
                myIntent.putExtra("userid", userid);
                myIntent.putExtra("username", username);
                startActivityForResult(myIntent, 0);
            }
        });

        Button start=(Button) findViewById(R.id.button3);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(Menu.this, Start_Task.class);
                myIntent.putExtra("userid", userid);
                myIntent.putExtra("username", username);
//                myIntent.putExtra("notifId1",notId);
                startActivityForResult(myIntent, 0);
            }
        });

        Button viewTask = (Button) findViewById(R.id.showTask);
        viewTask.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(Menu.this, Show_All_Task.class);
                myIntent.putExtra("userid", userid);
                myIntent.putExtra("username", username);
//                myIntent.putExtra("notifId1",notId);
                startActivityForResult(myIntent, 0);
            }
        });


        Button testTool=(Button) findViewById(R.id.testTool);

        if(userid.contains("rohitbhawal")){
            testTool.setVisibility(View.VISIBLE);
        }
        else{
            testTool.setVisibility(View.GONE);
        }
        testTool.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(Menu.this, Test_Tool.class);
                myIntent.putExtra("userid", userid);
                myIntent.putExtra("username", username);
//                myIntent.putExtra("notifId1",notId);
                startActivityForResult(myIntent, 0);
            }
        });

        Intent serviceIntent = new Intent(this, TaskQuery.class);
        serviceIntent.putExtra("userid", userid);
//        serviceIntent.putExtra("StartService", "true");
        Log.v(LOG_TAG,"Service Started");
        this.startService(serviceIntent);
//        serviceIntent.putExtra("userid", "Rohit Bhawal");
//        serviceIntent.putExtra("StartService", "true");
//        Log.v(LOG_TAG,"Service STOPPED");
    }


}
