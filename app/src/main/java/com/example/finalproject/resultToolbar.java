package com.example.finalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;

import android.os.Bundle;

public class resultToolbar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar tbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_toolbar);

        tbar = findViewById(R.id.toolbar);
        setSupportActionBar(tbar);

        //NavigationDrawer
        DrawerLayout drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.result_page_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch (item.getItemId()) {

            //what to do when the menu item is selected:
            case R.id.item1:
                Intent goHome = new Intent(resultToolbar.this, MainActivity.class);
                startActivity(goHome);
                break;
            /*case R.id.item2:
                Intent gotoFav = new Intent(result_page.this, Fav.class);
                startActivity(gotoFav);
                break;
            case R.id.item3:
                Intent gotoGoogle = new Intent(result_page.this, GoogleAvtivity.class);
                startActivity(gotoGoogle);
                break;*/

            case R.id.item4:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Click Favourite to save a song to Favourites. \n Click on icon, you can go to other page");
                alertDialogBuilder.setNegativeButton("Exit", null);
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String message = null;
        AlertDialog.Builder alertDialogBuilder;
        switch(item.getItemId())
        {
            case R.id.help:
                alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setNegativeButton("Exit", null);
                break;

            case R.id.donation:
                alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Donation: Please give generously.");
                alertDialogBuilder.setPositiveButton("THANK YOU", null);
                alertDialogBuilder.setNegativeButton("CANCEL", null);
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

}