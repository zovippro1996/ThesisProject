package com.example.thesis.booktrading;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.thesis.booktrading.gnutellaprotocol.GnutellaCoordinator;
import com.example.thesis.booktrading.gnutellaprotocol.Searcher;
import com.example.thesis.booktrading.helper.DatabaseHelper;
import com.google.android.gms.nearby.connection.ConnectionsClient;

import java.io.File;

public class FindTargetGnutellaActivity extends AppCompatActivity {

    String BookName = "";

    private DatabaseHelper databaseHelper;

    GnutellaCoordinator gnutellaCoordinator = null;
    /**
     * Our handler to Nearby Connections.
     */
    private ConnectionsClient mConnectionsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_target_gnutella);

        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        BookName = intent.getStringExtra("BookName");

        String preferencesDir = this.getFilesDir().getAbsolutePath() + "/preferences.txt";
        gnutellaCoordinator = new GnutellaCoordinator(preferencesDir);

        gnutellaCoordinator.addSearchGnutella(BookName);

        Searcher custom = new Searcher(this);
    }
}
