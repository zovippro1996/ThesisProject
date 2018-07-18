package com.example.thesis.booktrading;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;

import com.example.thesis.booktrading.helper.DatabaseHelper;
import com.example.thesis.booktrading.helper.TransactCursorAdapter;

public class TransactionHistoryActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        databaseHelper = new DatabaseHelper(this);
        Cursor allTransactsCursor = databaseHelper.getAllTransacts();
        // Create a new list adapter bound to the cursor

        TransactCursorAdapter adapter = new TransactCursorAdapter(
                this,
                R.layout.list_transact_record,
                allTransactsCursor,
                ResourceCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );

        listView = findViewById(R.id.listView_Transacts);
        listView.setAdapter(adapter);
    }
}
