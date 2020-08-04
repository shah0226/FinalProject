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

        TextView artistname = findViewById(R.id.enterartistname);
        TextView titleSong = findViewById(R.id.enterartitlesong);
        Button resultButton = findViewById(R.id.resultbutton);
        resultButton.setOnClickListener(click -> {
            Intent goToResult = new Intent(MainActivity.this, result_page.class);
            goToResult.putExtra("InputArtist", artistname.getText().toString());
            goToResult.putExtra("InputTitle", titleSong.getText().toString());
            startActivity(goToResult);
        });

    }
}