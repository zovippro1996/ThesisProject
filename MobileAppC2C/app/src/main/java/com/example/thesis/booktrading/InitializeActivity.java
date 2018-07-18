package com.example.thesis.booktrading;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thesis.booktrading.helper.DatabaseHelper;
import com.example.thesis.booktrading.object.PersonalInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InitializeActivity extends AppCompatActivity {

    //Initialize
    String FullName = "";
    String Phone = "";

    EditText editText_FullName = null;
    EditText editText_Phone = null;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize);

        // Get ready to access the database later
        databaseHelper = new DatabaseHelper(this);

        long numberProfileInfo = databaseHelper.getNumberOfPersonalInfo();

        //Check if the application is initialized
        if (numberProfileInfo>0){
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }

        editText_FullName = findViewById(R.id.editText_FullName);
        editText_Phone = findViewById(R.id.editText_Phone);

    }

    private boolean validateInitializeInfo(){

        if (FullName.isEmpty()){
            editText_FullName.setError("Required Field cannot be empty");
            return false;
        }

        Pattern p = Pattern.compile("^[ A-Za-z]+$");
        Matcher m = p.matcher(FullName);
        boolean isValidFullName = m.matches();
        if (!isValidFullName){
            editText_FullName.setError("Invalid Format");
            return false;
        }

        if (Phone.isEmpty()){
            editText_Phone.setError("Required Field cannot be empty");
            return false;
        }

        return true;
    }

    public void onClick_Save(View view) {

        FullName = editText_FullName.getText().toString();
        Phone = editText_Phone.getText().toString();

        if(validateInitializeInfo()){

            PersonalInfo newProfile = new PersonalInfo(FullName, Phone);

            try {
                // Insert into database
                long result_rowID = databaseHelper.insertPersonalInfo(newProfile);

                // Notify successful saving
                popupToast("Successfully Initialized");
                Intent intent = new Intent(this, DashboardActivity.class);
                startActivity(intent);
                finish();

            } catch (SQLiteException sqle) {
                Log.w(this.getClass().getName(), "Error saving to database");

                // Notify unsuccessful saving
                popupToast("Couldn't save your review into database");
            }
        }
    }

    private void popupToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
