/*
 * Copyright (C) 2014 Bluetooth Connection Template
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hardcopy.blechat.fragments;

import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.method.ScrollingMovementMethod;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hardcopy.blechat.R;
import com.hardcopy.blechat.ViewCam;
import com.hardcopy.blechat.bluetooth.BleManager;
import com.hardcopy.blechat.bluetooth.TransactionBuilder;

import org.w3c.dom.CharacterData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ExampleFragment extends Fragment implements View.OnClickListener {

	private Context mContext = null;
	private IFragmentListener mFragmentListener = null;
	private Handler mActivityHandler = null;
    private BluetoothGattCallback mbluetoothGattCallback = null;

    Button mTakeOff;
    Button mLanding;
    Button mstop;
    Button mgo;
    Button mpitch;
    Button myaw;
    Button mtrottle_up;
    Button mdown;
    Button mmission1;
    Button mmission2;
    Button mCamera;

    EditText mx_txt;
    EditText my_txt;
    EditText mYawTxt;
    EditText mUpTxt;
    EditText mMission1Txt;
    private String msg;
    //private NumberThread mNumberThread;

    public ExampleFragment(Context c, IFragmentListener l, Handler h) {
		mContext = c;
		mFragmentListener = l;
		mActivityHandler = h;
        //mreadCharacteristic = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);

        mx_txt = (EditText)rootView.findViewById(R.id.x_txt);
        String str_roll = mx_txt.getText().toString();
        my_txt = (EditText)rootView.findViewById(R.id.y_txt);
        String str_pitch = my_txt.getText().toString();
        mYawTxt = (EditText)rootView.findViewById(R.id.yaw_txt);
        String str_yaw = mYawTxt.getText().toString();
        mUpTxt = (EditText)rootView.findViewById(R.id.up_txt);
        String str_up = mUpTxt.getText().toString();
        mMission1Txt = (EditText)rootView.findViewById(R.id.mission1_txt);
        String str_down = mMission1Txt.getText().toString();

        mTakeOff = (Button)rootView.findViewById(R.id.TakeOff);
        mTakeOff.setOnClickListener(this);
        mLanding = (Button)rootView.findViewById(R.id.Landing);
        mLanding.setOnClickListener(this);
        mstop = (Button)rootView.findViewById(R.id.stop);
        mstop.setOnClickListener(this);
        mgo = (Button)rootView.findViewById(R.id.go);
        mgo.setOnClickListener(this);
        mpitch = (Button)rootView.findViewById(R.id.pitch);
        mpitch.setOnClickListener(this);
        myaw = (Button)rootView.findViewById(R.id.yaw);
        myaw.setOnClickListener(this);
        mtrottle_up = (Button)rootView.findViewById(R.id.trottle_up);
        mtrottle_up.setOnClickListener(this);
        mdown = (Button)rootView.findViewById(R.id.down);
        mdown.setOnClickListener(this);
        mmission1 = (Button)rootView.findViewById(R.id.mission1);
        mmission1.setOnClickListener(this);
        mmission2 = (Button)rootView.findViewById(R.id.mission2);
        mmission2.setOnClickListener(this);
        mCamera = (Button)rootView.findViewById(R.id.Camera);
        mCamera.setOnClickListener(this);
        //mNumberThread = new NumberThread(true);
        //mNumberThread.start();

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mNumberThread.stopThread();
    }

    public void sendInstruction(byte is[],int isArray){
		byte [] new_is = new byte[isArray];
		for(int i=0; i < isArray;i++){
			new_is[i] = is[i];
		}
        sendMessage(new_is);
	}
    public void motion_take_off(){
        byte[] sample = new byte[3];
        sample[0] = 0x11;
        sample[1] = 0x22;
        sample[2] = 0x01;
        sendInstruction(sample, 3);
    }
    public void motion_landing(){
        byte[] sample = new byte[3];
        sample[0] = 0x11;
        sample[1] = 0x22;
        sample[2] = 0x07;
        sendInstruction(sample, 3);
    }
    public void motion_stop(){
        byte[] sample = new byte[3];
        sample[0] = 0x11;
        sample[1] = 0x22;
        sample[2] = 0x06;
        sendInstruction(sample, 3);
    }
    public void motion(int Roll,int Pitch,int Yaw,int Throttle){
        byte[] sample = new byte[5];
        sample[0] = 0x10;
        sample[1] = (byte)Roll;
        sample[2] = (byte)Pitch;
        sample[3] = (byte)Yaw;
        sample[4] = (byte)Throttle;
        sendInstruction(sample, 5);
    }

    public void delay(int i){
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        byte[] sample = new byte[10];
        byte to_1;
        String s1;
        String s2;
        int to;
        int to_2;
        switch (v.getId()) {
            case R.id.TakeOff:
                motion_take_off();
                break;
            case R.id.Landing:
                motion_landing();
                break;
            case R.id.stop:
                motion_stop();
                break;
            case R.id.go:
                s1 = mx_txt.getText().toString();
                to = Integer.parseInt(s1);
                switch (to) {
                    case 1:
                        motion_take_off();
                        delay(1500);
                        motion(0,0,0,18);
                        delay(500);
                        motion(0,22,0,10);
                        delay(800);
                        motion_landing();
                        break;
                    case 2:
                        motion_take_off();
                        delay(1500);
                        motion(0,0,0,6);
                        delay(500);
                        motion(0,32,0,6);
                        delay(1000);
                        motion(0,0,0,18);
                        delay(500);
                        motion_landing();
                        break;
                    case 3:  //complete
                        motion_take_off();
                        delay(1500);
                        motion(0,0,0,37);
                        delay(500);
                        motion(0,40,0,16);
                        delay(800);
                        motion_take_off();
                        break;
                    case 4:
                        motion_take_off();
                        delay(1500);
                        motion(0,0,0,48);
                        delay(500);
                        motion(0,53,0,16);
                        delay(1000);
                        motion(0,0,0,37);
                        delay(1000);
                        motion_take_off();
                        break;
                    case 5:
                        motion_take_off();
                        delay(1500);
                        motion(0,0,0,48);
                        delay(500);
                        motion(0,65,0,16);
                        delay(1000);
                        motion(0,0,0,40);
                        delay(1000);
                        motion_take_off();
                        break;
                    case 6:
                        motion_take_off();
                        delay(1500);
                        motion(0,0,0,48);
                        delay(500);
                        motion(0,75,0,16);
                        delay(1000);
                        motion(0,0,0,45);
                        delay(1000);
                        motion_take_off();
                        break;
                    case 7:
                        motion_take_off();
                        delay(1500);
                        motion(0,0,0,48);
                        delay(500);
                        motion(0,85,0,16);
                        delay(1000);
                        motion(0,0,0,40);
                        delay(1000);
                        motion_take_off();
                        break;
                    case 8:
                        motion_take_off();
                        delay(1500);
                        motion(0,0,0,48);
                        delay(500);
                        motion(0,95,0,16);
                        delay(1000);
                        motion(0,0,0,40);
                        delay(1000);
                        motion_take_off();
                        break;
                    case 9:
                        motion_take_off();
                        delay(1500);
                        motion(0,0,0,48);
                        delay(500);
                        motion(0,100,0,16);
                        delay(1000);
                        motion(0,0,0,40);
                        delay(1000);
                        motion_take_off();
                        break;
                }
                break;
            case R.id.pitch:
                s1 = my_txt.getText().toString();
                to = Integer.parseInt(s1);
                switch (to)  {
                    case 1:         //complete
                        motion_take_off();
                        delay(1500);
                        motion(13,0,0,11);
                        delay(1000);
                        motion(0,-19,0,16);
                        delay(800);
                        motion_landing();
                        break;
                    case 2:             //complete
                        motion_take_off();
                        delay(1500);
                        motion(28,0,0,13);
                        delay(1000);
                        motion(0,-37,0,17);
                        delay(800);
                        motion_landing();
                        break;
                    case 3:
                        motion_take_off();
                        delay(1500);
                        motion(23,0,0,8);
                        delay(1000);
                        motion(0,-41,0,20);
                        delay(800);
                        motion_landing();
                        break;
                    case 4:         //debug from here
                        motion_take_off();
                        delay(1500);
                        motion(38,0,0,8);
                        delay(1000);
                        motion(0,-41,0,20);
                        delay(800);
                        motion_landing();
                        break;
                    case 5:
                        motion_take_off();
                        delay(1500);
                        motion(45,0,0,8);
                        delay(1000);
                        motion(0,-41,0,20);
                        delay(800);
                        motion_landing();
                        break;
                    case 6:
                        motion_take_off();
                        delay(1500);
                        motion(57,0,0,8);
                        delay(1000);
                        motion(0,-41,0,20);
                        delay(800);
                        motion_landing();
                        break;
                    case 7:
                        motion_take_off();
                        delay(1500);
                        motion(70,0,0,8);
                        delay(1000);
                        motion(0,-41,0,20);
                        delay(800);
                        motion_landing();
                        break;
                }

                break;
            case R.id.mission1:
                s1 = mMission1Txt.getText().toString();
                to_2 = Integer.parseInt(s1);
                while(to_2>0) {
                    motion_take_off();
                    delay(1500);
                    motion(0,0,0,28);
                    delay(1000);
                    motion(0,0,0,0);
                    delay(1000);
                    motion(0,0,0,14);
                    delay(200);
                    motion(0,0,0,0);
                    delay(800);
                    motion(0,0,0,14);
                    delay(200);
                    motion(0,0,0,0);
                    delay(1700);
                    motion_landing();
                    delay(5500);
                    to_2--;
                }
                break;
            case R.id.mission2:
                motion_take_off();
                delay(1500);
                motion(0,0,0,40);
                delay(1000);
                motion(0,0,0,0);
                delay(1000);
                motion(0,0,0,20);
                delay(200);
                motion(0,0,0,0);
                delay(2500);
                motion(0,0,70,0); //yaw
                delay(1000);
                motion(0,50,0,0);   //pitch
                delay(700);
                motion(0,0,0,0);
                delay(1000);
                motion_landing();
                break;
            case R.id.Camera:
                Intent intent = new Intent(getContext(),ViewCam.class);
                startActivity(intent);
                break;
            case R.id.yaw:
                byte[] test = new byte[3];
                test[0] = 0x11;
                test[1] = -112;
                test[2] = 0x50;
                sendMessage(test);
            default:
                break;

        }
    }
/*
	private void sandingMessage(char input[]){
		if(input == null)
			return;
		// send to remote
		if(mFragmentListener != null)
			for(int cnt = 0; cnt < 4; cnt++)
				mFragmentListener.OnFragmentCallback(IFragmentListener.CALLBACK_SEND_MESSAGE, input[cnt], 0,null, null,null);
		else
			return;
	}*/

    // The action listener for the EditText widget, to listen for the return key
    /*private TextView.OnEditorActionListener mWriteListener =
        new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                if(message != null && message.length() > 0)
                	sendMessage(message);
            }
            return true;
        }
    };*/
    // Sends user message to remote
    private void sendMessage(String message) {
    	if(message == null || message.length() < 1)
    		return;
    	// send to remote
    	if(mFragmentListener != null)
    		mFragmentListener.OnFragmentCallback(IFragmentListener.CALLBACK_SEND_MESSAGE, 0, 0, message, null,null);
    	else
    		return;
    }
    private void sendMessage(byte[] message) {
        if(message == null || message.length < 1)
            return;
        // send to remote
        if(mFragmentListener != null)
            mFragmentListener.OnFragmentCallback(IFragmentListener.CALLBACK_SEND_MESSAGE, 0, 0, message, null,null);
        else
            return;
    }
    
    private static final int NEW_LINE_INTERVAL = 1000;
    private long mLastReceivedTime = 0L;
    
    // Show messages from remote
    public void showMessage(String message) {
    	if(message != null && message.length() > 0) {
    		long current = System.currentTimeMillis();
    		
    		if(current - mLastReceivedTime > NEW_LINE_INTERVAL) {
    			//mTextChat.append("\nRcv: ");
    		}
            Log.d("받은값: ",message);
            byte[] new_byte = message.getBytes();
            for(int i=0; i < new_byte.length;i++)
                System.out.printf("0x%x ",new_byte[i]);
    		//mTextChat.append(message);
        	//int scrollamout = mTextChat.getLayout().getLineTop(mTextChat.getLineCount()) - mTextChat.getHeight();
        	//if (scrollamout > mTextChat.getHeight())
        		//mTextChat.scrollTo(0, scrollamout);
        	
        	mLastReceivedTime = current;
    	}
    }
/*
    class NumberThread extends Thread{

        private boolean isPlay = false;

        public NumberThread(boolean isPlay) {this.isPlay = isPlay;}

        public void stopThread() { isPlay =!isPlay;}

        @Override
        public void run(){
            super.run();
            while(isPlay){
                byte[] sample = new byte[3];
                try{Thread.sleep(700);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                sample[0] = 0x11;
                sample[1] = -112;
                sample[2] = 0x31;
                sendInstruction(sample, 3);
                try{Thread.sleep(60);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                sample[0] = 0x11;
                sample[1] = -112;
                sample[2] = 0x31;
                for(int cnt = 0; cnt < 3; cnt++)
                    System.out.printf("보낼값: 0x%02x\n",sample[cnt]);
                sendInstruction(sample, 3);

            }
        }
    }
*/

}
