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
    SeekBar speedBar;

    private InetAddress targetIP;
    private InetAddress targetIP2;
    String targetIpStr;
    int portNumber;
    int check;
    float spd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw2);

        forward = (RadioButton) findViewById(R.id.forward);
        backward = (RadioButton) findViewById(R.id.backward);
        backwardForward = (RadioButton) findViewById(R.id.backwardForward);
        backwardForward.setChecked(true);
        pause = (ToggleButton)findViewById(R.id.pauseButton);
        speedBar = (SeekBar)findViewById(R.id.speed);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            targetIpStr = extras.getString("ip");
            portNumber = extras.getInt("port");
        }
        setPort(portNumber);
        setIp(targetIpStr);


        dv = (DrawingView) findViewById(R.id.signature_canvas);


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
        this.portNumber = portNumber;
    }
    public int getPort(){
        return portNumber;
    }
    public void setIp(String targetIpStr){
        this.targetIpStr = targetIpStr;
    }
    public String getIp(){
        return targetIpStr;
    }
    private float normalizeX(float n) {

        n = n / (100 >> 1) - 1;
//Log.d("seek: ", Float.toString(n));
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

    public void clearCanvas(View v) {

        dv.clearDrawing();
        }



}


