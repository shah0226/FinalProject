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

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class DetailsFragment extends Fragment {

    TextView albumTextView;
    private Bundle dataFromActivity;
    private long id;
    private String artist;
    private String title;
    private String lyrics;
    private String album;
    private AppCompatActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong( FavouritesActivity.ITEM_ID);
        artist = dataFromActivity.getString( FavouritesActivity.ARTIST);
        title = dataFromActivity.getString( FavouritesActivity.TITLE);
        lyrics = dataFromActivity.getString( FavouritesActivity.LYRICS);

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.fragment_details, container, false);

        //show the artist
        TextView artistTextView = result.findViewById(R.id.artistName);
        artistTextView.setText(dataFromActivity.getString(FavouritesActivity.ARTIST));

        //show the title
        TextView titleTextView = result.findViewById(R.id.songTitle);
        titleTextView.setText(dataFromActivity.getString(FavouritesActivity.TITLE));

        ProgressBar progressBar = result.findViewById(R.id.progressBar);

        //show the lyrics
        TextView lyricsTextView = result.findViewById(R.id.songLyrics);
        lyricsTextView.setText(dataFromActivity.getString(FavouritesActivity.LYRICS));


        // get the delete button, and add a click listener:
        Button hideButton = (Button)result.findViewById(R.id.fragmentButton);

        hideButton.setOnClickListener( clk -> {
            //Tell the parent activity to remove
            getFragmentManager().beginTransaction().remove(this).commit();
            getFragmentManager().popBackStackImmediate();

        });

        albumTextView = result.findViewById(R.id.albumName);
        AlbumQuery req = new AlbumQuery();
        req.execute("https://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=4702790256e79095e292ff7393d9b9da" +
                "&artist=" + artist.replace(" ", "%20") +
                "&track=" + title.replace(" ", "%20") +
                "&format=xml");

        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }

    private class AlbumQuery extends AsyncTask<String, Integer, String> {
        private String lyrics;

        @Override
        protected String doInBackground(String... args) {
            try {
                //create a URL object of what server to contact:

                URL url = new URL(args[0]);

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

                publishProgress(50);

                while(eventType != XmlPullParser.END_DOCUMENT) {
                    if(eventType == XmlPullParser.START_TAG) {
                        if(xpp.getName().equals("title")) {
                            xpp.next();
                            album = xpp.getText();
                        }
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return album;
        }


        protected void onProgressUpdate(Integer... value) {
            ProgressBar progressBar = getView().findViewById(R.id.progressBar);
            progressBar.setProgress(value[0]);
            progressBar.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(String fromDoInBackground) {
            ProgressBar progressBar = getView().findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            albumTextView.setText(album);
        }
    }
}