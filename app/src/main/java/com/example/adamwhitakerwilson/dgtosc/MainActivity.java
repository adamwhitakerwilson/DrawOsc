package com.example.adamwhitakerwilson.dgtosc;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private RadioButton butRadio1;
    private RadioButton butRadio2;

    private EditText ipTx;
    private EditText portTx;
    private TextView validDisplay;

    private InetAddress targetIP;
    private String targetIPStr;
    private int portNumber;
    private OSCPortOut sender = null;

    private boolean valid = false;

    String targetIpStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button butNext = (Button) findViewById(R.id.buttonNext);
        Button butConnect = (Button) findViewById(R.id.connect);

        ipTx = (EditText) findViewById(R.id.editTextIpAddress);
        portTx = (EditText) findViewById(R.id.editTextPort);

        validDisplay = (TextView) findViewById(R.id.textViewValidate);



//Button Listeners

        butConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                validDisplay.setText("");
                valid = false;
                targetIPStr = ipTx.getText().toString();
                portNumber = Integer.valueOf(portTx.getText().toString());
                setPort(portNumber);
                setIp(targetIPStr);
                Log.d("IP str :  ", targetIPStr);
                Log.d("Port:    ", Integer.toString(portNumber));

                // Start the thread that sends messages


                new Thread(new Runnable() {
                    public void run() {

                        setConnection();
                        valid = true;
                       // toast("connected");
                        //sendMyOscMessage();

                        //noinspection StatementWithEmptyBody
                        //                            validDisplay.setText(getString(R.string.connected));

                    }

                }).start();

            }


        });

        butNext.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), DrawActivity2.class);
                intent.putExtra("port", portNumber);
                intent.putExtra("ip", targetIPStr);
                startActivity(intent);


                if (valid) {
                    //open draw activity
                    sender.close();
                    startActivity(intent);
                }
                else if (!valid){
                    toast(getString(R.string.instruct));
                }


            }

        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("IP", targetIPStr);
        outState.putString("port", Integer.toString(portNumber));

    }

    //displays a toast message
    private void toast(String msg){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }

    //set OSC UDP Port Connection

    private void setConnection(){
        try {
            targetIP = InetAddress.getByName(targetIPStr);
            //targetIP = InetAddress.getLocalHost();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        //toast(getString(R.string.notConnecting));
        }

        try {
            sender = new OSCPortOut(targetIP, portNumber);//------set up outgoing ------
        } catch (SocketException e) {
            e.printStackTrace();
           // toast(getString(R.string.notConnecting));
        }

    }
    //send OSC messages

    public void sendMyOscMessage(){
        float x1 = (float) 0.0; //dummy
        float y1 = (float) 0.0;//dummy

        OSCMessage msgX1 = new OSCMessage();
        msgX1.setAddress("/X_1");
        msgX1.addArgument(x1);

        OSCMessage msgY1 = new OSCMessage();
        msgY1.setAddress("/Y_1");
        msgY1.addArgument(y1);


        try {

            sender.send(msgX1);
            sender.send(msgY1);


        } catch (Exception ignored) {

        }


    }
    public void setPort(int portNumber){
        this.portNumber = portNumber;
    }
    public int getPort(){
        return portNumber;
    }
    public void setIp(String targetIp){
        this.targetIpStr = targetIp;
    }
    public String getIp(){
        return targetIpStr;
    }

}
