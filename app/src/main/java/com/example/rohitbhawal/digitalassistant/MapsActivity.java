package com.example.rohitbhawal.digitalassistant;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.location.Address;

import java.util.List;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Criteria;
import android.widget.Toast;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String text1, text, text2, range, date, addr;
    double range11, defaultValue;
    String userid;
    String username;
    double latitude, longitude;
    String LOG_TAG = "MapsActivity";
    UiSettings mUiSettings;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent int1 = getIntent();
        text1 = int1.getStringExtra("text1");
        userid = int1.getStringExtra("userid");
        username = int1.getStringExtra("username");
        //range11=int1.getIntExtra("text4",range1);
        range11 = int1.getDoubleExtra("text4", defaultValue);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        Button zoomout=(Button) findViewById(R.id.button13);
//        zoomout.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                mMap.animateCamera(CameraUpdateFactory.zoomOut());
//            }
//        });
        //setUpMap();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
//        mUiSettings.setCompassEnabled(true);
//        mUiSettings.
        Geocoder geocoder = new Geocoder(this);
        /*setUpMap();*/
        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);*/
        List<Address> addressList = null;
        if (text1 != null) {

            try {
                Log.v(LOG_TAG, "Address LIST REACHED :");
                addressList = geocoder.getFromLocationName(text1, 1);
                if (addressList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Address Not Found !", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.v(LOG_TAG, "AddresslIST : " + addressList);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            Log.v(LOG_TAG, "Address: " + address);

            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

            mMap.addMarker(new MarkerOptions().position(latLng).title(text1));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            CircleOptions circleOptions = new CircleOptions().center(new LatLng(address.getLatitude(), address.getLongitude())).radius(range11).strokeColor(Color.GREEN).fillColor(Color.argb(128, 0, 255, 0));

            Circle circle = mMap.addCircle(circleOptions);

             /*if(addressList.get(1)!=null) {
                 Address address1 = addressList.get(1);
                 mMap.addMarker(new MarkerOptions().position(new LatLng(address1.getLatitude(),address1.getLongitude())).title(text1));
                 CircleOptions circleOptions1=new CircleOptions().center(new LatLng(address1.getLatitude(),address1.getLongitude())).radius(range11).strokeColor(Color.GREEN).fillColor(Color.argb(128,0,255,0));
                 Circle circle1 = mMap.addCircle(circleOptions1);
             }
            if(addressList.get(2)!=null) {
             Address address2=addressList.get(2);
                mMap.addMarker(new MarkerOptions().position(new LatLng(address2.getLatitude(),address2.getLongitude())).title(text1));
                CircleOptions circleOptions2=new CircleOptions().center(new LatLng(address2.getLatitude(),address2.getLongitude())).radius(range11).strokeColor(Color.GREEN).fillColor(Color.argb(128,0,255,0));
                Circle circle2 = mMap.addCircle(circleOptions2);}
                if(addressList.get(3)!=null) {
             Address address3=addressList.get(3);
                    mMap.addMarker(new MarkerOptions().position(new LatLng(address3.getLatitude(),address3.getLongitude())).title(text1));
                    CircleOptions circleOptions3=new CircleOptions().center(new LatLng(address3.getLatitude(),address3.getLongitude())).radius(range11).strokeColor(Color.GREEN).fillColor(Color.argb(128,0,255,0));
                    Circle circle3 = mMap.addCircle(circleOptions3);}
            if(addressList.get(4)!=null) {
             Address address4=addressList.get(4);
                mMap.addMarker(new MarkerOptions().position(new LatLng(address4.getLatitude(),address4.getLongitude())).title(text1));
                CircleOptions circleOptions4=new CircleOptions().center(new LatLng(address4.getLatitude(),address4.getLongitude())).radius(range11).strokeColor(Color.GREEN).fillColor(Color.argb(128,0,255,0));
                Circle circle4 = mMap.addCircle(circleOptions4);}*/

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                public void onMapLongClick(LatLng latLng) {
                    Geocoder geocoder1 = new Geocoder(MapsActivity.this);


                    try {
                        List<Address> geoResult = geocoder1.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (geoResult.isEmpty()) {
                            addr = "Lat: " + latLng.latitude + " \n Long: " + latLng.longitude;
                        } else {
                            addr = geoResult.get(0).getAddressLine(0) + "," + geoResult.get(0).getLocality() + "," + geoResult.get(0).getAdminArea() + "," +
                                    geoResult.get(0).getCountryName();
                        }

                        Intent int2 = getIntent();
                        text = int2.getStringExtra("text");
                        text2 = int2.getStringExtra("text2");
                        date = int2.getStringExtra("text3");
                        Intent myIntent = new Intent(MapsActivity.this, Add_Task.class);
                        myIntent.putExtra("text3", text);
                        myIntent.putExtra("text4", text2);
                        myIntent.putExtra("text5", addr);
                        myIntent.putExtra("date1", date);
                        myIntent.putExtra("Lat", latLng.latitude);
                        myIntent.putExtra("Long", latLng.longitude);
                        myIntent.putExtra("Range", range11);
                        myIntent.putExtra("userid", userid);
                        myIntent.putExtra("username", username);
                        startActivity(myIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            });
        } else {
            GTrack gps = new GTrack(MapsActivity.this);
            if (gps.canGetLocation()) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                Log.v(LOG_TAG, userid + " Your Location is: Lat:" + latitude + "  Long:" + longitude);
            }
            LatLng currCoordinate = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(currCoordinate).title(text1));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currCoordinate));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currCoordinate, 12));

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    //Toast.makeText(getApplicationContext(), latLng.latitude + " : " + latLng.longitude, Toast.LENGTH_LONG).show();
                    Geocoder geocoder1 = new Geocoder(MapsActivity.this);


                    try {
                        List<Address> geoResult = geocoder1.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        addr = geoResult.get(0).getAddressLine(0) + "," + geoResult.get(0).getLocality() + "," + geoResult.get(0).getAdminArea() + "," +
                                geoResult.get(0).getCountryName();
                        Intent int2 = getIntent();
                        text = int2.getStringExtra("text");
                        text2 = int2.getStringExtra("text2");
                        date = int2.getStringExtra("text3");
                        Intent myIntent = new Intent(MapsActivity.this, Add_Task.class);
                        myIntent.putExtra("text3", text);
                        myIntent.putExtra("text4", text2);
                        myIntent.putExtra("text5", addr);
                        myIntent.putExtra("date1", date);
                        myIntent.putExtra("Lat", latLng.latitude);
                        myIntent.putExtra("Long", latLng.longitude);
                        myIntent.putExtra("Range", range11);
                        myIntent.putExtra("userid", userid);
                        myIntent.putExtra("username", username);
                        startActivity(myIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            });


        }
        /*mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        /*if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            /*mMap.setMyLocationEnabled(true);
            LocationManager locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria= new Criteria();
            String provider=locationManager.getBestProvider(criteria,true);
            Location myLocation=locationManager.getLastKnownLocation(provider);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            double latitude = myLocation.getLatitude();
            double longitude = myLocation.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
            mMap.addMarker(new MarkerOptions().position(latLng).title("You are here!").snippet("Consider yourself located"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));*/
            /*return;*/
        /*}*/
        /*mMap.setMyLocationEnabled(true);
        LocationManager locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria= new Criteria();
        String provider=locationManager.getBestProvider(criteria,true);
        Location myLocation=locationManager.getLastKnownLocation(provider);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        double latitude = myLocation.getLatitude();
        double longitude = myLocation.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        mMap.addMarker(new MarkerOptions().position(latLng).title("You are here!").snippet("Consider yourself located"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));*/
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.rohitbhawal.digitalassistant/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.rohitbhawal.digitalassistant/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    /*private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            mMap.setMyLocationEnabled(true);

            return;
        }
    }*/

}
