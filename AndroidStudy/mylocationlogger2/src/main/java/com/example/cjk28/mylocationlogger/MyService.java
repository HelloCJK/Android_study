package com.example.cjk28.mylocationlogger;

import android.*;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import android.location.LocationListener;
//import com.google.android.gms.location.LocationListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyService extends Service {

    private LocationManager locationManager = null;
    private BufferedWriter fos = null;


    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("테스트", "onCreate() 호출");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(mLocationListener);
        Log.i("테스트", "onDestroy() 호출");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return super.onStartCommand(intent, flags, startId);
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 0,
                mLocationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 5000, 0,
                mLocationListener);

        Log.i("테스트","onStartCommand() 호출");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    final LocationListener mLocationListener = new LocationListener() {
        public String tmpLatitude = "0";
        public String tmpLongitude = "0";

        @Override
        public void onLocationChanged(Location location) {

            Log.d("테스트2", String.valueOf(GetDistance(location.getLatitude(),location.getLongitude(),Double.parseDouble(tmpLatitude),Double.parseDouble(tmpLongitude))));
            if(GetDistance(location.getLatitude(),location.getLongitude(),Double.parseDouble(tmpLatitude),Double.parseDouble(tmpLongitude)) < 0.001)    return;

            tmpLatitude = String.valueOf(location.getLatitude());
            tmpLongitude = String.valueOf(location.getLongitude());

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String strNow = sdfNow.format(date);

            String data = strNow+ " " + tmpLatitude + " " +tmpLongitude + " ";

            try {
                Log.i("테스트2","쓰기 시작");
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File f = new File(path, "external.txt"); // 경로, 파일명
                FileWriter write = new FileWriter(f, true);
                PrintWriter out = new PrintWriter(write);
                out.println(data);
                out.close();
                Log.i("테스트2","쓰기 종료");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("테스트2",data);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public double GetDistance(double x0, double y0, double x1, double y1){
        return Math.sqrt((Math.pow(x0-x1,2)+Math.pow(y0-y1,2)));
    }
}
