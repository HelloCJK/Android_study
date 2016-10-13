package com.example.cjk28.quickcoding2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    EditText input_num;
    TextView correct_text;
    Button btn_send, btn_bigger, btn_smaller, btn_bingo;

    private int state = 0;
    private int number = 0, max_num = 1000, min_num = 0;
    private String correct_ = "Your Number is " + String.valueOf(number);
    Random random;
    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_num = (EditText)findViewById(R.id.editText);
        correct_text = (TextView)findViewById(R.id.correct_num);
        btn_send = (Button)findViewById(R.id.btn_send);
        btn_bigger = (Button)findViewById(R.id.btn_bigger);
        btn_smaller = (Button)findViewById(R.id.btn_smaller);
        btn_bingo = (Button)findViewById(R.id.btn_bingo);

        random = new Random();

            btn_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    state = 1;
                    do {
                        number = random.nextInt() % 1000;
                        correct_ = "Your Number is " + String.valueOf(number);
                    }while(number > max_num || number < min_num);
                    correct_text.setText(correct_);
                }
            });
            btn_bigger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    max_num = number;
                    do {
                        number = random.nextInt() % 1000;
                        correct_ = "Your Number is " + String.valueOf(number);
                    }while(number > max_num || number < min_num);
                    correct_text.setText(correct_);
                    cnt++;
                }
            });
            btn_smaller.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    min_num = number;
                    do {
                        number = random.nextInt() % 1000;
                        correct_ = "Your Number is " + String.valueOf(number);
                    }while(number > max_num || number < min_num);
                    correct_text.setText(correct_);
                    cnt++;
                }
            });
            btn_bingo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),cnt+ "번 만에 성공!",Toast.LENGTH_LONG).show();
                }
            });
    }
}
