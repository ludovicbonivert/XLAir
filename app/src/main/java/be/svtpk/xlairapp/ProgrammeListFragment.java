package be.svtpk.xlairapp;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
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

import be.svtpk.xlairapp.Adapters.FileDownloader;
import be.svtpk.xlairapp.Adapters.ProgrammeAdapter;
import be.svtpk.xlairapp.Data.Programme;


/**
 * A simple {@link Fragment} subclass.
 *
 * Card view: http://code.tutsplus.com/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465
 * JSON parsing: http://kylewbanks.com/blog/Tutorial-Android-Parsing-JSON-with-GSON
 */
public class ProgrammeListFragment extends Fragment {

    private static final int ARG_MENU_ITEM = 2;
    private List<Programme> programmes;
    private ProgrammeAdapter programmeAdapter;
    private RecyclerView rv;
    private OnFragmentInteractionListener mListener;

    public ProgrammeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_programme_list, container, false);
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

        programmeAdapter.SetOnItemClickListener(new ProgrammeAdapter.OnItemClickListener() {
            public void onItemClick(View v, int position) {

                // Load programme detail fragment on item click
                //getFragmentManager().beginTransaction().add(R.id.content_main, new ProgrammeFragment()).commit();
                mListener.onProgrammeSelected(0);

            }
        });

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(ARG_MENU_ITEM);

        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mReceiver, new IntentFilter(FileDownloader.DOWNLOAD_ACTION));
    }

    DownloadedImageReceiver mReceiver = new DownloadedImageReceiver();

    //The DownloadedImageReceiver listens for updates from the service
    private class DownloadedImageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            syncData();
        }
    }

    protected void syncData(){
        programmeAdapter.notifyDataSetChanged();

    }


    private void handleProgrammeList(final List<Programme> programmes) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProgrammeListFragment.this.programmes.clear();
                ProgrammeListFragment.this.programmes.addAll(programmes);

                programmeAdapter.notifyDataSetChanged();
                for (Programme prog : ProgrammeListFragment.this.programmes) {

                    // Strip away html tags and tabs from description
                    String strippedDesc = Html.fromHtml(prog.getDesc()).toString();
                    strippedDesc = strippedDesc.replace("\t", "");
                    prog.setDesc(strippedDesc);

                    // Sugar ORM save for later use
                    prog.save();
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

        @Override
        protected void onPostExecute(String status) {
            super.onPostExecute(status);


            //Start download service for images and audio
            Intent dlServiceIntent = new Intent(getActivity(), FileDownloader.class);
            getActivity().startService(dlServiceIntent);

        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        public void onProgrammeSelected(int position);
    }
}
