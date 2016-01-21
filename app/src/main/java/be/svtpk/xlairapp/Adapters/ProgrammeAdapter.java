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

import be.svtpk.xlairapp.Data.Programme;
import be.svtpk.xlairapp.R;

/**
 * Created by Sveta on 07/01/16.
 *
 * Source onitemclicklistener: http://venomvendor.blogspot.be/2014/07/setting-onitemclicklistener-for-recycler-view.html
 */
public class ProgrammeAdapter extends RecyclerView.Adapter<ProgrammeAdapter.ProgrammeViewHolder>{

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
    public ProgrammeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_programme, viewGroup, false);
        ProgrammeViewHolder pvh = new ProgrammeViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ProgrammeViewHolder programmeViewHolder, int i) {
        programmeViewHolder.currentProgramme = programmes.get(i);
        programmeViewHolder.position = i;
        programmeViewHolder.programmeTitle.setText(programmes.get(i).getTitle());
        programmeViewHolder.programmeNbBroadcasts.setText(programmes.get(i).getNbBroadcasts() + " uitzendingen");
        programmeViewHolder.programmeDesc.setText(programmes.get(i).getDesc());
        try {
            if(! programmes.get(i).getImageFileSrc().isEmpty()) {
                programmeViewHolder.programmaIcon.setImageURI(
                        Uri.fromFile(new File(programmes.get(i).getImageFileSrc()))
                );
            }
            else {
                programmeViewHolder.programmaIcon.setImageResource(R.drawable.no_image);
            }
        }
        catch (OutOfMemoryError e) {
            programmeViewHolder.programmaIcon.setImageResource(R.drawable.no_image);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ProgrammeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Programme currentProgramme;
        int position;
        CardView cv;
        TextView programmeTitle;
        TextView programmeNbBroadcasts;
        TextView programmeDesc;
        ImageView programmaIcon;

        ProgrammeViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_programme);
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