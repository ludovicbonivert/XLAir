package be.svtpk.xlairapp.Adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
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

import be.svtpk.xlairapp.Data.Programme;
import be.svtpk.xlairapp.LiveFragment;
import be.svtpk.xlairapp.R;

/**
 * Created by Sveta on 07/01/16.
 *
 * Source onitemclicklistener: http://venomvendor.blogspot.be/2014/07/setting-onitemclicklistener-for-recycler-view.html
 */
public class ProgrammeAdapter extends RecyclerView.Adapter<ProgrammeAdapter.PersonViewHolder>{

    List<Programme> programmes;
    public static OnItemClickListener mItemClickListener;

    public ProgrammeAdapter(List<Programme> programmes){
        this.programmes = programmes;
    }

    @Override
    public int getItemCount() {
        return programmes.size();
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_programme, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.currentProgramme = programmes.get(i);
        personViewHolder.position = i;
        personViewHolder.programmeTitle.setText(programmes.get(i).getTitle());
        personViewHolder.programmeNbBroadcasts.setText(programmes.get(i).getNbBroadcasts() + " uitzendingen");
        personViewHolder.programmeDesc.setText(programmes.get(i).getDesc());
        try {
            if(! programmes.get(i).getImageFileSrc().isEmpty()) {
                personViewHolder.programmaIcon.setImageURI(
                        Uri.fromFile(new File(programmes.get(i).getImageFileSrc()))
                );
            }
            else {
                personViewHolder.programmaIcon.setImageResource(R.drawable.no_image);
            }
        }
        catch (OutOfMemoryError e) {
            personViewHolder.programmaIcon.setImageResource(R.drawable.no_image);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Programme currentProgramme;
        int position;
        CardView cv;
        TextView programmeTitle;
        TextView programmeNbBroadcasts;
        TextView programmeDesc;
        ImageView programmaIcon;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            programmeTitle= (TextView)itemView.findViewById(R.id.programme_title);
            programmeNbBroadcasts = (TextView)itemView.findViewById(R.id.programme_nb_broadcasts);
            programmeDesc = (TextView)itemView.findViewById(R.id.programme_desc);
            programmaIcon = (ImageView)itemView.findViewById(R.id.programme_icon);
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