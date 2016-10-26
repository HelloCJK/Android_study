package com.example.cjk28.mylocationlogger;

import android.*;
import android.Manifest;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MapsActivity extends FragmentActivity
        implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;

    static final LatLng SEOUL = new LatLng(37.56, 126.97);

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static final int REQUEST_CODE_LOCATION = 2;

    private int cntLocation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        mMap.setMyLocationEnabled(true);
        Marker seoul = mMap.addMarker(new MarkerOptions().position(SEOUL).title("Seoul"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10),2000,null);

        // Add a marker in Sydney and move the camera
        //LatLng seoul = new LatLng(37.560000, 126.970000);
        //mMap.addMarker(new MarkerOptions().position(seoul).title("Marker in seoul"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(1500);

        if(PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            CheckPermission();
        }
        startLocationUpdates();
    }

    private void CheckPermission(){
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED){
            if(!ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)){
                ActivityCompat.requestPermissions(MapsActivity.this
                        ,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}
                        ,REQUEST_CODE_LOCATION);
                return;
            }
            ActivityCompat.requestPermissions(MapsActivity.this
            ,new String[]{Manifest.permission.WRITE_CONTACTS}
            ,REQUEST_CODE_LOCATION);
            return;
        }
        hasWriteContactsPermission = ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED){
            if (!ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_LOCATION);

                return;
            }
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{android.Manifest.permission.WRITE_CONTACTS},
                    REQUEST_CODE_LOCATION);
            return;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("LOG_I", "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    private double tmpLatitude = 0;
    private double tmpLongitude = 0;

    private double GetDistance(double x0, double y0, double x1, double y1){
        return Math.sqrt((Math.pow(x0-x1,2)+Math.pow(y0-y1,2)));
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("하이1", String.valueOf(GetDistance(location.getLatitude(),location.getLongitude(),tmpLatitude,tmpLongitude)));
        if(GetDistance(location.getLatitude(),location.getLongitude(),tmpLatitude,tmpLongitude) < 0.001)    return;
        LatLng CURRENT_LOCATION = new LatLng(location.getLatitude(), location.getLongitude());
        tmpLatitude = location.getLatitude();
        tmpLongitude = location.getLongitude();

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String strNow = sdfNow.format(date);

        Marker seoul = mMap.addMarker(new MarkerOptions().position(CURRENT_LOCATION)
                .title(strNow));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CURRENT_LOCATION, 15));
        Log.d("하이2", String.valueOf(cntLocation++));
    }
    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
        if (locationAvailability.isLocationAvailable()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            Toast.makeText(this,"Location Unavialable",Toast.LENGTH_LONG).show();
        }


    }
}
