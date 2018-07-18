package com.example.thesis.booktrading;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.thesis.booktrading.gnutellaprotocol.GnutellaCoordinator;
import com.example.thesis.booktrading.helper.DatabaseHelper;
import com.example.thesis.booktrading.helper.FragmentInventory;
import com.example.thesis.booktrading.helper.FragmentWishList;
import com.example.thesis.booktrading.helper.ViewPagerAdapter;
import com.example.thesis.booktrading.object.PersonalInfo;

import java.io.File;
import java.io.FileOutputStream;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseHelper databaseHelper;

    TextView textView_headerFullName = null;
    TextView textView_headerPhone = null;

    PersonalInfo currentPersonalInfo = null;

    GnutellaCoordinator gnutellaCoordinator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get ready to access the database later
        databaseHelper = new DatabaseHelper(this);
        currentPersonalInfo = databaseHelper.getPersonalInfoRecord();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_addItem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent new_intent = new Intent(DashboardActivity.this, NewInventoryItem.class);
                startActivity(new_intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        textView_headerFullName = (TextView) header.findViewById(R.id.textView_headerFullName);
        textView_headerFullName.setText(currentPersonalInfo.getPersonalInfoFullName());
        textView_headerPhone = (TextView) header.findViewById(R.id.textView_headerPhone);
        textView_headerPhone.setText(currentPersonalInfo.getPersonalInfoPhone());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager_1);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(new FragmentWishList(), "WISH LIST");
        adapter.addFragment(new FragmentInventory(), "INVENTORY");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout_main);
        tabLayout.setupWithViewPager(viewPager);

        //Shared Directory
        File optDir = this.getDir("opt", Context.MODE_PRIVATE);
        //Download Directory
        File ccrickDir = this.getDir("ccrick", Context.MODE_PRIVATE);

        //Create Preference File in File Directory
        String filename = "preferences.txt";
        String fileContents = "Max-Live: 5\n" +
                "Max-Cache: 50\n" +
                "Auto-Connect: true\n" +
                "Shared-Directory: " + optDir.getAbsolutePath() + "\n" +
                "Download-Directory: " + ccrickDir.getAbsolutePath() + "\n";

        File file = new File(this.getFilesDir(), filename);
        if (!file.exists()){
            FileOutputStream outputStream;
            try {
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(fileContents.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String preferencesDir = this.getFilesDir().getAbsolutePath() + "/preferences.txt";
        gnutellaCoordinator = new GnutellaCoordinator(preferencesDir);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, MyProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_transacts) {
            Intent intent = new Intent(this, TransactionHistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_close) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
