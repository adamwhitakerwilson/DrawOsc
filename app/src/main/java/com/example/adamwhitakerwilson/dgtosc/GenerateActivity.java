package com.example.adamwhitakerwilson.dgtosc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GenerateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
        @SuppressWarnings("UnusedAssignment") Intent intent = getIntent();
    }
}
