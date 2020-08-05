package com.example.finalproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GoogleActivity extends AppCompatActivity {
     Button yes = findViewById(R.id.yes);
     Button no = findViewById(R.id.no);
     TextView art_song = findViewById(R.id.artist_song);
    Intent intent = getIntent();
    String artist = intent.getStringExtra(InputArtist);
    String song = intent.getStringExtra(InputTitle);
    Toolbar toolbar = findViewById(R.id.tool);



    @Override
    public void setSupportActionBar(@Nullable androidx.appcompat.widget.Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int random = 1;
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(random, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.choice1:
                message = "You clicked choice 1";
                break;
            case R.id.choice2:
                message = "You clicked choice 2";
                break;
            case R.id.choice3:
                message = "You clicked on help";
                break;
            case R.id.aboutproject:
                Toast.makeText(this,"this is the google activity written by Robert  Nix", Toast.LENGTH_LONG);
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }


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
