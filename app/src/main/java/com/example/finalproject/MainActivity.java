package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String ACTIVITY_NAME = "MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView artistname = findViewById(R.id.artistname);
        Button resultButton = findViewById(R.id.resultbutton);
        resultButton.setOnClickListener(click -> {
            Intent goToResult = new Intent(MainActivity.this, result_page.class);
            goToResult.putExtra("artistname", artistname.getText().toString());
            startActivity(goToResult);
        });

    }
}