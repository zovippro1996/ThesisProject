package com.example.thesis.booktrading.helper;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;

import com.example.thesis.booktrading.R;

public class FragmentWishList extends Fragment {
    // Bind adapter to the ListView
    ListView listView = null;
    DatabaseHelper databaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get all records
        Cursor records = databaseHelper.getAllBooksWishList();

        View inf = inflater.inflate(R.layout.fragment_wishlist, container, false);

        // Create a new list adapter bound to the cursor
        final BookCursorAdapter adapter = new BookCursorAdapter(
                getActivity(),
                R.layout.list_book_record,
                records,
                ResourceCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );

        // Inflate the layout for this fragment
        listView = (ListView) inf.findViewById(R.id.listView_WishList);
        listView.setAdapter(adapter);

        return inf;

    }
}
