package com.example.adamwhitakerwilson.dgtosc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class DrawActivity2 extends Activity{
    DrawingView dv;
    RadioButton forward;
    RadioButton backward;
    RadioButton backwardForward;
    ToggleButton pause;
    ToggleButton relationToggle;
    SeekBar speedBar;

    int check;
    float spd;

    String ip;
    int port;

    boolean relationTrigger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ip = extras.getString("ip");
            port = extras.getInt("port");
            Log.d("ip: ", ip);
            Log.d("port: ", Integer.toString(port));
            setPort(port);
            setIp(ip);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw2);

        forward = (RadioButton) findViewById(R.id.forward);
        backward = (RadioButton) findViewById(R.id.backward);
        backwardForward = (RadioButton) findViewById(R.id.backwardForward);
        backwardForward.setChecked(true);
        pause = (ToggleButton)findViewById(R.id.pauseButton);
        speedBar = (SeekBar)findViewById(R.id.speed);
        relationToggle = (ToggleButton)findViewById(R.id.relationTogg);

        dv = (DrawingView) findViewById(R.id.signature_canvas);

        pause.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked)
            {
                Log.d("tog:", Boolean.toString(isChecked));
                setRelationTrigger(isChecked);

            }
        });
        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
              setSpeed(normalizeX((float)progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });




    final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.loopTypeRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(backward.isChecked()) {
                  //  Log.d("Backward: ", Integer.toString(checkedId));
                    check = 2;
                    setSender(check);
                }
                if(backwardForward.isChecked()) {
                  //  Log.d("BackwardForward: ", Integer.toString(checkedId));
                    check = 0;
                    setSender(check);

                }
                if(forward.isChecked()) {
                  //  Log.d("Forward: ", Integer.toString(checkedId));

                    check = 1;
                    setSender(check);

                }

            }
        });


    }
    public void setPort(int portNumber){
        this.port = portNumber;
    }
    public int getPort(){
        return port;
    }
    public void setIp(String targetIpStr){
        this.ip = targetIpStr;
    }
    public String getIp(){
        return ip;
    }
    private float normalizeX(float n) {
    // seek max = 100
        n = n / (100 >> 1) - 1;
        return n;
    }
    public void setSender(int check){
        this.check= check;
    }
    public int getSender(){
        return check;
    }
    public void setSpeed(float spd){
        this.spd = spd;
    }
    public float getSpeed(){
        return spd;
    }
    public  void setRelationTrigger(boolean trigger){
        this.relationTrigger = trigger;
    }
    public  boolean getRelationTrigger(){
        return relationTrigger;
    }
    public void clearCanvas(View v) {

        dv.clearDrawing();
        }



}


