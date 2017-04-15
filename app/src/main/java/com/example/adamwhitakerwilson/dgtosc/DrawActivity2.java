package com.example.adamwhitakerwilson.dgtosc;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

public class DrawActivity2 extends Activity {
    private DrawingView dv;
    private RadioButton forward;
    private RadioButton backward;
    private RadioButton backwardForward;
    private RadioButton out1;
    private RadioButton out2;
    private RadioButton out3;
    private RadioButton out4;
    private RadioButton out5;
    private RadioButton out6;
    private RadioButton out7;
    private RadioButton id1;
    private RadioButton id2;
    private RadioButton id3;
    private RadioButton id4;
    private RadioButton id5;
    private RadioButton id6;
    private RadioButton id7;
    private RadioButton id8;
    private ToggleButton pause;
    private int check;
    private String ip;
    private int port;
    private int outTrack = 0;
    private int idTrack = 0;
    private boolean pauser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ip = extras.getString("ip");
            port = extras.getInt("port");
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
        id1 = (RadioButton) findViewById(R.id.id1);
        id2 = (RadioButton) findViewById(R.id.id2);
        id3 = (RadioButton) findViewById(R.id.id3);
        id4 = (RadioButton) findViewById(R.id.id4);
        id5 = (RadioButton) findViewById(R.id.id5);
        id6 = (RadioButton) findViewById(R.id.id6);
        id7 = (RadioButton) findViewById(R.id.id7);
        id8 = (RadioButton) findViewById(R.id.id8);
        dv = (DrawingView) findViewById(R.id.signature_canvas);

        check = 0;
        setSender(check);
        outTrack = 1;
        setTrack(outTrack);
        idTrack = 1;
        setIdentity(idTrack);

        pause.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                setPauser(isChecked);
            }
        });

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.loopTypeRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (backward.isChecked()) {
                    check = 2;
                    setSender(check);
                }
                if (backwardForward.isChecked()) {
                    check = 0;
                    setSender(check);
                }
                if (forward.isChecked()) {
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
                    outTrack = 1;
                    setTrack(outTrack);
                }
                if (out2.isChecked()) {
                    outTrack = 2;
                    setTrack(outTrack);
                }
                if (out3.isChecked()) {
                    outTrack = 3;
                    setTrack(outTrack);
                }
                if (out4.isChecked()) {
                    outTrack = 4;
                    setTrack(outTrack);
                }
                if (out5.isChecked()) {
                    outTrack = 5;
                    setTrack(outTrack);
                }
                if (out6.isChecked()) {
                    outTrack = 6;
                    setTrack(outTrack);
                }
                if (out7.isChecked()) {
                    outTrack = 7;
                    setTrack(outTrack);
                }
            }
        });

        final RadioGroup radioGroup3 = (RadioGroup) findViewById(R.id.identitySwitch);
        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (id1.isChecked()) {
                    idTrack = 1;
                    setIdentity(idTrack);
                }
                if (id2.isChecked()) {
                    idTrack = 2;
                    setIdentity(idTrack);
                }
                if (id3.isChecked()) {
                    idTrack = 3;
                    setIdentity(idTrack);
                }
                if (id4.isChecked()) {
                    idTrack = 4;
                    setIdentity(idTrack);
                }
                if (id5.isChecked()) {
                    idTrack = 5;
                    setIdentity(idTrack);
                }
                if (id6.isChecked()) {
                    idTrack = 6;
                    setIdentity(idTrack);
                }
                if (id7.isChecked()) {
                    idTrack = 7;
                    setIdentity(idTrack);
                }
                if (id8.isChecked()) {
                    idTrack = 8;
                    setIdentity(idTrack);
                }
            }
        });

    }

    public void setIdentity(int idTrack){
        this.idTrack = idTrack;
    }

    public int getIdTrack(){
        return idTrack;
    }

    public boolean getPauser() {
        return pauser;
    }

    private void setPauser(boolean pauser) {
        this.pauser = pauser;
    }

    public int getTrack() {
        return outTrack;
    }

    private void setTrack(int outTrack) {
        this.outTrack = outTrack;
    }

    public int getPort() {
        return port;
    }

    private void setPort(int portNumber) {
        this.port = portNumber;
    }

    public String getIp() {
        return ip;
    }

    private void setIp(String targetIpStr) {
        this.ip = targetIpStr;
    }

    public int getSender() {
        return check;
    }

    private void setSender(int check) {
        this.check = check;
    }


}


