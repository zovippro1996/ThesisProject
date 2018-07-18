package com.example.thesis.booktrading.helper;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.example.thesis.booktrading.PrepareFindingActivity;
import com.example.thesis.booktrading.R;
import com.example.thesis.booktrading.object.Book;

import java.util.Locale;

public class BookCursorAdapter extends ResourceCursorAdapter {

    public BookCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
        super(context, layout, cursor, flags);
    }


    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView textView_BookName = view.findViewById(R.id.textView_BookName);
        textView_BookName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_BOOK_NAME))
                .toUpperCase(Locale.ROOT));

        TextView textView_BookPrice = view.findViewById(R.id.textView_BookPriceLabel);
        textView_BookPrice.setText(Book.getStringPrice(Float.parseFloat(cursor.getString(cursor
                .getColumnIndexOrThrow
                        (DatabaseHelper
                                .COL_BOOK_PRICE))
                .toUpperCase(Locale.ROOT))));

        final int bookID = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));

        //Handle buttons and add onClickListeners
        Button callbtn= (Button)view.findViewById(R.id.button_Scan);

        callbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), PrepareFindingActivity.class);
                intent.putExtra("BookID", Integer.toString(bookID));

                context.startActivity(intent);
            }
        });
    }
}
