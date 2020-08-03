package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;

public class FavouritesActivity extends AppCompatActivity {

    public static final String ACTIVITY_NAME = "FAVOURITES_ACTIVITY";

    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final String IS_SEND = "SEND";

    private ArrayList<SavedFavourite> elements = new ArrayList<>(Arrays.asList());
    private MyListAdapter myAdapter;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        ListView myList = findViewById(R.id.myListView);
//        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded

        loadDataFromDatabase();

        myAdapter = new MyListAdapter();
        myList.setAdapter( myAdapter);

        myList.setOnItemClickListener( (list, view, position, id) -> {
            //CODE FOR SWITCHING TO NEW PAGE WITH LYRICS
            Bundle dataToPass = new Bundle();
            dataToPass.putLong(ITEM_ID, id);
            dataToPass.putString(ITEM_SELECTED, elements.get(position).getArtist());
            dataToPass.putString(ITEM_SELECTED, elements.get(position).getTitle());
            dataToPass.putString(ITEM_SELECTED, elements.get(position).getLyrics());




            //CONSIDER DOING THIS WITH FRAGMENT
//            if(isTablet)
//            {
//                DetailsFragment dFragment = new DetailsFragment(); //add a DetailFragment
//                dFragment.setArguments( dataToPass );
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
//                        .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
//
//            }
//            else //isPhone
//            {
                Intent nextActivity = new Intent(FavouritesActivity.this, SavedFavourite.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
//            }
        });

        myList.setOnItemLongClickListener( (parent, view, position, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to delete this?")
                    .setMessage("The selected row is: " + (position+1)
                            + "\nThe database ID is: " + elements.get(position).getId())
                    .setPositiveButton("Yes", (click, arg) -> {
                        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(elements.get(position).getId())});
                        elements.remove(position);
                        myAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (click, arg) -> {  })
                    .create().show();

            return true;

        });
    }


    private void loadDataFromDatabase() {
        //get a database connection"
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase(); // Calls onCreate() if you've never built the table before, onUpgrade if the version here is newer

        String[] columns = {MyOpener.COL_ID, MyOpener.COL_ARTIST, MyOpener.COL_TITLE, MyOpener.COL_LYRICS};

        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);
        printCursor(results, db.getVersion());

        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);
        int artistColIndex = results.getColumnIndex(MyOpener.COL_ARTIST);
        int titleColIndex = results.getColumnIndex(MyOpener.COL_TITLE);
        int lyricsColIndex = results.getColumnIndex(MyOpener.COL_LYRICS);

        while (results.moveToNext()) {
            String artist = results.getString(artistColIndex);
            String title = results.getString(titleColIndex);
            String lyrics = results.getString(lyricsColIndex);

            elements.add(new SavedFavourite(artist, title, lyrics));
        }
    }

    public void printCursor (Cursor c, int version){
        System.out.println("Version number: " + version);
        System.out.println("Cursor column count: " + c.getColumnCount());
        System.out.println("Cursor column names: ");
        for (int i = 0; i < c.getColumnCount(); i++)
            System.out.println("\t" + c.getColumnName(i));
        System.out.println("Cursor row count: " + c.getCount());
        System.out.println("Cursor row contents: ");
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {
            System.out.println(c.getLong(0) + ", "
                    + c.getString(1) + ", "
                    + c.getString(2) + ", "
                    + c.getString(3) + ", ");
            c.moveToNext();
        }
        c.moveToPosition(-1);
    }

    class MyListAdapter extends BaseAdapter implements ListAdapter {

        public int getCount() { return elements.size(); }

        public Object getItem(int position) { return elements.get(position); }

        public long getItemId(int position) { return position; }

        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            //make a new row:
            View newView = inflater.inflate(R.layout.activity_saved_favourite, parent, false);
            //set what the text should be for this row:
            TextView artist = newView.findViewById(R.id.artistName);
            TextView title = newView.findViewById(R.id.songTitle);
            TextView lyrics = newView.findViewById(R.id.songLyrics);
            artist.setText(elements.get(position).getArtist());
            title.setText(elements.get(position).getTitle());
            lyrics.setText(elements.get(position).getLyrics());

            //return it to be put in the table
            return newView;
        }
    }
}
