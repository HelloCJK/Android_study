package com.example.cjk28.quickcoding01;

/**
 * Created by cjk28 on 2016-10-10.
 */

public class MyMinValue extends MyValues {
    private int[] value;

    MyMinValue(int[] value){
        this.value = value;
    }

    @Override
    int getValues() {
        int min_num = value[0];

        for(int i = 0; i < value.length; i++){
            if(min_num > value[i])
                min_num = value[i];
        }

        return min_num;
    }
}
