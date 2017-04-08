package com.example.adamwhitakerwilson.dgtosc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class DrawActivity2 extends Activity {
    DrawingView dv;
    RadioButton forward;
    RadioButton backward;
    RadioButton backwardForward;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw2);

        dv = (DrawingView) findViewById(R.id.signature_canvas);
        forward = (RadioButton) findViewById(R.id.forward);
        backward = (RadioButton) findViewById(R.id.backward);
        backwardForward = (RadioButton) findViewById(R.id.backwardForward);

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.loopTypeRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId % 10 == 9) {
                    Log.d("BackwardForward: ", Integer.toString(checkedId));

                }
                if(checkedId % 10 == 0) {
                    Log.d("Forward: ", Integer.toString(checkedId));


                }
                if(checkedId % 10 == 1) {
                    Log.d("Backward: ", Integer.toString(checkedId));



                }
            }
        });

    }


    public void clearCanvas(View v) {

        dv.clearDrawing();
        }


    }


