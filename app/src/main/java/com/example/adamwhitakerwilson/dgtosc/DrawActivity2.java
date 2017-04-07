package com.example.adamwhitakerwilson.dgtosc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class DrawActivity2 extends Activity {
    DrawingView dv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw2);

        dv = (DrawingView) findViewById(R.id.signature_canvas);
        }

    public void clearCanvas(View v) {
        dv.clearDrawing();
    }
    }


