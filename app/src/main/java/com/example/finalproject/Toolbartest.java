package com.example.finalproject;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class Toolbartest extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        try {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        }catch (Exception e){
//            e.printStackTrace();
//        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        String message = null;
        switch (item.getItemId()) {
            case (R.id.item1):
                message = "You clicked item 1";
                break;
            case (R.id.item2):
                message = "You clicked on the overflow menu";
                break;
        }
        Toast.makeText(this,message, Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String message = null;
        switch(item.getItemId())
        {

            case(R.id.item1):
                message = "You clicked item 1";
                Intent dataSent = getIntent();
                dataSent.putExtra("result", 500);
                setResult(500);
                finish();
                break;
            case(R.id.item2):
                message = "You clicked on the navigation +drawer icon";

                new AlertDialog.Builder(this)
                        .setMessage("Please give generously")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Thank you", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("CANCEL", null)
                        .show();

                break;
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        Toast.makeText(this,"Nav Drawer: " + message, Toast.LENGTH_LONG).show();
        return false;
    }
}
