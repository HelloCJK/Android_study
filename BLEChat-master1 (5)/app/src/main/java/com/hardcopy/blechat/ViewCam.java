package com.hardcopy.blechat;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by cjk28 on 2016-06-28.
 */
public class ViewCam extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private String path;
    public String msg;

    LinearLayout container;
    MediaPlayer mediaPlayer;
    SurfaceView CamView;
    SurfaceHolder surfaceHolder;
    Button btn;
    TextView textView;
    EditText editTarget1;
    EditText editTarget2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        setContentView(R.layout.view_cam);
        //System.in;
        container = (LinearLayout)findViewById(R.id.linear);
        btn = (Button) findViewById(R.id.NBTN0);
        textView = (TextView) findViewById(R.id.textView);
        CamView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = CamView.getHolder();
        surfaceHolder.addCallback(this);
        editTarget1 = (EditText) findViewById(R.id.target1);
        editTarget2 = (EditText) findViewById(R.id.target2);

        path = "rtsp://192.168.100.1/cam1/h264";

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int target1 = Integer.parseInt(editTarget1.getText().toString());
                int target2 = Integer.parseInt(editTarget2.getText().toString());

                Thread cThread = new Thread(new TCPClient());
                cThread.start();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                textView.setText(msg);
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }//surfaceCreated End

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }//surfaceChanged End

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mediaPlayer.stop();

    }//surfaceDestroyed End

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }//onPrepared End

    public class TCPClient implements Runnable {
        @Override
        public void run() {
            try{
                Log.d("TCP","C: Connecting...");
                Socket socket = new Socket("192.168.100.101",8000);

                try{
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    msg = in.readLine();
                    System.out.println(msg);
                    String[] arr = msg.split(" ");
                    for(int i = 0; i<arr.length; i++)
                        System.out.println(arr[i]);
                    Log.d("TCP","C: Receive.");
                    Log.d("TCP","C: Done.");
                } catch(Exception e) {
                    Log.e("TCP", "S: Error", e);
                } finally {
                    socket.close();
                }
            } catch (UnknownHostException e) {
                Log.e("TCP", "C: Error", e);
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("TCP", "C: Error", e);
                e.printStackTrace();
            }
        }//end run
    }//end TCPClient.class
}
/*VideoView CamView =  (VideoView)findViewById(R.id.videoView);
CamView.setVideoPath(path);
CamView.setMediaController(new MediaController(this));
CamView.requestFocus();
CamView.start();
result = CamView.getDrawingCache();
image = (ImageView) findViewById(R.id.imageView) ;
image.setImageBitmap(result);*/
/*
image = (ImageView) findViewById(R.id.imageview);
        path = "rtsp://192.168.100.1:3306/cam1/h264";
        //path = "http://210.123.42.29/";
        Log.d("ㅎㅇ","path");

        try{
        URL url = new URL(path);
        URLConnection connection = url.openConnection();

        //connection.setConnectTimeout(5000);
        //connection.setReadTimeout(5000);

        connection.connect();

        InputStream is = connection.getInputStream();
        Log.d("ㅎㅇ","try");
        Log.d("해",is.toString());
        result = BitmapFactory.decodeStream(is);
        is.close();
        } catch (Exception e){
        Log.d("ㅎㅇ","catch");
        e.printStackTrace();
        }
        //Log.d("ㅎㅇ",result.toString());
        Log.d("ㅎㅇ","image");
        image.setImageBitmap(result);*/