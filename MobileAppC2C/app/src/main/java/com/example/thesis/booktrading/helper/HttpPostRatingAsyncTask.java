package com.example.thesis.booktrading.helper;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class HttpPostRatingAsyncTask extends AsyncTask<String, String, String> {

    private String data;

    public HttpPostRatingAsyncTask(String passedData) {
        data = passedData;
    }

    @Override
    protected String doInBackground(String... strings) {
        String response = "";
        try {
            URL url = new URL(strings[0]);

            // Create the urlConnection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            urlConnection.setRequestMethod("POST");

            // Send the post body
            if (this.data != null) {
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write("=" + data.toString());
                writer.flush();
            }

            int statusCode = urlConnection.getResponseCode();

            if (statusCode == 200) {

                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());


                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer sb = new StringBuffer();
                String inputLine = "";
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }
                String result = sb.toString();
                response = "Rating success";
            } else {
                InputStream inputStream = new BufferedInputStream(urlConnection.getErrorStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer sb = new StringBuffer();
                String inputLine = "";
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }
                String result = sb.toString();
                response = "There is sending request to Server";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

}
