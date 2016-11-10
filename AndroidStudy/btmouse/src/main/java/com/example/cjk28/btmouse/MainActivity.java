package com.example.cjk28.btmouse;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.EntityIterator;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public static Context mContext;

    private final static int DEVICES_DIALOG = 1;
    private final static int ERROR_DIALOG = 2;

    private static ProgressDialog waitDialog;
    private EditText editText1;
    private EditText editText2;
    private String errorMessage = "";

    private static final String TAG = "BluetoothTask";

    static private BluetoothAdapter bluetothAdapter;
    static private BluetoothDevice bluetoothDevice = null;
    static private BluetoothSocket bluetoothSocket;
    static private InputStream btIn;
    static private OutputStream btOut;
    public static AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        activity = this;

        editText1 = (EditText)findViewById(R.id.editText1);
        editText2 = (EditText)findViewById(R.id.editText2);

        Button sendBtn = (Button)findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText1.getText().toString();
                doSend(msg);
            }
        });
        Button resetBtn = (Button)findViewById(R.id.resetBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText1.setText("");
                editText2.setText("");
            }
        });

        bluetothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetothAdapter == null){
            ErrorDialog("This device is not implement Bluetooth.");
            return;
        }
        if(!bluetothAdapter.isEnabled()){
            ErrorDialog("This device is disabled Bluetooth.");
            return;
        }else DeviceDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doClose();
    }
    public void DeviceDialog(){
        if(activity.isFinishing())  return;

        FragmentManager fm = MainActivity.this.getSupportFragmentManager();
        MyDialogFragment alertDialog = MyDialogFragment.newInstance(DEVICES_DIALOG, "");
        alertDialog.show(fm,"");
    }

    public void ErrorDialog(String text){
        if(activity.isFinishing())  return;

        FragmentManager fm = MainActivity.this.getSupportFragmentManager();
        MyDialogFragment alertDialog = MyDialogFragment.newInstance(ERROR_DIALOG, text);
        alertDialog.show(fm,"");
    }

    public void doSetResultText(String text){
        editText2.setText(text);
    }

    public static void hideWaitDialog(){
    }

    static public Set<BluetoothDevice> getPairedDevices(){
        return bluetothAdapter.getBondedDevices();
    }

    public void doConnect(BluetoothDevice device){
        bluetoothDevice = device;

        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            bluetothAdapter.cancelDiscovery();
            new ConnectTask().execute();
        } catch (IOException e) {
            Log.e(TAG, e.toString(),e);
            ErrorDialog(e.toString());
            //e.printStackTrace();
        }
    }

    private void doClose() {
        new CloseTask().execute();
    }

    public void doSend(String msg){
        new SendTask().execute(msg);
    }

    private class ConnectTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if(result instanceof  Throwable){
                Log.e(TAG,result.toString(), (Throwable) result);
                ErrorDialog(result.toString());
            }else{
                hideWaitDialog();
            }
        }

        @Override
        protected Object doInBackground(Void... params) {
            try{
                bluetoothSocket.connect();
                btIn = bluetoothSocket.getInputStream();
                btOut = bluetoothSocket.getOutputStream();
            }catch(Throwable t){
                Log.e( TAG, "connect? "+ t.getMessage() );
                doClose();
                return t;
            }
            return null;
        }
    }

    private class CloseTask extends AsyncTask<Void,Void,Object>{
        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if(result instanceof Throwable){
                Log.e(TAG,(result).toString(),(Throwable)result);
                ErrorDialog((result).toString());
            }
        }

        @Override
        protected Object doInBackground(Void... params) {
            try{
                try{btOut.close();}catch(Throwable t){}
                try{btIn.close();}catch(Throwable t){}
                bluetoothSocket.close();
            }catch(Throwable t){
                return t;
            }
            return null;
        }
    }
    private class SendTask extends AsyncTask<String,Void,Object>{
        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if(result instanceof Exception){
                Log.e(TAG,result.toString(),(Throwable)result);
                ErrorDialog(result.toString());
            }else{
                doSetResultText(result.toString());
            }
        }

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
    }
}//main class end
