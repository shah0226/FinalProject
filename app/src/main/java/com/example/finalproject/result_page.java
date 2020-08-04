package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static org.xmlpull.v1.XmlPullParser.START_TAG;

public class result_page extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "Result page";
    private ProgressBar progressBar;
    private TextView artistName, titleSong, lyrics;
    //private resultAdapter myAdapter;
   // private ArrayList<Result> list = new ArrayList<>();
    private Button btn, saveBtn, gBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        //loadDataFromDatabase(); //get any previously saved Contact objects
        saveBtn = findViewById(R.id.save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(result_page.this);
                builder.setTitle("Select your answer.");
                builder.setMessage("Do you want to save as favourite?");

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent goToSaved = new Intent(result_page.this, FavouritesActivity.class);
                                startActivity(goToSaved);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                builder.setPositiveButton("Yes", dialogClickListener);
                builder.setNegativeButton("No", dialogClickListener);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



        btn = findViewById(R.id.checkBox);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(result_page.this, getResources().getString(R.string.toast_message), Toast.LENGTH_LONG).show();
            }
        });

        gBtn = findViewById(R.id.googleSearch);
        gBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent goToGoogle = new Intent(result_page.this, GoogleActivity.class);
                // startActivity(goToGoogle);
            }
        });

        Intent fromMain = getIntent();
        String artistName = fromMain.getStringExtra("InputArtist");
        EditText resultArtist = findViewById(R.id.nameInput);
        resultArtist.setText(artistName);

        String titleInput = fromMain.getStringExtra("InputTitle");
        EditText resultTitle = findViewById(R.id.titleInput);
        resultTitle.setText(titleInput);

        String lyricsURL = "https://api.lyrics.ovh/v1/"+titleInput+"/"+artistName;
        ForecastQuery forecastQuery = new ForecastQuery();
        forecastQuery.execute(lyricsURL);
        //progressBar.setVisibility(View.VISIBLE);
    }
        private class ForecastQuery extends AsyncTask<String, Integer, String>{
            private String lyr;

            @Override
            protected String doInBackground(String... args) {
                String returnString = null;

                    try {
                        URL lURL = new URL(args[0]);
                        HttpURLConnection lUrlConnection = (HttpURLConnection) lURL.openConnection();
                        InputStream lyricsResponse = lUrlConnection.getInputStream();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(lyricsResponse, "UTF-8"), 8);
                        StringBuilder sb = new StringBuilder(300);
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }

                        String result = sb.toString();
                        Log.i("result" , result);
                        //convert string to JSON
                        JSONObject jObject = new JSONObject(result);
                        //get the double associated with "value"
                        lyr = String.valueOf(jObject.getString("lyrics"));
                    } catch (Exception e ) {
                        returnString = "error";
                        e.printStackTrace();
                    }
                        return returnString;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                Log.i("AsyncTaskExample", "update:" + values[0]);
                progressBar.setProgress(values[0]);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String fromDoInBackground) {
                super.onPostExecute(fromDoInBackground);
                TextView lyrics = findViewById(R.id.lyrics);
                lyrics.setText(String.format("Lyrics: %s", lyr));
               // progressBar.setVisibility(View.INVISIBLE);
            }
        }
}