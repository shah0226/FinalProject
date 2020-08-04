package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class result_page extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "Result page";
    private ProgressBar progressBar;
    //private TextView artistName, titleSong, lyrics;
    private resultAdapter myAdapter;
    private ArrayList<Result> list = new ArrayList<>();
    private Button btn, helpBtn, gBtn;

    public static final String ARTIST = "com.example.finalproject.artist";
    public static final String SONG = "com.example.finalproject.song";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        //loadDataFromDatabase(); //get any previously saved Contact objects

        ListView myList = findViewById(R.id.theListView);
        myList.setAdapter(myAdapter = new resultAdapter());

        //artistName = findViewById(R.id.artistname);
        //titleSong = findViewById(R.id.titlesong);
        //lyrics = findViewById(R.id.lyrics);
        progressBar = findViewById(R.id.progressBar);

        helpBtn = findViewById(R.id.help);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(result_page.this, getResources().getString(R.string.toast_message2) , Toast.LENGTH_LONG).show();
            }
        });

        btn = findViewById(R.id.checkBox);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(result_page.this, getResources().getString(R.string.toast_message) , Toast.LENGTH_LONG).show();
            }
        });

        gBtn = findViewById(R.id.googleSearch);
        gBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent goToGoogle = new Intent(result_page.this, GoogleActivity.class);
                goToGoogle.putExtra(ARTIST, list.toString().indexOf(1) );
                goToGoogle.putExtra(SONG, list.toString().indexOf(1) );
                startActivity(goToGoogle);

            }
        });

        progressBar.setVisibility(View.INVISIBLE);
    }

    class resultAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }

    class Result {

        private String artistName, songTitle, lyrics;

        public Result (String artistName, String songTitle, String lyrics){
            this.artistName = artistName;
            this.songTitle = songTitle;
            this.lyrics = lyrics;
        }

        public String getArtistName() { return artistName; }

        public void setArtistName(String artistName) {

        }

        public String getSongTitle() {return songTitle;}

        public void setSongTitle(String songTitle){

        }

        public String getLyrics(){
            return lyrics;
        }

        public void setLyrics(String lyrics){

        }

    }


}