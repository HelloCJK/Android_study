package com.example.cjk28.quickcoding01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView txt_val;
    TextView txt_rdval;
    Button btn_max;
    Button btn_min;

    MyMaxValue myMaxValue;
    MyMinValue myMinValue;

    int num = 0;
    int[] random_num = new int[10];
    String out_txt = "Value: " + num;
    String random_txt = "{ ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_val = (TextView)findViewById(R.id.txt_value);
        txt_rdval = (TextView)findViewById(R.id.txt_rdVal);

        btn_max = (Button)findViewById(R.id.btn_max);
        btn_min = (Button)findViewById(R.id.btn_min);

        Random random = new Random();
        for(int i = 0; i < 10; i++) {
            random_num[i] = random.nextInt() % 100;
            random_txt += random_num[i] + " ";
        }
        random_txt += "}";

        txt_rdval.setText(random_txt);

        myMaxValue = new MyMaxValue(random_num);
        myMinValue = new MyMinValue(random_num);

        btn_max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = myMaxValue.getValues();
                out_txt = "Value: " + num;
                txt_val.setText(out_txt);
            }
        });
        btn_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = myMinValue.getValues();
                out_txt = "Value: " + num;
                txt_val.setText(out_txt);
            }
        });
    }
}
