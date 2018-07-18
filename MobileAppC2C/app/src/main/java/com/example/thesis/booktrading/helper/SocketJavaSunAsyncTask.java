package com.example.thesis.booktrading.helper;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.Socket;

public class SocketJavaSunAsyncTask extends AsyncTask<String, Void, Socket> {
    @Override
    protected Socket doInBackground(String... strings) {
        Socket s = new Socket();
        try {
            s = new Socket("java.sun.com", 80);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}
