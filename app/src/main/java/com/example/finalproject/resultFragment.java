package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class resultFragment extends Fragment {

    private Bundle dataFromActivity;
    private long id;
    private String artist;
    private String title;
    private String lyrics;
    private String album;
    private AppCompatActivity parentActivity;
    private ContentValues newRow = new ContentValues();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong("id");
        artist = dataFromActivity.getString("Artist");
        title = dataFromActivity.getString("Title");
        lyrics = dataFromActivity.getString("Lyrics");

        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.activity_result_fragment, container, false);

        //show the artist
        TextView artistTextView = result.findViewById(R.id.artistName);
        artistTextView.setText(dataFromActivity.getString(result_page.ARTIST));

        //show the title
        TextView titleTextView = result.findViewById(R.id.titleSong);
        titleTextView.setText(dataFromActivity.getString(result_page.TITLE));

        ProgressBar progressBar = result.findViewById(R.id.progressBar);

        //show the lyrics
        TextView lyricsTextView = result.findViewById(R.id.lyrics);
        lyricsTextView.setText(dataFromActivity.getString(result_page.LYRICS));


        Button faveBtn = result.findViewById(R.id.buttonFave);
        faveBtn.setOnClickListener(clk -> {



        });

        // get the back button, and add a click listener to finish and go back to the list of titles
        Button backBtn = result.findViewById(R.id.buttonBack);
        backBtn.setOnClickListener(clk -> {
            //Tell the parent activity to remove
            parentActivity.finish();
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity) context;
    }
}