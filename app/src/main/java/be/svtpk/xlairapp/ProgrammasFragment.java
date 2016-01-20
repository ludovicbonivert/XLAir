package be.svtpk.xlairapp;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import be.svtpk.xlairapp.Adapters.ProgrammeAdapter;
import be.svtpk.xlairapp.Data.Programme;


/**
 * A simple {@link Fragment} subclass.
 *
 * Card view: http://code.tutsplus.com/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465
 * JSON parsing: http://kylewbanks.com/blog/Tutorial-Android-Parsing-JSON-with-GSON
 */
public class ProgrammasFragment extends Fragment {

    private static final int ARG_MENU_ITEM = 2;
    private List<Programme> programmes;
    private ProgrammeAdapter programmeAdapter;
    private RecyclerView rv;

    public ProgrammasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_programmas, container, false);
        FragmentActivity context = getActivity();

        rv = (RecyclerView) view.findViewById(R.id.rec_view_prog);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);

        // Init programme list adapter
        programmes = Programme.listAll(Programme.class);
        programmeAdapter = new ProgrammeAdapter(programmes);
        // Download the list of programmes from API
        new ProgrammeFetcher().execute();
        // Set adapter to page view
        rv.setAdapter(programmeAdapter);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(ARG_MENU_ITEM);
    }


    private void handleProgrammeList(final List<Programme> programmes) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProgrammasFragment.this.programmes.clear();
                ProgrammasFragment.this.programmes.addAll(programmes);

                programmeAdapter.notifyDataSetChanged();
                for (Programme prog : ProgrammasFragment.this.programmes) {

                    // Strip away html tags and tabs from description
                    String strippedDesc = Html.fromHtml(prog.getDesc()).toString();
                    strippedDesc = strippedDesc.replace("\t", "");
                    prog.setDesc(strippedDesc);

                    // Sugar ORM save for later use
                    prog.save();

                    Toast.makeText(getActivity(), prog.getTitle() + "is saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void failedLoadingProgrammes() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "Failed to load Posts. Have a look at LogCat.", Toast.LENGTH_SHORT).show();
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
                        SharedPreferences sharedPref = getActivity().getSharedPreferences("XLAir", Context.MODE_WORLD_WRITEABLE);
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
                        SharedPreferences.Editor prefEditor = getActivity().getSharedPreferences( "XLAir", Context.MODE_WORLD_WRITEABLE ).edit();
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

    }
}
