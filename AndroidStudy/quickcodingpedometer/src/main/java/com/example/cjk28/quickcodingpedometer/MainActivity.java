package com.example.cjk28.quickcodingpedometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor accSencor;

    TextView textView;
    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accSencor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        textView = (TextView)findViewById(R.id.textView2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this
                , mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
                ,SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    float Xval, Yval, Zval;
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            Xval = (int)(event.values[0]);
            Yval = (int)(event.values[1]);
            Zval = (int)(event.values[2]);
            if(Zval > 10 )
                textView.setText("You Walked" +String.valueOf(++cnt) + "Steps!!");
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
