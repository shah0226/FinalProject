package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class FavouritesActivity extends AppCompatActivity {

    public static final String ACTIVITY_NAME = "FAVOURITES_ACTIVITY";

    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final String ARTIST = "ARTIST";
    public static final String TITLE = "TITLE";
    public static final String LYRICS = "LYRICS";

    SharedPreferences prefs = null;
    private ArrayList<SavedFavourite> elements = new ArrayList<>(Arrays.asList());
    private MyListAdapter myAdapter;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        prefs = getSharedPreferences("Last Lookup", Context.MODE_PRIVATE);
        String savedString = prefs.getString("Last Lookup", "");
        EditText favouritesSearchInput = findViewById(R.id.favouritesSearch);
        favouritesSearchInput.setHint(getResources().getString(R.string.lastSearch) + savedString);


        ListView myList = findViewById(R.id.myListView);
        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded

        loadDataFromDatabase(false, null);

        myAdapter = new MyListAdapter();
        myList.setAdapter( myAdapter);

        //Old way of doing the search

        Button favouritesSearchButton = findViewById(R.id.favouritesSearchButton);
        favouritesSearchButton.setOnClickListener(click -> {
            //Saving user's most recent search to display again next time

            saveSharedPrefs(favouritesSearchInput.getText().toString());

            String searchTerm = favouritesSearchInput.getText().toString();

            //If something was typed into the search bar
            if (searchTerm.length() > 0) {
                loadDataFromDatabase(true, searchTerm);
                myAdapter.notifyDataSetChanged();
            } else {
                Snackbar.make(favouritesSearchButton, getResources().getString(R.string.favouritesSnackbarString), Snackbar.LENGTH_SHORT).show();
                loadDataFromDatabase(false, null);
                myAdapter.notifyDataSetChanged();
            }

            //Hide Keyboard
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(favouritesSearchInput.getWindowToken(), 0);
        });

        myList.setOnItemClickListener( (list, view, position, id) -> {
            //Code for switching to fragment with selected item's lyrics
            //Creating and passing a bundle with that item's info
            Bundle dataToPass = new Bundle();
            dataToPass.putLong(ITEM_ID, id);
            dataToPass.putString(ARTIST, elements.get(position).getArtist());
            dataToPass.putString(TITLE, elements.get(position).getTitle());
            dataToPass.putString(LYRICS, elements.get(position).getLyrics());

            if(isTablet) {
                DetailsFragment dFragment = new DetailsFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass );
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
            } else {
                //isPhone
                Intent nextActivity = new Intent(FavouritesActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }
        });


        myList.setOnItemLongClickListener( (parent, view, position, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getResources().getString(R.string.favouritesDeleteQuestion))
                    .setMessage(getResources().getString(R.string.favouritesDeleteArtist) + elements.get(position).getArtist()
                            + "\n" + getResources().getString(R.string.favouritesDeleteSong) + elements.get(position).getTitle())
                    .setPositiveButton(getResources().getString(R.string.favouritesDeleteYes), (click, arg) -> {
                        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(elements.get(position).getId())});
                        elements.remove(position);
                        myAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(getResources().getString(R.string.favouritesDeleteNo), (click, arg) -> {  })
                    .create().show();

            return true;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void saveSharedPrefs(String stringToSave) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("Last Lookup", stringToSave);
        edit.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favourites_activity_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.help_item:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(getResources().getString(R.string.favouritesInstructionsTitle))
                        .setMessage(getResources().getString(R.string.favouritesInstructionsBody))
                        .setPositiveButton("OK", (click, arg) -> {}).create().show();;
                break;
        }
        return true;
    }

    private void loadDataFromDatabase(boolean searching, String searchTerm) {
        //get a database connection"
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase(); // Calls onCreate() if you've never built the table before, onUpgrade if the version here is newer

        String[] columns = {MyOpener.COL_ID, MyOpener.COL_ARTIST, MyOpener.COL_TITLE, MyOpener.COL_LYRICS};

        Cursor results;

        if (searchTerm!=null) {
            searchTerm = "%" + searchTerm + "%";
            elements.clear();
            String[] args = {searchTerm, searchTerm};
            results = db.query(false, MyOpener.TABLE_NAME, columns,  MyOpener.COL_ARTIST + " LIKE ? OR " + MyOpener.COL_TITLE + " LIKE ?" , args, null, null, null, null);
        } else {
            elements.clear();
            results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);
        }

        int artistColIndex = results.getColumnIndex(MyOpener.COL_ARTIST);
        int titleColIndex = results.getColumnIndex(MyOpener.COL_TITLE);
        int lyricsColIndex = results.getColumnIndex(MyOpener.COL_LYRICS);
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);


        while (results.moveToNext()) {
            String artist = results.getString(artistColIndex);
            String title = results.getString(titleColIndex);
            String lyrics = results.getString(lyricsColIndex);
            int id = results.getInt( idColIndex);

            elements.add(new SavedFavourite(artist, title, lyrics, id));
        }

        if (elements.size() == 1)
            Toast.makeText(this, getResources().getString(R.string.foundOneSong), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, getResources().getString(R.string.foundManySongs1) + elements.size() + getResources().getString(R.string.foundManySongs2), Toast.LENGTH_SHORT).show();
    }

    class MyListAdapter extends BaseAdapter implements ListAdapter {

        public int getCount() { return elements.size(); }

        public Object getItem(int position) { return elements.get(position); }

        public long getItemId(int position) { return position; }

        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            //make a new row:
            View newView = inflater.inflate(R.layout.favourite_individual, parent, false);
            //set what the text should be for this row:
            TextView artistTextView = newView.findViewById(R.id.artistName);
            TextView titleTextView = newView.findViewById(R.id.songTitle);

            artistTextView.setText(elements.get(position).getArtist());
            titleTextView.setText(elements.get(position).getTitle());

            //return it to be put in the table
            return newView;
        }
    }


}
