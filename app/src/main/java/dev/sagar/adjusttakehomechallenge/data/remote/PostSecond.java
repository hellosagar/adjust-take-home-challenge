package dev.sagar.adjusttakehomechallenge.data.remote;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class is responsible for making the POST API request to server where it contains the second as a request body
 */
public class PostSecond extends AsyncTask<Void, Void, String> {

    private final AsyncResponse result;
    private final String second;

    public PostSecond(AsyncResponse asyncResponse, String second) {
        this.result = asyncResponse;
        this.second = second;
    }

    @Override
    protected String doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        StringBuilder output = new StringBuilder();

        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/posts/");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");

            urlConnection.setDoOutput(true);

            OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write("seconds=" + second);

            writer.flush();
            writer.close();
            outputStream.close();

            int code = urlConnection.getResponseCode();
            if (code != 201) {
                throw new IOException("Invalid response from server: " + code);
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                output.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\n" +
                    "    \"seconds\": \"" + second + "\",\n" +
                    "    \"id\": -1\n" +
                    "}";
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        Log.d("Posted second:", second);
        return output.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        result.processFinish(s);
    }
}