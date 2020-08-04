package com.example.finalproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.net.URL;

import static com.example.finalproject.result_page.ARTIST;
import static com.example.finalproject.result_page.SONG;

public class GoogleActivity extends AppCompatActivity {
     Button yes = findViewById(R.id.yes);
     Button no = findViewById(R.id.no);
     TextView art_song = findViewById(R.id.artist_song);
    Intent intent = getIntent();
    String artist = intent.getStringExtra(ARTIST);
    String song = intent.getStringExtra(SONG);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        art_song.setText("artist: " + artist + "song: " + song);

       yes.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

            Intent songweb = new Intent( Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + artist + song ) );
            startActivity(songweb);
           }
       });

       no.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });

    }// end of onCreate()

}// end of GoogleActivity()
