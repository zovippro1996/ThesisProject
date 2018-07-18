package com.example.thesis.booktrading.helper;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.example.thesis.booktrading.AdvertiseActivity;
import com.example.thesis.booktrading.R;

public class FragmentInventory extends Fragment {

    // Bind adapter to the ListView
    ListView listView = null;
    DatabaseHelper databaseHelper;
    Button button_Advertise = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get all records
        Cursor records = databaseHelper.getAllBooksInventory();

        final View inf = inflater.inflate(R.layout.fragment_inventory, container, false);
        // Inflate the layout for this fragment

        // Create a new list adapter bound to the cursor
        final BookCursorAdapter adapter = new BookCursorAdapter(
                getActivity(),
                R.layout.list_book_record,
                records,
                ResourceCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );

        // Inflate the layout for this fragment
        listView = (ListView) inf.findViewById(R.id.listView_Inventory);
        listView.setAdapter(adapter);

        button_Advertise = (Button) inf.findViewById(R.id.button_Advertise);
        final Intent intent = new Intent(inf.getContext(), AdvertiseActivity.class);


        button_Advertise.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                startActivity(intent);
            }});

        return inf;

    }
}
