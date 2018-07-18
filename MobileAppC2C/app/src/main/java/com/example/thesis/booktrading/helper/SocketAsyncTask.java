package com.example.thesis.booktrading.helper;

import android.os.AsyncTask;

import com.example.thesis.booktrading.gnutellaprotocol.Connector;

import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class SocketAsyncTask extends AsyncTask<String, Void, Socket> {

    String ip = "";
    int port = 8089;

    @Override
    protected Socket doInBackground(String... strings) {
        String stringUrl = strings[0];
        StringTokenizer st = new StringTokenizer(stringUrl, ":");

        if (st.countTokens() == 2) {
            this.ip = st.nextToken();
            this.port = Integer.parseInt(st.nextToken());
        }

        Socket s = new Socket();
        try {
            s = new Socket(this.ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}
