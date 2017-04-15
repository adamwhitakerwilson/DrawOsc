package com.example.adamwhitakerwilson.dgtosc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class GenerateActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);



    }
}

/*
    int moveTime = 1000;

    Timer timer = new Timer();

    public Bot(){
        timer.schedule(new Task(), moveTime);
    }

public class Task extends TimerTask {
    @Override
    public void run() {
        Log.d("Timer", "Time Passed from last repeat:"+Integer.toString(movetime));
        moveTime += 1000;
        timer.schedule(new Task(), moveTime)
    }

}



  Timer timer=new Timer();



        timer.schedule(
                new TimerTask(){
                    @Override
                    public void run() {
                        update();
                    }
                }
                ,0,10 );

        timer.schedule(
                new TimerTask(){
                    @Override
                    public void run() {
                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        update();
                                    }
                                }
                        );

                    }
                },0,10 );



*/