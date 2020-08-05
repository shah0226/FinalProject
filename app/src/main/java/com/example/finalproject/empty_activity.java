package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class empty_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_activity);

        //get the data that was passed from NewsReaderSearch
        Bundle dataToPass = getIntent().getExtras();
        //This is  from FragmentExample.java lines 47-54
        resultFragment newFragment = new resultFragment();
        newFragment.setArguments( dataToPass ); //pass data to the the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation, newFragment)
                .commit();
    }
}