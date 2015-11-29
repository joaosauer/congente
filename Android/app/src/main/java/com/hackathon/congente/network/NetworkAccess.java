package com.hackathon.congente.network;

import android.os.AsyncTask;
import android.os.Build;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public abstract class NetworkAccess extends AsyncTask<Void, Void, Void> {
    private static int MILLISECONDS = 1000;

    protected abstract String getURL();
    protected abstract boolean isPOST();
    protected abstract String getPostData();
    protected abstract void dataReceived(String json);

    private String _result;

    @Override
    protected Void doInBackground(Void... voids) {
        _result = sendRequest();
        return null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(!isCancelled())
            dataReceived(_result);
    }

    private String sendRequest() {
        disableConnectionReuseIfNecessary();

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(getURL());
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(30*MILLISECONDS);
            urlConnection.setReadTimeout(60*MILLISECONDS);

            // handle POST parameters
            if (isPOST()) {
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setFixedLengthStreamingMode(getPostData().getBytes().length);
                urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                //send the POST out
                PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                out.print(getPostData());
                out.close();
            }

            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                return "";
            }

            // read output
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return getResponseText(in);

        } catch (MalformedURLException e) {
            // handle invalid URL
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    private String getResponseText(InputStream inStream) {
        // very nice trick from
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        return new Scanner(inStream).useDelimiter("\\A").next();
    }

    private static void disableConnectionReuseIfNecessary() {
        // see HttpURLConnection API doc
        if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }
}
