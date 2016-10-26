package com.example.cjk28.quickcoding01;

/**
 * Created by cjk28 on 2016-10-10.
 */

public class MyMaxValue extends MyValues {

    private int[] value;

    MyMaxValue(int[] value){
        this.value = value;
    }

    @Override
    int getValues() {

        int max_num = value[0];

        for(int i = 0; i < value.length; i++){
            if(max_num < value[i])
                max_num = value[i];
        }

        return max_num;
    }
}
