package com.example.cjk28.quickcoding05;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tv1;
    TextView tv2;
    EditText ed1;
    Button input;
    Button finish;

    String int_text = "입력된 숫자";
    String str_text = "입력된 문자열";

    int cnt1 = 0;
    int cnt2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<String> stringList = new LinkedList<>();
        final List<Integer> intList = new LinkedList<>();

        tv1 = (TextView)findViewById(R.id.textView3);
        tv2 = (TextView)findViewById(R.id.textView4);

        ed1 = (EditText)findViewById(R.id.editText) ;

        input = (Button)findViewById(R.id.BTNInput);
        finish = (Button)findViewById(R.id.BTNend);

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sr = String.valueOf(ed1.getText());
                try {
                    int i = Integer.parseInt(sr);
                    intList.add(i);
                    int_text += intList.get(cnt1++) + ", ";
                }catch(NumberFormatException e){
                    stringList.add(sr);
                    str_text += stringList.get(cnt2++) + ", ";
                }
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setText(int_text);
                tv2.setText(str_text);
            }
        });
    }
}
