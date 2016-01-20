package be.svtpk.xlairapp;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import be.svtpk.xlairapp.Adapters.ProgrammeAdapter;
import be.svtpk.xlairapp.Data.Programme;
import be.svtpk.xlairapp.Data.Broadcast;


/**
 * A simple {@link Fragment} subclass.
 *
 * Source based on: http://code.tutsplus.com/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465
 */
public class ProgrammasFragment extends Fragment {

    private static final int ARG_MENU_ITEM = 2;

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

        //TODO: TEST
        initializeData();
        initializeAdapter();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(ARG_MENU_ITEM);
    }

    //TODO: TEST
    private List<Programme> programmes;
    private List<Broadcast> broadcasts;
    private RecyclerView rv;

    private void initializeData(){
        broadcasts = new ArrayList<>();

        programmes = new ArrayList<>();
        programmes.add(new Programme("Programma 1", "bla bla bla", R.drawable.ic_menu_camera, broadcasts));
        programmes.add(new Programme("Programma 2", "hoi", R.drawable.ic_menu_camera, broadcasts));
        programmes.add(new Programme("Programma 3", "test", R.drawable.ic_menu_camera, broadcasts));
    }

    private void initializeAdapter(){
        ProgrammeAdapter adapter = new ProgrammeAdapter(programmes);
        rv.setAdapter(adapter);
    }
}
