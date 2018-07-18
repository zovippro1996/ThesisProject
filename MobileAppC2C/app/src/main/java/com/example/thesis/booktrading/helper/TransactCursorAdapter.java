package com.example.thesis.booktrading.helper;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.example.thesis.booktrading.PrepareFindingActivity;
import com.example.thesis.booktrading.R;
import com.example.thesis.booktrading.object.Book;

import java.util.Locale;

public class TransactCursorAdapter extends ResourceCursorAdapter {
    public TransactCursorAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView textView_BookName = view.findViewById(R.id.textView_BookName);
        textView_BookName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper
                .COL_TRANS_BOOK_NAME))
                .toUpperCase(Locale.ROOT));

        TextView textView_TraderName = view.findViewById(R.id.textView_TraderName);
        textView_TraderName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper
                .COL_TRANS_TARGET_FULLNAME)));

        TextView textView_TargetPhone = view.findViewById(R.id.textView_TargetPhone);
        textView_TargetPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper
                .COL_TRANS_TARGET_PHONE)));

        ImageView imageView_Delete = view.findViewById(R.id.imageView_Delete);
    }
}
