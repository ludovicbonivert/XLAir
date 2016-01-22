package be.svtpk.xlairapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.svtpk.xlairapp.Adapters.FileDownloader;
import be.svtpk.xlairapp.Data.Broadcast;
import be.svtpk.xlairapp.Data.Event;
import be.svtpk.xlairapp.Data.Programme;

public class LoadActivity extends Activity {

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        mProgressDialog = ProgressDialog.show(LoadActivity.this, "", "", true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setContentView(R.layout.progress_bar);
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        new ProgrammeFetcher().execute();
        new EventFetcher().execute();
    }

    private void handleProgrammeList(final List<Programme> programmes) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Programme.deleteAll(Programme.class);
                Broadcast.deleteAll(Broadcast.class);

                for (Programme prog : programmes) {

                    // Strip away html tags and tabs from description
                    String strippedDesc = Html.fromHtml(prog.getDesc()).toString();
                    strippedDesc = strippedDesc.replace("\t", "");
                    prog.setDesc(strippedDesc);

                    // Sugar ORM save for later use
                    prog.save();

                    // Fetch broadcasts
                    new BroadcastFetcher().execute(prog.getId());
                }

            }
        });
    }

    private void handleBroadcasts(final long id, final List<Broadcast> broadcasts) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Save broadcasts for programme
                Programme programme = Programme.findById(Programme.class, id);

                try {
                    for (Broadcast br : broadcasts) {
                        br.setProgramme(programme);
                        br.save();
                    }
                }
                catch (NullPointerException ex) {
                }
            }
        });

    }

    private void failedLoadingProgrammes() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoadActivity.this, "Failed to load, make sure your internet connection is on.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class ProgrammeFetcher extends AsyncTask<Void, Void, String> {
        private static final String TAG = "ProgrammeFetcher";
        public static final String SERVER_URL = "http://www.xlair.be/programmas/data/all";

        @Override
        protected String doInBackground(Void... params) {
            try {
                //Create an HTTP client
                HttpClient client = new DefaultHttpClient();
                HttpGet getRequest = new HttpGet(SERVER_URL);

                //Perform the request and check the status code
                HttpResponse response = client.execute(getRequest);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();

                    try {
                        //Read the server response and attempt to parse it as JSON
                        Reader reader = new InputStreamReader(content);

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.setDateFormat("yyyy-MM-dd hh:mm:ss");
                        Gson gson = gsonBuilder.create();
                        List<Programme> programmes = new ArrayList<Programme>();
                        programmes = Arrays.asList(gson.fromJson(reader, Programme[].class));


                        String programmesAsString = new Gson().toJson(programmes);
                        SharedPreferences sharedPref = getSharedPreferences("XLAir", Context.MODE_WORLD_WRITEABLE);
                        String savedProgrammesJson = sharedPref.getString("programmesJson", "");

                        content.close();

                        // If data from internet == data in shared prefs, don't do anything
                        // Else persist data
                        if (programmesAsString.equals(savedProgrammesJson)) {
                            Log.d("XLAir", "Programme data is not changed");
                        }
                        else {
                            Log.d("XLAir", "Programme data is changed");
                            handleProgrammeList(programmes);
                        }

                        // Save the loaded JSON into shared prefs
                        SharedPreferences.Editor prefEditor = getSharedPreferences("XLAir", Context.MODE_WORLD_WRITEABLE).edit();
                        prefEditor.putString("programmesJson", programmesAsString);
                        prefEditor.commit();

                    } catch (Exception ex) {
                        Log.e(TAG, "Failed to parse JSON due to: " + ex);
                        failedLoadingProgrammes();
                    }
                } else {
                    Log.e(TAG, "Server responded with status code: " + statusLine.getStatusCode());
                    failedLoadingProgrammes();
                }
            } catch (Exception ex) {
                Log.e(TAG, "Failed to send HTTP POST request due to: " + ex);
                failedLoadingProgrammes();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("XLAir", "ON POST EXECUTEEEEEEEE");

            //Start download service for images and audio
            Intent dlServiceIntent = new Intent(LoadActivity.this, FileDownloader.class);
            startService(dlServiceIntent);

            mProgressDialog.dismiss();

            // Main Activity
            Intent mainIntent = new Intent(LoadActivity.this, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainIntent);
        }
    }

    private class BroadcastFetcher extends AsyncTask<Long, Void, Void> {
        private static final String TAG = "BroadcastFetcher";
        public static final String SERVER_URL = "http://svtpk.be/files/broadcasts.json";

        @Override
        protected Void doInBackground(Long... params) {
            long id = params[0];

            try {
                //Create an HTTP client
                HttpClient client = new DefaultHttpClient();
                HttpGet getRequest = new HttpGet(SERVER_URL);

                //Perform the request and check the status code
                HttpResponse response = client.execute(getRequest);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();

                    try {
                        //Read the server response and attempt to parse it as JSON
                        Reader reader = new InputStreamReader(content);

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.setDateFormat("yyyy-MM-dd hh:mm:ss");
                        Gson gson = gsonBuilder.create();
                        List<Broadcast> broadcasts = new ArrayList<Broadcast>();
                        broadcasts = Arrays.asList(gson.fromJson(reader, Broadcast[].class));

                        content.close();

                        handleBroadcasts(id, broadcasts);

                    } catch (Exception ex) {
                        Log.e(TAG, "Failed to parse JSON due to: " + ex);
                    }
                } else {
                    Log.e(TAG, "Server responded with status code: " + statusLine.getStatusCode());
                }
            } catch (Exception ex) {
                Log.e(TAG, "Failed to send HTTP POST request due to: " + ex);
            }
            return null;
        }

    }



    private void handleEventList(final List<Event> events) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Event.deleteAll(Event.class);

                for (Event ev : events) {

                    // Strip away html tags and tabs from description
                    String strippedDesc = Html.fromHtml(ev.getDescription()).toString();
                    strippedDesc = strippedDesc.replace("\t", "");
                    ev.setDescription(strippedDesc);

                    // Sugar ORM save for later use
                    ev.save();

                }

            }
        });
    }

    private void failedLoadingEvents() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoadActivity.this, "Failed to load, make sure your internet connection is on.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class EventFetcher extends AsyncTask<Void, Void, String> {
        private static final String TAG = "EventFetcher";
        public static final String SERVER_URL = "http://www.xlair.be/events/data/all";

        @Override
        protected String doInBackground(Void... params) {
            try {
                //Create an HTTP client
                HttpClient client = new DefaultHttpClient();
                HttpGet getRequest = new HttpGet(SERVER_URL);

                //Perform the request and check the status code
                HttpResponse response = client.execute(getRequest);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();

                    try {
                        //Read the server response and attempt to parse it as JSON
                        Reader reader = new InputStreamReader(content);

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.setDateFormat("yyyy-MM-dd hh:mm:ss");
                        Gson gson = gsonBuilder.create();
                        List<Event> events = new ArrayList<Event>();
                        events = Arrays.asList(gson.fromJson(reader, Event[].class));


                        String eventsAsString = new Gson().toJson(events);
                        SharedPreferences sharedPref = getSharedPreferences("XLAir", Context.MODE_WORLD_WRITEABLE);
                        String savedEventsJson = sharedPref.getString("eventsJson", "");

                        content.close();

                        // If data from internet == data in shared prefs, don't do anything
                        // Else persist data
                        if (eventsAsString.equals(savedEventsJson)) {
                            Log.d("XLAir", "Event data is not changed");
                        }
                        else {
                            Log.d("XLAir", "Event data is changed");
                            handleEventList(events);
                        }

                        // Save the loaded JSON into shared prefs
                        SharedPreferences.Editor prefEditor = getSharedPreferences("XLAir", Context.MODE_WORLD_WRITEABLE).edit();
                        prefEditor.putString("eventsJson", eventsAsString);
                        prefEditor.commit();

                    } catch (Exception ex) {
                        Log.e(TAG, "Failed to parse JSON due to: " + ex);
                        failedLoadingEvents();
                    }
                } else {
                    Log.e(TAG, "Server responded with status code: " + statusLine.getStatusCode());
                    failedLoadingEvents();
                }
            } catch (Exception ex) {
                Log.e(TAG, "Failed to send HTTP POST request due to: " + ex);
                failedLoadingEvents();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String status) {
            super.onPostExecute(status);

        }

    }

}