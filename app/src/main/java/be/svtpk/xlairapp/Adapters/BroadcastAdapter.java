package be.svtpk.xlairapp.Adapters;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import be.svtpk.xlairapp.Data.Broadcast;
import be.svtpk.xlairapp.R;


/**
 * Created by Sveta on 18/01/15.
 */
public class BroadcastAdapter extends BaseAdapter {
    private Context mContext;
    private int lastPosition = -1;
    private LayoutInflater inflater;
    List<Broadcast> broadcasts;

    public BroadcastAdapter(Context c, List<Broadcast> broadcasts) {
        mContext = c;
        this.broadcasts = broadcasts;
        inflater = LayoutInflater.from(c);

    }

    public int getCount() {
        return broadcasts.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        View v = view;
        TextView programmeTitle;
        TextView broadcastTitle;
        ImageButton btn;

        Broadcast item = broadcasts.get(position);
        if (v == null) {
            v = inflater.inflate(R.layout.broadcast_item, parent, false);
        }

        programmeTitle = (TextView) v.findViewById(R.id.broadcast_programme_title);
        broadcastTitle = (TextView) v.findViewById(R.id.broadcast_title);
        btn = (ImageButton) v.findViewById(R.id.play_button);

        programmeTitle.setText(item.getProgramme().getTitle());
        broadcastTitle.setText(item.getTitle());

        lastPosition = position;

        return v;
    }

}
