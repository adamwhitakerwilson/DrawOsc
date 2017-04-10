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
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    String targetIpStr;
    private EditText ipTx;
    private EditText portTx;
    private TextView validDisplay;
    private InetAddress targetIP;
    private String targetIPStr;
    private int portNumber;
    private OSCPortOut sender = null;
    private boolean valid = false;


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
            public void onClick(View v) {

                validDisplay.setText("");
                valid = false;
                targetIPStr = ipTx.getText().toString();
                if(!portTx.getText().toString().isEmpty()) {
                    portNumber = Integer.valueOf(portTx.getText().toString());
                }
if(ValidateIPV4.isValidIPV4(targetIPStr) && portNumber <= 65535) {

    // Start the thread that sends messages

    new Thread(new Runnable() {
        public void run() {

            setConnection();
            validDisplay.setText(getString(R.string.connected));
            valid = true;
           // toast(getString(R.string.connected));

        }

    }).start();
}
else{
    toast(getString(R.string.ipInvalid));
}
            }


        });

        butNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DrawActivity2.class);
                intent.putExtra("port", portNumber);
                intent.putExtra("ip", targetIPStr);



                if (valid) {
                    //open draw activity
                    startActivity(intent);
                } else if (!valid) {
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
    private void toast(String msg) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }

    //set OSC UDP Port Connection

    private void setConnection() {
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

    public void sendMyOscMessage() {
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

    public int getPort() {
        return portNumber;
    }

    public void setPort(int portNumber) {
        this.portNumber = portNumber;
    }

    public String getIp() {
        return targetIpStr;
    }

    public void setIp(String targetIp) {
        this.targetIpStr = targetIp;
    }


}
class ValidateIPV4
{

    static private final String IPV4_REGEX = "(([0-1]?[0-9]{1,2}\\.)|(2[0-4][0-9]\\.)|(25[0-5]\\.)){3}(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))";
    static private Pattern IPV4_PATTERN = Pattern.compile(IPV4_REGEX);

    public static boolean isValidIPV4(final String s)
    {
        return IPV4_PATTERN.matcher(s).matches();
    }
}
