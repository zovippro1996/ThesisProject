package com.example.thesis.booktrading;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thesis.booktrading.helper.AsyncTaskIpify;
import com.example.thesis.booktrading.helper.DatabaseHelper;
import com.example.thesis.booktrading.helper.HttpPostRatingAsyncTask;
import com.example.thesis.booktrading.object.Book;
import com.example.thesis.booktrading.object.Transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TargetProfileActivity extends AppCompatActivity {

    String BookName = "";
    float BookPrice = 0;
    int BookisBuy = 0;

    String targetPhone = "";
    String targetName = "";
    String targetIP = "";

    Book currentBook = new Book();

    TextView textView_FullName= null;
    TextView textView_Phone = null;
    Button button_Rate = null;
    Button button_Transact = null;
    TextView textView_RateLabel = null;
    TextView textview_ServerPoint = null;
    RatingBar ratingBar = null;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_profile);

        Intent intent = getIntent();
        BookName = intent.getStringExtra("bookname");
        targetPhone = intent.getStringExtra("phone");
        targetIP = intent.getStringExtra("ipaddress");
        targetName = intent.getStringExtra("name");

        textview_ServerPoint = findViewById(R.id.textView_ServerPoint);

        //Some url endpoint that you may have
        String myUrl = "http://192.168.0.102:8081/api/users?phone=" + targetPhone;
        //String to place our result in
        String result = "";
        //Instantiate new instance of our class
        AsyncTaskIpify getRequest = new AsyncTaskIpify();

        try {
            System.out.println("Start URL ASYNCHRO"); //Checked
            result = getRequest.execute(myUrl).get();
            if (result != null){
                textview_ServerPoint.setText(result.replace("\"", ""));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        databaseHelper = new DatabaseHelper(this);
        System.out.println("OUT URL ASYNCHRO");
        button_Rate = findViewById(R.id.button_Rate);
        button_Transact = findViewById(R.id.button_Transact);
        textView_RateLabel = findViewById(R.id.textView_RateLabel);
        ratingBar = findViewById(R.id.ratingBar);
        textView_FullName = findViewById(R.id.textView_FullName);
        textView_Phone = findViewById(R.id.textView_Phone);

        textView_FullName.setText(targetName);
        textView_Phone.setText(targetPhone);
    }

    public void onClick_Transact(View view) {
        button_Transact.setBackgroundColor(getResources().getColor(R.color.waitingTransact));
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button_Transact.setBackgroundColor(getResources().getColor(R.color
                        .successTransact));
                button_Transact.setTextColor(getResources().getColor(R.color.colorWhite));
                button_Transact.setEnabled(false);
                button_Rate.setVisibility(View.VISIBLE);
                textView_RateLabel.setVisibility(View.VISIBLE);
                ratingBar.setVisibility(View.VISIBLE);

                if (addNewTransaction()) {
                    Log.w(this.getClass().getName(), "Successfully Saved to Database");

                    // Notify unsuccessful saving
                    popupToast("Transaction succeeded");
                }
            }
        }, 5000);
    }

    public boolean addNewTransaction() {
        try {
            Transaction newTransaction = new Transaction(targetName, targetPhone, BookName,
                    BookPrice, BookisBuy);
            databaseHelper.insertTrans(newTransaction);

        } catch (SQLiteException ex) {
            Log.w(this.getClass().getName(), "Error saving to database");
            // Notify unsuccessful saving
            popupToast("Couldn't save your review into database");
            return false;
        }
        return true;
    }

    private void popupToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void onClick_Contact(View view) {
        button_Transact.setVisibility(View.VISIBLE);

        Intent intent = new Intent(this, ChattingActivity.class);
        intent.putExtra("targetname", targetName);
        intent.putExtra("targetphone", targetPhone);
        intent.putExtra("targetip", targetIP);
        startActivity(intent);
    }

    public void onClick_Rate(View view) {
        Map<String, String> postData = new HashMap<>();

        String Userrating = databaseHelper.getPersonalInfoPhone();
        String Userrated = textView_Phone.getText().toString();
        String RatingPoint = Float.toString(ratingBar.getRating());

        String dataSend = Userrating + "*" + Userrated + "*" + RatingPoint.toString();

        HttpPostRatingAsyncTask task = new HttpPostRatingAsyncTask(dataSend);
        String result = "";
        try {
            result = task.execute("http://192.168.0.102:8081/api/rating").get();
            popupToast("Done result = " + result.toString());
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            popupToast("Done result = " + result.toString());
        }
    }
}
