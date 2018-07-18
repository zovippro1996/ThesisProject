package com.example.thesis.booktrading;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.thesis.booktrading.helper.DatabaseHelper;
import com.example.thesis.booktrading.object.PersonalInfo;

public class MyProfileActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    PersonalInfo currentPersonalInfo = null;

    TextView textView_FullName = null;
    TextView textView_Phone = null;
    TextView textView_NumberTransacts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get ready to access the database later
        databaseHelper = new DatabaseHelper(this);

        currentPersonalInfo = databaseHelper.getPersonalInfoRecord();
        setContentView(R.layout.activity_my_profile);

        textView_FullName = findViewById(R.id.textView_FullName);
        textView_FullName.setText(currentPersonalInfo.getPersonalInfoFullName());
        textView_Phone = findViewById(R.id.textView_Phone);
        textView_Phone.setText(currentPersonalInfo.getPersonalInfoPhone());
        textView_NumberTransacts = findViewById(R.id.textView_NumberTransacts);
        textView_NumberTransacts.setText(Long.toString(databaseHelper.getNumberOfTransacts()));
    }

    public void deletePersonalInfo(View view) {
        databaseHelper.deletePersonalInfo();
        databaseHelper.deleteAllBooks();
        databaseHelper.deleteAllTransacts();

        Intent intent = new Intent(this, InitializeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
