package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static final String ACTIVITY_NAME = "MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button logButton = findViewById(R.id.searchbutton);
        logButton.setOnClickListener(click -> startactivitymethod());

    }

    private void startactivitymethod() {
        Intent nextPage = new Intent(this, MainActivity.class);
        startActivity(nextPage);
    }

}



