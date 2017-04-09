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

public class DrawActivity2 extends Activity {
    DrawingView dv;
    RadioButton forward;
    RadioButton backward;
    RadioButton backwardForward;
    RadioButton out1, out2, out3, out4, out5, out6, out7;
    ToggleButton pause;
    ToggleButton relationToggle;
    SeekBar speedBar;

    int check;
    float spd;

    String ip;
    int port;
    int outTrack = 0;

    boolean relationTrigger;
    boolean pauser;

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


        pause = (ToggleButton) findViewById(R.id.pauseButton);

        out1 = (RadioButton) findViewById(R.id.out1);
        out2 = (RadioButton) findViewById(R.id.out2);
        out3 = (RadioButton) findViewById(R.id.out3);
        out4 = (RadioButton) findViewById(R.id.out4);
        out5 = (RadioButton) findViewById(R.id.out5);
        out6 = (RadioButton) findViewById(R.id.out6);
        out7 = (RadioButton) findViewById(R.id.out7);


        relationToggle = (ToggleButton) findViewById(R.id.relationButton);

        dv = (DrawingView) findViewById(R.id.signature_canvas);


        relationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                Log.d("tog:", Boolean.toString(isChecked));
                setRelationTrigger(isChecked);

            }
        });

        pause.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                Log.d("tog:", Boolean.toString(isChecked));
                setPauser(isChecked);

            }
        });


        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.loopTypeRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (backward.isChecked()) {
                    //  Log.d("Backward: ", Integer.toString(checkedId));
                    check = 2;
                    setSender(check);
                }
                if (backwardForward.isChecked()) {
                    //  Log.d("BackwardForward: ", Integer.toString(checkedId));
                    check = 0;
                    setSender(check);

                }
                if (forward.isChecked()) {
                    //  Log.d("Forward: ", Integer.toString(checkedId));

                    check = 1;
                    setSender(check);

                }

            }
        });


        final RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.outputs);
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                if (out1.isChecked()) {
                    //  Log.d("BackwardForward: ", Integer.toString(checkedId));
                    outTrack = 1;
                    setTrack(outTrack);

                }
                if (out2.isChecked()) {
                    //  Log.d("Backward: ", Integer.toString(checkedId));
                    outTrack = 2;
                    setTrack(outTrack);
                }
                if (out3.isChecked()) {
                    //  Log.d("Forward: ", Integer.toString(checkedId));

                    outTrack = 3;
                    setTrack(outTrack);

                }
                if (out4.isChecked()) {
                    //  Log.d("Forward: ", Integer.toString(checkedId));

                    outTrack = 4;
                    setTrack(outTrack);

                }
                if (out5.isChecked()) {
                    //  Log.d("Forward: ", Integer.toString(checkedId));

                    outTrack = 5;
                    setTrack(outTrack);

                }
                if (out6.isChecked()) {
                    //  Log.d("Forward: ", Integer.toString(checkedId));

                    outTrack = 6;
                    setTrack(outTrack);

                }
                if (out7.isChecked()) {
                    //  Log.d("Forward: ", Integer.toString(checkedId));

                    outTrack = 7;
                    setTrack(outTrack);

                }

            }
        });

    }

    public boolean getPauser() {
        return pauser;
    }

    public void setPauser(boolean pauser) {
        this.pauser = pauser;
    }

    public int getTrack() {
        return outTrack;
    }

    public void setTrack(int outTrack) {
        this.outTrack = outTrack;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int portNumber) {
        this.port = portNumber;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String targetIpStr) {
        this.ip = targetIpStr;
    }

    private float normalizeX(float n) {
        // seek max = 100
        n = n / (100 >> 1) - 1;
        return n;
    }

    public int getSender() {
        return check;
    }

    public void setSender(int check) {
        this.check = check;
    }

    public float getSpeed() {
        return spd;
    }

    public void setSpeed(float spd) {
        this.spd = spd;
    }

    public boolean getRelationTrigger() {
        return relationTrigger;
    }

    public void setRelationTrigger(boolean trigger) {
        this.relationTrigger = trigger;
    }

    public void clearCanvas(View v) {

        dv.clearDrawing();
    }


}


