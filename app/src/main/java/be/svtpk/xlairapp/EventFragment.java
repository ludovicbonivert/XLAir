package be.svtpk.xlairapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import be.svtpk.xlairapp.Data.Event;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class EventFragment extends Fragment {

    private static final int ARG_MENU_ITEM = 3;
    private long id;
    private Event event;

    private OnFragmentInteractionListener mListener;

    public EventFragment() {
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
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        FragmentActivity context = getActivity();

        id = getArguments().getLong("selectedEvent", 1);
        event = Event.findById(Event.class, id);

        // Set image header
        ImageView img = (ImageView) view.findViewById(R.id.event_img);
        try {
            if(! event.getImageFileSrc().isEmpty()) {
                img.setImageURI(
                        Uri.fromFile(new File(event.getImageFileSrc()))
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
        TextView title = (TextView) view.findViewById(R.id.event_title);
        title.setText(event.getTitle());
        TextView date = (TextView) view.findViewById(R.id.event_date);
        date.setText(event.getDatum());

        // Set description
        TextView desc = (TextView) view.findViewById(R.id.event_description);
        desc.setText(event.getDescription());


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
    }
}
