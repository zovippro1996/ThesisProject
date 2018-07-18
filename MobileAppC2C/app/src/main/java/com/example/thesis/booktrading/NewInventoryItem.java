package com.example.thesis.booktrading;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.thesis.booktrading.helper.DatabaseHelper;
import com.example.thesis.booktrading.object.Book;

public class NewInventoryItem extends AppCompatActivity {

    Spinner spinner = null;
    EditText editText_Price = null;
    CheckBox checkBox_isPossess = null;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_inventory_item);

        databaseHelper = new DatabaseHelper(this);

        // Import (currency) items to two spinners
        spinner = findViewById(R.id.spinner_Book);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.book_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        editText_Price = findViewById(R.id.editText_Price);
        checkBox_isPossess = findViewById(R.id.checkBox_isPossess);
    }

    public void onClick_Save(View view) {
        int isPossess = 0;
        String bookName = spinner.getSelectedItem().toString();
        float bookPrice = Float.parseFloat(editText_Price.getText().toString());

        if(checkBox_isPossess.isChecked()){
            isPossess = 1;
        }

        if(isValidAllInputs()){
            try{
                Book newBook = new Book(bookName, bookPrice, isPossess);
                // Insert into database
                long result_rowID = databaseHelper.insertBook(newBook);
                Intent intent = new Intent(this, DashboardActivity.class);
                startActivity(intent);
            } catch (SQLiteException sqle){
                Log.w(this.getClass().getName(), "Error saving to database");
                // Notify unsuccessful saving
                popupToast("Couldn't save your review into database");
            }
        }
    }

    private boolean isValidAllInputs(){

        String bookPrice = editText_Price.getText().toString();

        if (bookPrice.isEmpty()) {
            editText_Price.setError("Required Field cannot be empty");
            return false;
        }

        return true;
    }

    private void popupToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
