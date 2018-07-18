package com.example.thesis.booktrading;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.thesis.booktrading.helper.DatabaseHelper;
import com.example.thesis.booktrading.object.Book;

import java.io.File;

public class PrepareFindingActivity extends AppCompatActivity {

    String BookID = "";
    Book currentBook = null;

    EditText editText_BookName = null;
    TextView textView_BookPrice = null;
    Spinner spinner_isBuy = null;
    EditText editText_Hops = null;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_finding);

        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        BookID = intent.getStringExtra("BookID");

        currentBook = databaseHelper.getBookfromID(BookID);

        editText_BookName = findViewById(R.id.editText_BookName);
        editText_BookName.setText(currentBook.getBookName());
        textView_BookPrice = findViewById(R.id.textView_BookPrice);
        textView_BookPrice.setText(Book.getStringPrice(currentBook.getBookPrice()));


        spinner_isBuy = findViewById(R.id.spinner_isBuy);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.prepare_buysell_label, R.layout.support_simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_isBuy.setAdapter(adapter);

    }

    public void onClick_Scan(View view) {
        String BookName = editText_BookName.getText().toString();
        float BookPrice = Float.parseFloat(textView_BookPrice.getText().toString().substring(2));
        String BookisBuy = getResources().getStringArray(R.array.prepare_buysell_value)
                [spinner_isBuy.getSelectedItemPosition()];

        Intent intent = new Intent(this, FindTargetGnutellaActivity.class);
        intent.putExtra("BookName", BookName);
        intent.putExtra("BookPrice", BookPrice);
        intent.putExtra("BookisBuy", BookisBuy);
        startActivity(intent);
    }

    public void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }
}
