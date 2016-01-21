package be.svtpk.xlairapp.Adapters;

import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import be.svtpk.xlairapp.Data.Event;
import be.svtpk.xlairapp.R;

/**
 * Created by Sveta on 07/01/16.
 *
 * Source onitemclicklistener: http://venomvendor.blogspot.be/2014/07/setting-onitemclicklistener-for-recycler-view.html
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>{

    List<Event> events;
    public static OnItemClickListener mItemClickListener;

    public EventAdapter(List<Event> events){
        this.events = events;
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_event, viewGroup, false);
        EventViewHolder pvh = new EventViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        eventViewHolder.currentEvent = events.get(i);
        eventViewHolder.position = i;
        eventViewHolder.eventTitle.setText(events.get(i).getTitle());
        eventViewHolder.eventDate.setText(events.get(i).getDatum());
        eventViewHolder.eventDesc.setText(events.get(i).getDescription());
        try {
            if(! events.get(i).getImageFileSrc().isEmpty()) {
                eventViewHolder.eventIcon.setImageURI(
                        Uri.fromFile(new File(events.get(i).getImageFileSrc()))
                );
            }
            else {
                eventViewHolder.eventIcon.setImageResource(R.drawable.no_image);
            }
        }
        catch (OutOfMemoryError e) {
            eventViewHolder.eventIcon.setImageResource(R.drawable.no_image);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Event currentEvent;
        int position;
        CardView cv;
        TextView eventTitle;
        TextView eventDate;
        TextView eventDesc;
        ImageView eventIcon;

        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_event);
            eventTitle = (TextView)itemView.findViewById(R.id.event_title);
            eventDate = (TextView)itemView.findViewById(R.id.event_date);
            eventDesc = (TextView)itemView.findViewById(R.id.event_desc);
            eventIcon = (ImageView)itemView.findViewById(R.id.event_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("XLAir", "click on card view " + position);
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, position);
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}