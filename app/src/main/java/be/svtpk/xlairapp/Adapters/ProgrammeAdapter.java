package be.svtpk.xlairapp.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import be.svtpk.xlairapp.Data.Programme;
import be.svtpk.xlairapp.R;

/**
 * Created by Sveta on 07/01/16.
 */
public class ProgrammeAdapter extends RecyclerView.Adapter<ProgrammeAdapter.PersonViewHolder>{

    List<Programme> programmes;

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
        personViewHolder.programmeTitle.setText(programmes.get(i).getTitle());
        personViewHolder.programmeNbBroadcasts.setText(programmes.get(i).getBroadcastList().size() + " uitzendingen");
        personViewHolder.programmeDesc.setText(programmes.get(i).getDesc());
        personViewHolder.programmaIcon.setImageResource(programmes.get(i).getIconId());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
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
        }
    }

}