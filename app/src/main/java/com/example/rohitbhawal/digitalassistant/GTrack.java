package com.example.rohitbhawal.digitalassistant;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.content.Context;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

public class GTrack extends Service implements LocationListener {
    /*@Override*/
    private Context context;
    boolean isGPSEnabled=false;
    boolean canGetLocation=false;
    boolean isNetworkEnabled=false;
    double latitude,longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES=0;
    private static final long MIN_TIME_BW_UPDATES=1;
    protected LocationManager locationManager;
    Location location;
    String LOG_TAG = "GTrack";
    public GTrack(Context context){
//        if ( Build.VERSION.SDK_INT >= 23 &&
//                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(getApplicationContext(), "No GPS Permission", Toast.LENGTH_SHORT).show();
//            return  ;
//        }
        this.context=context;
        getLocation();
    }
    public Location getLocation(){
        try{
            locationManager=(LocationManager) context.getSystemService(LOCATION_SERVICE);

            isGPSEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!isGPSEnabled&&!isNetworkEnabled){

            }
            else{
                this.canGetLocation=true;
                latitude = 0.0;
                longitude = 0.0;
                if(isGPSEnabled){
                    if(location==null){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                        if(locationManager!=null){
                            location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if(location!=null){
                                latitude=location.getLatitude();
                                longitude=location.getLongitude();
                                Log.v(LOG_TAG, "Location From GPS: Lat->"+latitude +"  Long->"+longitude);
                            }
                        }
                    }
                }
                if(latitude == 0.0 && longitude == 0.0) {
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    }
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.v(LOG_TAG, "Location From NETWORK: Lat->"+latitude +"  Long->"+longitude);
                        }
                    }
                }
            }

        }catch (SecurityException s){
            Log.e(LOG_TAG, "Security Exception: "+s);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return location;
    }

    public void stopUsingGPS(){
        if(locationManager!=null){
//            locationManager.removeUpdates(GTrack.this);
        }
    }
    public double getLatitude(){
        if(location!=null){
            latitude=location.getLatitude();
        }
        return latitude;
    }
    public double getLongitude(){
        if(location!=null){
            longitude=location.getLongitude();
        }
        return longitude;
    }
    public boolean canGetLocation(){
        return this.canGetLocation;
    }
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog= new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled.Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
    public void onLocationChanged(Location location) {
        if(location==null){
            try{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                if(locationManager!=null){
                    location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(location!=null){
                        latitude=location.getLatitude();
                        longitude=location.getLongitude();
                    }
                }
            }catch (SecurityException s){
                Log.e(LOG_TAG, "Security Exception OnLocationChanged: "+s);
            }

        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
