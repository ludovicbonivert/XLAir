package be.svtpk.xlairapp;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.apache.http.protocol.HTTP;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    private static final int ARG_MENU_ITEM = 4;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        ImageView btnInfo = (ImageButton) view.findViewById(R.id.btn_info);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType(HTTP.PLAIN_TEXT_TYPE);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"info@xlair.com"}); // recipients
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "XLAir!?");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        ImageView btnJoin = (ImageButton) view.findViewById(R.id.btn_join);
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType(HTTP.PLAIN_TEXT_TYPE);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"join@xlair.com"}); // recipients
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "I wanna join XLAir!");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(ARG_MENU_ITEM);
    }

}
