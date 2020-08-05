package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static final String ACTIVITY_NAME = "MAIN_ACTIVITY";

    SharedPreferences prefs = null;
    private ArrayList<SavedFavourite> elements = new ArrayList<>(Arrays.asList());
    SQLiteDatabase db;
    protected String artist;
    protected String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView artistname = findViewById(R.id.enterartistname);
        TextView titleSong = findViewById(R.id.enterartitlesong);
        Button resultButton = findViewById(R.id.searchButton);
        resultButton.setOnClickListener(click -> {
            Intent goToResult = new Intent(MainActivity.this, result_page.class);
            goToResult.putExtra("InputArtist", artistname.getText().toString());
            goToResult.putExtra("InputTitle", titleSong.getText().toString());
            startActivity(goToResult);
        });

        Button toSavedFavourites = findViewById(R.id.savedFavouritesButton);
        EditText artistName = findViewById(R.id.enterartistname);
        EditText songTitle = findViewById(R.id.enterartitlesong);


        toSavedFavourites.setOnClickListener(click -> {
            Intent nextPage = new Intent(this, FavouritesActivity.class);
            nextPage.putExtra("Artist", artistName.getText().toString());
            nextPage.putExtra("Title", songTitle.getText().toString());
            startActivity(nextPage);
        });


        Button searchAPIButton = findViewById(R.id.searchAPIButton);
        searchAPIButton.setOnClickListener(lb -> {
            artist = artistName.getText().toString();
            title = songTitle.getText().toString();

            SongQuery req = new SongQuery();
            loadDataFromDatabase();
            String url = "https://api.lyrics.ovh/v1/" +
                    artist.replace(" ", "%20") + "/" +
                    title.replace(" ", "%20");
            req.execute(url);
        });
    }

    private void loadDataFromDatabase() {

        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase(); // Calls onCreate() if you've never built the table before, onUpgrade if the version here is newer

        String[] columns = {MyOpener.COL_ID, MyOpener.COL_ARTIST, MyOpener.COL_TITLE, MyOpener.COL_LYRICS};

        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);
//        printCursor(results, db.getVersion());

        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);
        int artistColIndex = results.getColumnIndex(MyOpener.COL_ARTIST);
        int titleColIndex = results.getColumnIndex(MyOpener.COL_TITLE);
        int lyricsColIndex = results.getColumnIndex(MyOpener.COL_LYRICS);

        while (results.moveToNext()) {
            long id = results.getLong(idColIndex);
            String artist = results.getString(artistColIndex);
            String title = results.getString(titleColIndex);
            String lyrics = results.getString(lyricsColIndex);

            //add the new Song to the array list:
            elements.add(new SavedFavourite(artist, title, lyrics, id));
        }
    }


    private class SongQuery extends AsyncTask<String, String, String> {
        private String lyrics;

        @Override
        protected String doInBackground(String... args) {
            try {
                //create a URL object of what server to contact:

                URL url = new URL(args[0]);

                artist = artist.replace("%20", " ");
                title = title.replace("%20", " ");

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //From part 3: slide 19
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(response, "UTF-8");

                //From part 3, slide 20
                String parameter = null;

                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                response = urlConnection.getInputStream();

                //JSON reading:   Look at slide 26
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                    sb.append(line + "\n");

                String result = sb.toString(); //result is the whole string

                JSONObject returnedInfo = new JSONObject(result);
                lyrics = returnedInfo.getString("lyrics");


            } catch (Exception e) {
                e.printStackTrace();
            }

            return title;
        }


        protected void onProgressUpdate(Integer... value) {
//            ProgressBar progressBar = findViewById(R.id.progressBar);
//            progressBar.setProgress(value[0]);
//            progressBar.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(String fromDoInBackground) {

            title = fromDoInBackground;
            ContentValues newRowValues = new ContentValues();

            loadDataFromDatabase();

            newRowValues.put(MyOpener.COL_ARTIST, artist);
            newRowValues.put(MyOpener.COL_TITLE, title);
            newRowValues.put(MyOpener.COL_LYRICS, lyrics);


            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

            SavedFavourite favourite = new SavedFavourite(artist, title, lyrics, newId);
            elements.add(favourite);
        }
    }
}