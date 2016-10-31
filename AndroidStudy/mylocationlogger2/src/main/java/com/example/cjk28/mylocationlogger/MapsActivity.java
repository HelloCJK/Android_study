package com.example.cjk28.mylocationlogger;

import android.*;
import android.Manifest;
<<<<<<< HEAD
import android.content.Intent;
import android.location.Location;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
=======
import android.location.Location;
>>>>>>> c5b7e1b43b67edb908c2bece0b05c1e241e260bf
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
<<<<<<< HEAD
import android.view.Menu;
import android.view.MenuItem;
=======
>>>>>>> c5b7e1b43b67edb908c2bece0b05c1e241e260bf
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

<<<<<<< HEAD
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.data;
import static com.google.android.gms.internal.zzsr.My;

=======
>>>>>>> c5b7e1b43b67edb908c2bece0b05c1e241e260bf
public class MapsActivity extends FragmentActivity
        implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;

    static final LatLng SEOUL = new LatLng(37.56, 126.97);
<<<<<<< HEAD
    public static final int CLEAR                = 1;
    public static final int START_BACKGROUND    = 2;
    public static final int STOP_BACKGROUND     = 3;
=======
>>>>>>> c5b7e1b43b67edb908c2bece0b05c1e241e260bf

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static final int REQUEST_CODE_LOCATION = 2;

<<<<<<< HEAD
    private String state;
    private int cntLocation = 0;
    private String[] upLoading;

=======
>>>>>>> c5b7e1b43b67edb908c2bece0b05c1e241e260bf
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
<<<<<<< HEAD

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //menu.add(0,CLEAR,0,"Clear");
        //menu.add(0,START_BACKGROUND,0,"Start Background");
        //menu.add(0,STOP_BACKGROUND,0,"Stop Background");

        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId() ){
            case R.id.clear:
                mMap.clear();
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File f = new File(path,"external.txt");
                if(f.delete())  Log.i("테스트", "file remove = " + f.getName() + ", 삭제 성공");
                else            Log.i("테스트", "file remove = " + f.getName() + ", 삭제 실패");
                break;
            case R.id.start:
                Intent intent = new Intent(MapsActivity.this, MyService.class);
                Log.i("테스트","StartService()");
                startService(intent);
                break;
            case R.id.stop:
                Intent intent2 = new Intent(MapsActivity.this, MyService.class);
                Log.i("테스트","StopService()");
                stopService(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String tmp = "";
        if(!checkExternalStorage()) return;

        try {
            StringBuffer data = new StringBuffer();
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File f = new File(path,"external.txt");
            BufferedReader buffer = new BufferedReader(new FileReader(f));
            String str = buffer.readLine();
            while (str!=null) {
                data.append(str+"\n");
                str = buffer.readLine();
            }
            Log.i("테스트",String.valueOf(data));
            tmp = String.valueOf(data);
            buffer.close();
        } catch (FileNotFoundException e) {
            Log.i("테스트","FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("테스트","IOException");
            e.printStackTrace();
        }
        upLoading = tmp.split(" ");
    }

    private boolean checkExternalStorage() {
        state = Environment.getExternalStorageState();
        // 외부메모리 상태
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // 읽기 쓰기 모두 가능
            Log.d("테스트", "외부메모리 읽기 쓰기 모두 가능");
            return true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            //읽기전용
            Log.d("테스트", "외부메모리 읽기만 가능");
            return false;
        } else {
            // 읽기쓰기 모두 안됨
            Log.d("테스트", "외부메모리 읽기쓰기 모두 안됨 : "+ state);
            return false;
        }
    }

=======
>>>>>>> c5b7e1b43b67edb908c2bece0b05c1e241e260bf
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

<<<<<<< HEAD
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
=======
        if(PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
>>>>>>> c5b7e1b43b67edb908c2bece0b05c1e241e260bf
            return;
        }
        mMap.setMyLocationEnabled(true);
        Marker seoul = mMap.addMarker(new MarkerOptions().position(SEOUL).title("Seoul"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10),2000,null);
<<<<<<< HEAD
        mMap.clear();

        String strNow = "";
        for(int i = 0; i < upLoading.length; i++) {
            switch(i%4){
                case 0:
                    strNow = upLoading[i];
                    Log.i("테스트",strNow);
                    break;
                case 1:
                    strNow += " " + upLoading[i];
                    Log.i("테스트",strNow);
                    break;
                case 2:
                    tmpLatitude = Double.parseDouble(upLoading[i]);
                    Log.i("테스트", String.valueOf(tmpLatitude));
                    break;
                case 3:
                    tmpLongitude = Double.parseDouble(upLoading[i]);
                    Log.i("테스트", String.valueOf(tmpLongitude));
                    LatLng CURRENT_LOCATION = new LatLng(tmpLatitude, tmpLongitude);
                    mMap.addMarker(new MarkerOptions().position(CURRENT_LOCATION).title(strNow));
                    break;
                default:
            }
        }
=======

>>>>>>> c5b7e1b43b67edb908c2bece0b05c1e241e260bf
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
<<<<<<< HEAD
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
=======

        if(PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            CheckPermission();
>>>>>>> c5b7e1b43b67edb908c2bece0b05c1e241e260bf
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

<<<<<<< HEAD
    private double tmpLatitude = 0;
    private double tmpLongitude = 0;

    private double GetDistance(double x0, double y0, double x1, double y1){
        return Math.sqrt((Math.pow(x0-x1,2)+Math.pow(y0-y1,2)));
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("테스트", String.valueOf(GetDistance(location.getLatitude(),location.getLongitude(),tmpLatitude,tmpLongitude)));
        if(GetDistance(location.getLatitude(),location.getLongitude(),tmpLatitude,tmpLongitude) < 0.001)    return;
        LatLng CURRENT_LOCATION = new LatLng(location.getLatitude(), location.getLongitude());
        tmpLatitude = location.getLatitude();
        tmpLongitude = location.getLongitude();

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String strNow = sdfNow.format(date);

        String data = strNow+ " " + String.valueOf(tmpLatitude) + " " +String.valueOf(tmpLongitude)+ " ";

        try {
            Log.i("테스트1","쓰기 시작");
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File f = new File(path, "external.txt"); // 경로, 파일명
            FileWriter write = new FileWriter(f, true);
            PrintWriter out = new PrintWriter(write);
            out.println(data);
            out.close();
            Log.i("테스트1","쓰기 종료");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("테스트1",data);

        Marker seoul = mMap.addMarker(new MarkerOptions().position(CURRENT_LOCATION)
                .title(strNow));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CURRENT_LOCATION, 15));
        Log.d("테스트1", String.valueOf(cntLocation++));
=======
    @Override
    public void onLocationChanged(Location location) {
        LatLng CURRENT_LOCATION = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();
        Marker seoul = mMap.addMarker(new MarkerOptions().position(CURRENT_LOCATION)
                .title("Seoul"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CURRENT_LOCATION, 15));
>>>>>>> c5b7e1b43b67edb908c2bece0b05c1e241e260bf
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
