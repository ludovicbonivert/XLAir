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
import be.svtpk.xlairapp.Data.Broadcast;
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

        // Set adapter to page view
        rv.setAdapter(programmeAdapter);

        programmeAdapter.SetOnItemClickListener(new ProgrammeAdapter.OnItemClickListener() {
            public void onItemClick(View v, int position) {

                // Load programme detail fragment on item click

                Programme selectedProgramme = programmes.get(position);
                mListener.onProgrammeSelected(selectedProgramme.getId());

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
        public void onProgrammeSelected(long id);
    }
}
