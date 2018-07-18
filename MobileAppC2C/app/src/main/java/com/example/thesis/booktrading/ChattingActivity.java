package com.example.thesis.booktrading;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.thesis.booktrading.helper.MessagesListAdapter;
import com.example.thesis.booktrading.object.Message;

import java.util.ArrayList;
import java.util.List;

public class ChattingActivity extends AppCompatActivity {

    private Button btnSend;
    private EditText inputMsg;

    // Chat messages list adapter
    private MessagesListAdapter adapter;
    private List<Message> listMessages;
    private ListView listViewMessages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        btnSend = (Button) findViewById(R.id.btnSend);
        inputMsg = (EditText) findViewById(R.id.inputMsg);
        listViewMessages = (ListView) findViewById(R.id.list_view_messages);

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Sending message to web socket server
//                sendMessageToServer(utils.getSendMessageJSON(inputMsg.getText()
//                        .toString()));

                // Clearing the input filed once message was sent
                inputMsg.setText("");
            }
        });

        listMessages = new ArrayList<Message>();

        adapter = new MessagesListAdapter(this, listMessages);
        listViewMessages.setAdapter(adapter);

    }


}
