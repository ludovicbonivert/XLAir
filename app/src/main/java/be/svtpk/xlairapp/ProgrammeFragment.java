package be.svtpk.xlairapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import java.io.File;
import java.util.List;
import be.svtpk.xlairapp.Adapters.BroadcastAdapter;
import be.svtpk.xlairapp.Data.Broadcast;
import be.svtpk.xlairapp.Data.Programme;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProgrammeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ProgrammeFragment extends Fragment {

    private static final int ARG_MENU_ITEM = 2;
    private long id;
    private Programme programme;
    private List<Broadcast> broadcasts;
    ListView list;
    BroadcastAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public ProgrammeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_programme, container, false);
        FragmentActivity context = getActivity();

        id = getArguments().getLong("selectedProgramme", 1);
        programme = Programme.findById(Programme.class, id);
        broadcasts = programme.getSavedBroadcasts();

        // Set image header
        ImageView img = (ImageView) view.findViewById(R.id.prog_img);
        try {
            if(! programme.getImageFileSrc().isEmpty()) {
                img.setImageURI(
                        Uri.fromFile(new File(programme.getImageFileSrc()))
                );
            }
            else {
                img.setImageResource(R.drawable.no_image);
            }
        }
        catch (OutOfMemoryError e) {
            img.setImageResource(R.drawable.no_image);
        }

        // Set title and number of episodes
        TextView title = (TextView) view.findViewById(R.id.prog_title);
        title.setText(programme.getTitle());
        TextView nbBroadcasts = (TextView) view.findViewById(R.id.prog_nb_broadcasts);
        nbBroadcasts.setText(programme.getNbBroadcasts() + " uitzendingen");

        // Button on click -> start first broadcast
        FloatingActionButton btn = (FloatingActionButton) view.findViewById(R.id.prog_play);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (broadcasts.size() > 0) {
                    Broadcast selectedBroadcast = broadcasts.get(0);
                    mListener.onBroadcastSelected(selectedBroadcast);
                }
            }
        });

        // Set expandable description
        ExpandableTextView desc = (ExpandableTextView) view.findViewById(R.id.expand_description);
        desc.setText(programme.getDesc());

        // List and adapter for broadcast episodes
        list = (ListView) view.findViewById(R.id.broadcast_list);
        adapter = new BroadcastAdapter(container.getContext(), broadcasts);
        list.setAdapter(adapter);

        // OnClickListener -> click item -> send URL of selected broadcast to MainActivity -> stream URL
        list.setOnItemClickListener(new GridView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Broadcast selectedBroadcast = broadcasts.get(position);
                mListener.onBroadcastSelected(selectedBroadcast);

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
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        public void onBroadcastSelected(Broadcast broadcast);
    }
}
