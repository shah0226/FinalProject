package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class result_page extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    protected static final String ACTIVITY_NAME = "Result page";
    private ProgressBar progressBar;
    private TextView artistNameView, titleInputView, titleSong, lyrics;
    private ArrayList<SavedFavourite> elements = new ArrayList<>();
    SQLiteDatabase db;
    private Button btn, saveBtn;
    protected String artistName;
    protected String titleInput;

    public static final String ARTIST = "Artist";
    public static final String TITLE = "TITLE";
    public static final String LYRICS = "LYRICS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        //loadDataFromDatabase(); //get any previously saved Contact objects
        saveBtn = findViewById(R.id.goToFaveList);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToFaves = new Intent(result_page.this, FavouritesActivity.class);
                startActivity(goToFaves);
            }

        });

        btn = findViewById(R.id.backToMain);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goHome = new Intent(result_page.this, MainActivity.class);
                startActivity(goHome);
            }
        });

        //toolbar
        Toolbar myToolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(myToolbar);

        //NavigationDrawer
        DrawerLayout drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);


        Intent fromMain = getIntent();
        artistName = fromMain.getStringExtra("InputArtist");
        EditText resultArtist = findViewById(R.id.nameInput);
        resultArtist.setText(artistName);

        titleInput = fromMain.getStringExtra("InputTitle");
        EditText resultTitle = findViewById(R.id.titleInput);
        resultTitle.setText(titleInput);

        if(resultArtist.getText().toString() != artistName && resultTitle.getText().toString() != titleInput) {
            String lyricsURL = "https://api.lyrics.ovh/v1/" + artistName.replace(" ", "%20") + "/" +
                    titleInput.replace("0", "%20");
            ResultQuery ResultQuery = new ResultQuery();
            ResultQuery.execute(lyricsURL);
        }else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("You have entered invalid value");
            alertDialogBuilder.setNegativeButton("Exit", (click,arg) -> {
                Intent goHome = new Intent(result_page.this, MainActivity.class);
                startActivity(goHome);
            });

            alertDialogBuilder.create().show();
        }
    }

    private void loadDataFromDatabase() {

        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase(); // Calls onCreate() if you've never built the table before, onUpgrade if the version here is newer

        String[] columns = {MyOpener.COL_ID, MyOpener.COL_ARTIST, MyOpener.COL_TITLE, MyOpener.COL_LYRICS};

        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);
 //       printCursor(results, db.getVersion());

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Look at your menu XML file. Put a case for every id in that file:
        switch (item.getItemId()) {
            //what to do when the menu item is selected:

            case R.id.help:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Click back to the main page. Click Favourite to save a song to Favourites.");
                alertDialogBuilder.setNegativeButton("Exit", null);
                alertDialogBuilder.create().show();
                break;
        }
        return true;
    }

    // Needed for the OnNavigationItemSelected interface:
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String message2 = null;
        switch(item.getItemId())
        {

            case R.id.help:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Click back to the main page. Click Favourite to save a song to Favourites.");
                alertDialogBuilder.setNegativeButton("Exit", null);
                alertDialogBuilder.create().show();
                break;
        }
        Toast.makeText(this, message2, Toast.LENGTH_LONG).show();
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private class ResultQuery extends AsyncTask<String, Integer, String>{
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
                ContentValues newRowValues = new ContentValues();

                loadDataFromDatabase();

                newRowValues.put(MyOpener.COL_ARTIST, artistName);
                newRowValues.put(MyOpener.COL_TITLE, titleInput);
                newRowValues.put(MyOpener.COL_LYRICS, lyr);

                long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

                SavedFavourite favourite = new SavedFavourite(artistName, titleInput, lyr, newId);
                elements.add(favourite);
               // progressBar.setVisibility(View.INVISIBLE);
            }
        }
}