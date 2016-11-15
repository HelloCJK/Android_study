package com.example.cjk28.btmouse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ZoomControls;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    public static Context mContext;
    private final static int DEVICES_DIALOG = 1;
    private final static int ERROR_DIALOG = 2;
    private static ProgressDialog waitDialog;
    private String errorMessage = "";
    private static final String TAG = "BluetoothTask";

    static private BluetoothAdapter bluetoothAdapter;
    static private BluetoothDevice bluetoothDevice = null;
    static private BluetoothSocket bluetoothSocket;
    static private InputStream btIn;
    static private OutputStream btOut;
    public static AppCompatActivity activity;

    private SensorManager mSensorManager;
    private Sensor accSencor;

    public int Xval;
    public int Yval;
    public int Zval;

    private int mouseSensitive = 6;

    Button btn_m1;
    Button btn_m2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        activity=this;

        btn_m1 = (Button) findViewById(R.id.m1);
        btn_m2 = (Button) findViewById(R.id.m2);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accSencor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            ErrorDialog("This device is not implement Bluetooth.");
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            ErrorDialog("This device is disabled Bluetooth.");
            return;
        }else DeviceDialog();

        btn_m1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        doSend("1");
                        break;
                    case MotionEvent.ACTION_UP:
                        doSend("2");
                        break;
                }
                return false;
            }
        });
        btn_m2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        doSend("3");
                        break;
                    case MotionEvent.ACTION_UP:
                        doSend("4");
                        break;
                }
                return false;
            }
        });
/*
        btn_m1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btn_m2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doClose();
    }
    public void DeviceDialog()
    {
        if (activity.isFinishing()) return;
        FragmentManager fm = MainActivity.this.getSupportFragmentManager();
        MyDialogFragment alertDialog = MyDialogFragment.newInstance(DEVICES_DIALOG, "");
        alertDialog.show(fm, "");
    }

    public void ErrorDialog(String text)
    {
        if (activity.isFinishing()) return;
        FragmentManager fm = MainActivity.this.getSupportFragmentManager();
        MyDialogFragment alertDialog = MyDialogFragment.newInstance(ERROR_DIALOG, text);
        alertDialog.show(fm, "");
    }

    public void doSetResultText(String text) {

    }
        public static void hideWaitDialog() {
    }


    static public Set<BluetoothDevice> getPairedDevices() {
        return bluetoothAdapter.getBondedDevices();
    }

    public void doConnect(BluetoothDevice device) {
        bluetoothDevice = device;
        //Standard SerialPortService ID
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        //UUID uuid = UUID.fromString("11111111-1111-1111-1111-111111111123");
        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            bluetoothAdapter.cancelDiscovery();
            new ConnectTask().execute();
            mSensorManager.registerListener(this
                    , mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
                    ,SensorManager.SENSOR_DELAY_GAME
            );
        } catch (IOException e) {
            Log.e(TAG, e.toString(), e);
            ErrorDialog(e.toString());
        }
    }

    public void doClose() {
        new CloseTask().execute();
    }

    public void doSend(String msg) {
        new SendTask().execute(msg);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            Xval = (int)(event.values[0]*mouseSensitive);
            Yval = (int)(event.values[1]*mouseSensitive);
            Zval = (int)(event.values[2]*mouseSensitive);
            String str = String.valueOf(Xval) + " " + String.valueOf(Yval) + " " + String.valueOf(Zval);
            Log.d("센서",str);
            doSend(str);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if(sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            Log.d("센서 정확도",String.valueOf(accuracy));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mSensorManager.unregisterListener(this);
    }

    private class ConnectTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected Object doInBackground(Void... params) {
            try {
                bluetoothSocket.connect();
                btIn = bluetoothSocket.getInputStream();
                btOut = bluetoothSocket.getOutputStream();
            } catch (Throwable t) {
                Log.e( TAG, "connect? "+ t.getMessage() );
                doClose();
                return t;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Object result) {
            if (result instanceof Throwable) {
                Log.e(TAG,result.toString(),(Throwable)result);
                ErrorDialog(result.toString());
            } else {
                hideWaitDialog();
            }
        }
    }
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
            case R.id.UP:
                mouseSensitive++;
                if(mouseSensitive > 20)
                    mouseSensitive = 20;
                Toast.makeText(this,"민감도: "+ String.valueOf(mouseSensitive),Toast.LENGTH_LONG).show();
                break;
            case R.id.DOWN:
                mouseSensitive--;
                if(mouseSensitive <= 0)
                    mouseSensitive = 1;
                Toast.makeText(this,"민감도: "+ String.valueOf(mouseSensitive),Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private class CloseTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected Object doInBackground(Void... params) {
            try {
                try{btOut.close();}catch(Throwable t){/*ignore*/}
                try{btIn.close();}catch(Throwable t){/*ignore*/}
                bluetoothSocket.close();
            } catch (Throwable t) {
                return t;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Object result) {
            if (result instanceof Throwable) {
                Log.e(TAG,result.toString(),(Throwable)result);
                ErrorDialog(result.toString());
            }
        }
    }

    private class SendTask extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... params) {
            try {
                btOut.write(params[0].getBytes());
                btOut.flush();
                byte[] buff = new byte[512];
                int len = btIn.read(buff);
                Log.e( TAG, "recv? "+ len );
                return new String(buff, 0, len);
            } catch (Throwable t) {
                doClose();
                return t;
            }
        }
        @Override
        protected void onPostExecute(Object result) {
            if (result instanceof Exception) {
                Log.e(TAG,result.toString(),(Throwable)result);
                ErrorDialog(result.toString());
            } else {
                doSetResultText(result.toString());
            }
        }
    }
}

